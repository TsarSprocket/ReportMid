package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.logError
import com.tsarsprocket.reportmid.riotapi.ddragon.DataDragonService
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.room.ddragon.*
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

class DataDragon(private val db: MainStorage, private val iconProvider: RIOTIconProvider) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://ddragon.leagueoflegends.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val ddragon: DataDragonService = retrofit.create(DataDragonService::class.java)
    private val lang: String
    val tailSubject: ReplaySubject<Tail> = ReplaySubject.createWithSize(1)
    val tail: Tail get() = tailSubject.value ?: throw RuntimeException("Data Dragon is missing its tail")
    val errors: PublishSubject<RuntimeException> = PublishSubject.create()

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

        val championEntities = lstRetroChamps.data.values.map { champ -> ChampionEntity(langId, champ.key, champ.id, champ.name, champ.image.full)
            .also { champEnt -> db.championDao().insert(champEnt).blockingGet() } }

        val lstRetroSummonerSpells = try { ddragon.summonerSpells(ver, lang).blockingFirst() } catch (ex: Exception) { this.logError( "Can't get summoner spells from DD" ); throw ex }

        val summonerSpellEntities = lstRetroSummonerSpells.data.values.map { retroSpell ->
            SummonerSpellEntity(langId, retroSpell.id, retroSpell.key.toLong(), retroSpell.image.full)
                .also { spellEnt -> db.summonerSpellDao().insert(spellEnt).blockingGet() }
        }

        val lstRetroItems = try { ddragon.items(ver, lang).blockingFirst() } catch (ex: Exception) { this.logError( "Can't get items from DD" ); throw ex }

        val itemEntities = lstRetroItems.data.entries.map { (key,data) ->
            ItemEntity(langId, key.toLong(), data.name, data.image.full)
                .also { db.itemDao().insert(it).blockingGet() }
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
    ): Tail
    {
        val runePaths = runePathEntities.map { ent -> RunePathModel(ent.riot_id, ent.key, ent.name, ent.iconPath, iconProvider).let { ent.id to it } }.toMap()

        val runes = runeEntities.mapNotNull { runePaths[it.runePathId]?.createRune(it.riotId, it.key, it.name, it.slotNo, it.iconPath, iconProvider) } +
                PerkModel.getBasicPerks(iconProvider)

        val champs = championEntities.map { ChampionModel(it.riotId, it.riotStrId, it.name, it.iconName, iconProvider) }

        val summonerSpells = summonerSpellEntities.map { SummonerSpellModel(it.key, it.imageName, iconProvider) }

        val items = itemEntities.map { ItemModel(it.riotId, it.name, it.imageName, iconProvider) }

        return Tail(ver,lang, runePaths.values.toList(), runes, champs, summonerSpells, items)
    }

    class Tail(
        val latestVersion: String,
        val language: String,
        val runePaths: List<RunePathModel>,
        val perks: List<PerkModel>,
        val champs: List<ChampionModel>,
        val summonerSpells: List<SummonerSpellModel>,
        val items: List<ItemModel>,
    ) {
        private val runePathRegistry: Map<Int,RunePathModel> = runePaths.map { it.id to it }.toMap()
        private val perkRegistry: Map<Int,PerkModel> = perks.map { it.id to it }.toMap()
        private val champRegistry: Map<Int,ChampionModel> = champs.map { it.id to it }.toMap()
        private val summSpellRegistry: Map<Long,SummonerSpellModel> = summonerSpells.map { it.key to it }.toMap()
        private val itemRegistry: Map<Long,ItemModel> = items.map { it.riotId to it }.toMap()


        fun getRunePathById(id: Int): RunePathModel = runePathRegistry[id] ?: throw RuntimeException("Rune path with unknown id=$id is requested")
        fun getPerkById(id: Int): PerkModel = perkRegistry[id] ?: throw RuntimeException("Rune with unknown id=$id is requested")
        fun getChampionById(id: Int): ChampionModel = champRegistry[id] ?: throw RuntimeException("Champion with unknown id=$id is requested")
        fun getSummonerSpellById(id: Long): SummonerSpellModel = summSpellRegistry[id] ?: throw RuntimeException("Summoner spell with unknown id=$id is requested")
        fun getItemById(id: Long): ItemModel = itemRegistry[id] ?: throw RuntimeException("Item with unknown id=$id is requested")
    }
}