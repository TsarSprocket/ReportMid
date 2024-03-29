package com.tsarsprocket.reportmid.data_dragon_impl.data

import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.data_dragon_api.data.DataDragon
import com.tsarsprocket.reportmid.data_dragon_api.data.DataDragon.Tail
import com.tsarsprocket.reportmid.data_dragon_impl.retrofit.DataDragonService
import com.tsarsprocket.reportmid.data_dragon_room.ChampionEntity
import com.tsarsprocket.reportmid.data_dragon_room.ItemEntity
import com.tsarsprocket.reportmid.data_dragon_room.LanguageEntity
import com.tsarsprocket.reportmid.data_dragon_room.RuneEntity
import com.tsarsprocket.reportmid.data_dragon_room.RunePathEntity
import com.tsarsprocket.reportmid.data_dragon_room.SummonerSpellEntity
import com.tsarsprocket.reportmid.data_dragon_room.VersionEntity
import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.Item
import com.tsarsprocket.reportmid.lol.model.Perk
import com.tsarsprocket.reportmid.lol.model.RunePath
import com.tsarsprocket.reportmid.lol.model.SummonerSpell
import com.tsarsprocket.reportmid.utils.common.logError
import io.reactivex.subjects.ReplaySubject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import javax.inject.Inject

class DataDragonImpl @Inject constructor(private val db: MainStorage) : DataDragon {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://ddragon.leagueoflegends.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val ddragon: DataDragonService = retrofit.create(DataDragonService::class.java)
    private val lang: String
    override val tailSubject: ReplaySubject<Tail> = ReplaySubject.createWithSize(1)
    override val tail: Tail get() = tailSubject.value ?: throw RuntimeException("Data Dragon is missing its tail")

    init {
        lang = selectLanguage()

        tailSubject.onNext(updateAndLoadTail())
    }

    private fun selectLanguage(): String {
        val locale = Locale.getDefault()
        val langCode = locale.language
        val country = locale.country

        val availableLangs = ddragon.languages().blockingFirst()

        val idealLang = "${langCode}_$country"

        availableLangs.firstOrNull { it.equals(idealLang, ignoreCase = true) }?.let { return it }

        availableLangs.firstOrNull { it.split("_")[0].equals(langCode, ignoreCase = true) }?.let { return it }

        availableLangs.firstOrNull { it.split("_")[1].equals(country, ignoreCase = true) }?.let { return it }

        availableLangs.firstOrNull { it.equals("en_us", ignoreCase = true) }?.let { return it }

        return availableLangs[0]
    }

    private fun updateAndLoadTail(): Tail {
        val versions = ddragon.versions().blockingFirst()
        val latestVersion = versions.first()

        val dbVersions = db.ddragonVersionDao().getAll().blockingFirst()

        val dbLanguages = dbVersions.find { it.version == latestVersion }?.let { db.ddragonLanguageDao().getAllForVersion(it.id).blockingFirst() } ?: listOf()

        return if(dbLanguages.find { it.language == lang } == null) {
            updateTail(latestVersion, lang)
        } else {
            loadLatestTail()
        }
    }

    private fun updateTail(ver: String, lang: String): Tail = db.runInTransaction<Tail> {
        val verId = db.ddragonVersionDao().insert(VersionEntity(ver)).blockingGet()
        val langId = db.ddragonLanguageDao().insert(LanguageEntity(verId, lang)).blockingGet()

        val lstRetroRunes = ddragon.runesReforged(ver, lang).blockingFirst()

        val lstRuneEntities = ArrayList<RuneEntity>()

        val runePathEntities =
            lstRetroRunes.map { retroPath ->
                RunePathEntity(langId, retroPath.id, retroPath.key, retroPath.name, retroPath.icon)
                    .also { runePathEnt ->
                        runePathEnt.id = db.runePathDao().insert(runePathEnt).blockingGet()
                        lstRuneEntities.addAll(retroPath.slots.withIndex().flatMap { (slotIdx, retroSlot) ->
                            retroSlot.runes.map { rune ->
                                RuneEntity(runePathEnt.id, rune.id, rune.key, rune.name, rune.icon, slotIdx)
                                    .also { re -> re.id = db.runeDao().insert(re).blockingGet() }
                            }
                        })
                    }
            }

        val lstRetroChamps = try { ddragon.champions(ver, lang).blockingFirst() } catch (ex: Exception) { this.logError("Can't get champions from DD", ex); throw ex }

        val championEntities = lstRetroChamps.data.values.map { champ ->
            ChampionEntity(langId, champ.key.toLong(), champ.id, champ.name, champ.image.full)
            .also { champEnt -> db.championDao().insert(champEnt).ignoreElement().blockingAwait() } }

        val lstRetroSummonerSpells = try { ddragon.summonerSpells(ver, lang).blockingFirst() } catch (ex: Exception) { this.logError( "Can't get summoner spells from DD", ex ); throw ex }

        val summonerSpellEntities = lstRetroSummonerSpells.data.values.map { retroSpell ->
            SummonerSpellEntity(langId, retroSpell.id, retroSpell.key.toLong(), retroSpell.image.full)
                .also { spellEnt -> db.summonerSpellDao().insert(spellEnt).ignoreElement().blockingAwait() }
        }

        val lstRetroItems = try { ddragon.items(ver, lang).blockingFirst() } catch (ex: Exception) { this.logError( "Can't get items from DD", ex ); throw ex }

        val itemEntities = lstRetroItems.data.entries.map { (key,data) ->
            ItemEntity(langId, key.toInt(), data.name, data.image.full)
                .also { db.itemDao().insert(it).ignoreElement().blockingAwait() }
        }

        tailFromEntities(ver, lang, runePathEntities, lstRuneEntities, championEntities, summonerSpellEntities, itemEntities)
    }

    private fun loadLatestTail(): Tail {
        val lang = db.ddragonLanguageDao().getLatestLanguage().blockingFirst().first() // get latest created language entity
        val ver = db.ddragonVersionDao().getById(lang.versionId).blockingFirst()

        val runePathEnts = db.runePathDao().getByLanguageId(lang.id).blockingFirst()
        val runeEnts = runePathEnts.flatMap { db.runeDao().getByRunePathId(it.id).blockingFirst() }

        val championEnts = db.championDao().getByLangueageId(lang.id).blockingFirst()

        val summonerSpellEnts = db.summonerSpellDao().getByLanguageId(lang.id).blockingFirst()

        val itemEnts = db.itemDao().getByLanguageId(lang.id).blockingFirst()

        return tailFromEntities(ver.version, lang.language, runePathEnts, runeEnts, championEnts, summonerSpellEnts, itemEnts)
    }

    private fun tailFromEntities(
        ver: String,
        lang: String,
        runePathEntities: List<RunePathEntity>,
        runeEntities: List<RuneEntity>,
        championEntities: List<ChampionEntity>,
        summonerSpellEntities: List<SummonerSpellEntity>,
        itemEntities: List<ItemEntity>,
    ): Tail {
        val runePaths = runePathEntities.associate { ent -> RunePath(ent.riot_id, ent.key, ent.name, ent.iconPath).let { ent.id to it } }

        val runes = runeEntities.mapNotNull { runePaths[it.runePathId]?.createRune(it.riotId, it.key, it.name, it.slotNo, it.iconPath) } +
                Perk.getBasicPerks()

        val champs = championEntities.map { Champion(it.riotId, it.name, it.iconName) }

        val summonerSpells = summonerSpellEntities.map { SummonerSpell(it.key, it.imageName) }

        val items = itemEntities.map { Item(it.riotId, it.name, it.imageName) }

        return TailImpl(ver, lang, runePaths.values.toList(), runes, champs, summonerSpells, items)
    }

    class TailImpl(
        override val latestVersion: String,
        override val language: String,
        override val runePaths: List<RunePath>,
        override val perks: List<Perk>,
        override val champs: List<Champion>,
        override val summonerSpells: List<SummonerSpell>,
        override val items: List<Item>,
    ) : Tail {
        private val runePathRegistry: Map<Int, RunePath> = runePaths.associateBy { it.id }
        private val perkRegistry: Map<Int, Perk> = perks.associateBy { it.id }
        private val champRegistry: Map<Long, Champion> = champs.associateBy { it.id }
        private val summSpellRegistry: Map<Long, SummonerSpell> = summonerSpells.associateBy { it.key }
        private val itemRegistry: Map<Int, Item> = items.associateBy { it.riotId }


        override fun getRunePathById(id: Int): RunePath = runePathRegistry[id] ?: throw RuntimeException("Rune path with unknown id=$id is requested")
        override fun getPerkById(id: Int): Perk = perkRegistry[id] ?: throw RuntimeException("Rune with unknown id=$id is requested")
        override fun getChampionById(id: Long): Champion = champRegistry[id] ?: throw RuntimeException("Champion with unknown id=$id is requested")
        override fun getSummonerSpellById(id: Long): SummonerSpell = summSpellRegistry[id] ?: throw RuntimeException("Summoner spell with unknown id=$id is requested")
        override fun getItemById(id: Int): Item = itemRegistry[id] ?: throw RuntimeException("Item with unknown id=$id is requested")
    }
}