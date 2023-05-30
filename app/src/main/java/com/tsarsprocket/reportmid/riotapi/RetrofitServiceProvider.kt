package com.tsarsprocket.reportmid.riotapi

import android.content.Context
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.RIOT_APY_KEY_HEADER
import com.tsarsprocket.reportmid.di.AppScope
import com.tsarsprocket.reportmid.lol.model.Region
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AppScope
class RetrofitServiceProvider @Inject constructor(val context: Context) {
    private val okClient = OkHttpClient.Builder()
        .addInterceptor(RequestRatePolicy(20, 1, TimeUnit.SECONDS))
        .addInterceptor(RequestRatePolicy(100, 2, TimeUnit.MINUTES))
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header(RIOT_APY_KEY_HEADER, InputStreamReader(context.resources.openRawResource(R.raw.riot_api_key)).readText())
                    .build()
            )
        }
        .build()
    private val adapterFactory = RxJava2CallAdapterFactory.create()
    private val converterFactory = GsonConverterFactory.create()
    private val retroCache = Collections.synchronizedMap(HashMap<String, RetroWithServices>())

    fun getServiceUnchecked(region: Region, clazz: Class<*> ): Any {
        val url = getUrl(clazz, region)
        return with(retroCache[ url ] ?: RetroWithServices(
            Retrofit.Builder()
                .client( okClient )
                .baseUrl( url )
                .addCallAdapterFactory( adapterFactory )
                .addConverterFactory( converterFactory )
                .build()
        ).also { retroCache[ url ] = it }
        ) {
            serviceMap[ clazz ] ?: retrofit.create( clazz ).also { serviceMap[ clazz ] = it }
        }
    }

    inline fun <reified T> getService(region: Region, clazz: Class<T> ): T = getServiceUnchecked( region, clazz ) as T

    private fun getUrl( clazz: Class<*>, region: Region): String {
        val serverInfo = clazz.getAnnotation(ServerInfo::class.java)
        val serverPrefix = when ( serverInfo?.callBy ) {
            CallByType.SUPER_REGION -> "${region.superServer.value}."
            CallByType.GLOBAL -> ""
            CallByType.REGION -> "${region.server.value}."
            else -> "${region.server.value}."
        }
        return "https://$serverPrefix${( serverInfo?.baseServer ?: RiotServers.RIOT_API ).value}"
    }

    private class RetroWithServices(val retrofit: Retrofit) {
        val serviceMap: HashMap<Class<*>, Any> = HashMap()
    }
}