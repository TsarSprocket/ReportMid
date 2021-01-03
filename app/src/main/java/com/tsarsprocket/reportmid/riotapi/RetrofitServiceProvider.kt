package com.tsarsprocket.reportmid.riotapi

import android.content.Context
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.RIOT_APY_KEY_HEADER
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.ServerModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class RetrofitServiceProvider @Inject constructor(val context: Context) {
    private val okClient = OkHttpClient.Builder().addInterceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .header(RIOT_APY_KEY_HEADER, InputStreamReader(context.resources.openRawResource(R.raw.riot_api_key)).readText())
                .build()
        )
    }.build()
    private val adapterFactory = RxJava2CallAdapterFactory.create()
    private val converterFactory = GsonConverterFactory.create()
    private val retroCache = Collections.synchronizedMap(HashMap<ServerModel, RetroWithServices>())

    fun getServiceUnchecked(region: RegionModel, clazz: Class<*>): Any =
        with(retroCache[region.server] ?: RetroWithServices(
            Retrofit.Builder()
                .client(okClient)
                .baseUrl("https://${region.server}.api.riotgames.com")
                .addCallAdapterFactory(adapterFactory)
                .addConverterFactory(converterFactory)
                .build()
        ).also { retroCache[region.server] = it }
        ) {
            serviceMap[clazz] ?: retrofit.create(clazz).also { serviceMap[clazz] = it }
        }

    inline fun <reified T> getService(region: RegionModel, clazz: Class<T>): T = getServiceUnchecked(region, clazz) as T

    private class RetroWithServices(val retrofit: Retrofit) {
        val serviceMap: HashMap<Class<*>, Any> = HashMap()
    }
}