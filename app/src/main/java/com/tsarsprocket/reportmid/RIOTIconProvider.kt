package com.tsarsprocket.reportmid

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

const val STR_ITEM_ICON_ASSET_PATH = "img/item/%d.png"
const val STR_PROFILE_ICON_ASSET_PATH = "img/profileicon/%d.png"
const val STR_CHAMPION_ICON_ASSET_PATH = "img/champion/%s.png"
const val STR_SUMMONER_SPELL_ICON_ASSET_PATH = "img/spell/%s.png"
const val STR_RUNE_ICON_ASSET_PATH = "img/%s"

const val STR_URL_DATA_DRAGON = "https://ddragon.leagueoflegends.com"
const val STR_URL_VERSIONS = "$STR_URL_DATA_DRAGON/api/versions.json"
const val STR_URL_IMAGE_BASE = "$STR_URL_DATA_DRAGON/cdn/%s/img/%s" // version, path
const val STR_URL_UNVERSIONED_IMAGE_BASE = "$STR_URL_DATA_DRAGON/cdn/img/%s" // path
const val STR_PATH_PROFILE_ICONS = "profileicon/%d.png" // profile icon id
const val STR_PATH_CHAMPION_ICONS = "champion/%s.png" // champion name
const val STR_PATH_ITEM_ICONS = "item/%d.png" // item id
const val STR_PATH_SUMMONER_SPELL_ICONS = "spell/%s.png" // summoner spell key
const val STR_PATH_RUNE_ICONS = "%s" // rune/rune path icon path

@Singleton
class RIOTIconProvider @Inject constructor(val context: Context) {

    //  Properties  ///////////////////////////////////////////////////////////

    private val versions by lazy { loadVersions() }
    private val currentVersion by lazy { versions.map { it[0] }.cache() }

    private val itemIconCache = ConcurrentHashMap<Int,Drawable>()
    private val profileIconCache = ConcurrentHashMap<Int,Drawable>()
    private val championIconCache = ConcurrentHashMap<String,Drawable>()
    private val summonerSpellCache = ConcurrentHashMap<String,Drawable>()
    private val runeIconCache = ConcurrentHashMap<String,Drawable>()

    //  Methods  //////////////////////////////////////////////////////////////

    fun getItemIcon(itemId: Int): Single<Drawable> =
        getVersionedDrawableInThreeStages(itemId, itemIconCache, STR_ITEM_ICON_ASSET_PATH.format(itemId),
            STR_PATH_ITEM_ICONS.format(itemId), R.drawable.item_icon_placegolder)

    fun getProfileIcon(profileIconId: Int) =
        getVersionedDrawableInThreeStages(profileIconId, profileIconCache, STR_PROFILE_ICON_ASSET_PATH.format(profileIconId),
            STR_PATH_PROFILE_ICONS.format(profileIconId), R.drawable.champion_icon_placegolder)

    fun getChampionIcon(championKey: String) =
        getVersionedDrawableInThreeStages(championKey, championIconCache, STR_CHAMPION_ICON_ASSET_PATH.format(championKey),
            STR_PATH_CHAMPION_ICONS.format(championKey), R.drawable.champion_icon_placegolder)

    fun getSummonerSpellIcon(spellName: String) =
        getVersionedDrawableInThreeStages(spellName, summonerSpellCache, STR_SUMMONER_SPELL_ICON_ASSET_PATH.format(spellName),
            STR_PATH_SUMMONER_SPELL_ICONS.format(spellName), R.drawable.item_icon_placegolder)

    fun getRuneIcon(runeIconPath: String) =
        getUnversionedDrawableInThreeStages(runeIconPath, runeIconCache, STR_RUNE_ICON_ASSET_PATH.format(runeIconPath),
            STR_PATH_RUNE_ICONS.format(runeIconPath), R.drawable.item_icon_placegolder)

    fun getRunePathIcon(runePathIconPath: String) = getRuneIcon(runePathIconPath)

    //  Private Methods  //////////////////////////////////////////////////////

    private fun<T> getVersionedDrawableInThreeStages(id: T, cache: MutableMap<T,Drawable>, assetPath: String, webPath: String, resId: Int): Single<Drawable> = Single.fromCallable {
        try {
            Drawable.createFromStream(context.assets.open(assetPath), null)
        } catch (ex: Exception) {
            try {
                cache[id] ?: Drawable.createFromStream(URL(STR_URL_IMAGE_BASE.format(currentVersion.blockingGet(), webPath)).openStream(), null)
                    .also { cache[id] = it }
            } catch (ex: Exception) {
                ResourcesCompat.getDrawable(context.resources, resId, null)!!
            }
        }
    }.subscribeOn(Schedulers.io()).cache()

    private fun<T> getUnversionedDrawableInThreeStages(id: T, cache: MutableMap<T,Drawable>, assetPath: String, webPath: String, resId: Int): Single<Drawable> = Single.fromCallable {
        try {
            Drawable.createFromStream(context.assets.open(assetPath), null)
        } catch (ex: Exception) {
            try {
                cache[id] ?: Drawable.createFromStream(URL(STR_URL_IMAGE_BASE.format(webPath)).openStream(), null)
                    .also { cache[id] = it }
            } catch (ex: Exception) {
                ResourcesCompat.getDrawable(context.resources, resId, null)!!
            }
        }
    }.subscribeOn(Schedulers.io()).cache()

    private fun loadVersions(): Single<Array<String>> = Single.fromCallable {
        val jsonText = InputStreamReader(URL(STR_URL_VERSIONS).openStream()).readText()
        val jsonArray = JSONArray(jsonText)
        Array(jsonArray.length()) { idx -> jsonArray.getString(idx) }
    }.subscribeOn(Schedulers.io()).cache()
}