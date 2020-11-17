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
import kotlin.Exception

const val STR_ITEM_ICON_RES_PATH = "img/item"

const val STR_URL_DATA_DRAGON = "https://ddragon.leagueoflegends.com"
const val STR_URL_VERSIONS = "$STR_URL_DATA_DRAGON/api/versions.json"
const val STR_URL_CHAMPION_ICONS = "$STR_URL_DATA_DRAGON/cdn/%s/img/champion/%s.png" // version, champion name
const val STR_URL_ITEM_ICON = "$STR_URL_DATA_DRAGON/cdn/%s/img/item/%d.png" // version, item id

class RIOTIconProvider @Inject constructor(val context: Context) {

    //  Properties  ///////////////////////////////////////////////////////////

    val versions by lazy { loadVersions() }
    val currentVersion by lazy { versions.map { it[0] }.cache() }

    val itemCache = ConcurrentHashMap<Int,Drawable>()

    //  Methods  //////////////////////////////////////////////////////////////

    fun getItemImage(itemId: Int): Single<Drawable> = Single.fromCallable {
        try {
            Drawable.createFromStream(context.assets.open("$STR_ITEM_ICON_RES_PATH/$itemId.png"), null)
        } catch (ex: Exception) {
            try {
                itemCache[itemId] ?:
                    Drawable.createFromStream(URL(STR_URL_ITEM_ICON.format(currentVersion.blockingGet(), itemId)).openStream(), null)
                        .also { itemCache[itemId] = it }
            } catch (ex: Exception) {
                ResourcesCompat.getDrawable(context.resources, R.drawable.item_icon_placegolder, null)!!
            }
        }
    }.subscribeOn(Schedulers.io()).cache()

    //  Private Methods  //////////////////////////////////////////////////////

    private fun loadVersions(): Single<Array<String>> = Single.fromCallable {
        val jsonText = InputStreamReader(URL(STR_URL_VERSIONS).openStream()).readText()
        val jsonArray = JSONArray(jsonText)
        Array(jsonArray.length()) { idx -> jsonArray.getString(idx) }
    }.subscribeOn(Schedulers.io()).cache()
}