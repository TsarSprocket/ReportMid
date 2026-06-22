package com.tsarsprocket.reportmid.lolServicesImpl.riotapi

import android.content.Context
import com.tsarsprocket.reportmid.baseApi.di.AppContext
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.CallByType
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.RiotServers
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServerInfo
import com.tsarsprocket.reportmid.lolServicesImpl.BuildConfig
import com.tsarsprocket.reportmid.lolServicesImpl.R
import com.tsarsprocket.reportmid.lolServicesImpl.di.RequestRatePolicyFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import java.util.Collections
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

internal class ServiceFactoryImpl @Inject constructor(
    @param:AppContext
    private val context: Context,
    requestRatePolicyFactory: RequestRatePolicyFactory,
) : com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory {

    private val okClient = OkHttpClient.Builder()
        .addInterceptor(requestRatePolicyFactory.create(FINE_GRAIN_RATE, FINE_GRAIN_INTERVAL_MILLIS))
        .addInterceptor(requestRatePolicyFactory.create(COARSE_GRAIN_RATE, COARSE_GRAIN_INTERVAL_MILLIS))
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.valueOf(BuildConfig.OKHTTP_LOGGING) })
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

    @Deprecated(message = "Use getService() extension instead")
    override fun getServiceUnchecked(region: Region, clazz: Class<*>): Any {
        val url = getUrl(clazz, region)
        return with(retroCache[url] ?: RetroWithServices(
            Retrofit.Builder()
                .client(okClient)
                .baseUrl(url)
                .addCallAdapterFactory(adapterFactory)
                .addConverterFactory(converterFactory)
                .build()
        ).also { retroCache[url] = it }) {
            serviceMap[clazz] ?: retrofit.create(clazz).also { serviceMap[clazz] = it }
        }
    }

    private fun getUrl(clazz: Class<*>, region: Region): String {
        val serverInfo = clazz.getAnnotation(ServerInfo::class.java)
        val serverPrefix = when (serverInfo?.callBy) {
            CallByType.SUPER_REGION -> "${region.superServer.value}."
            CallByType.GLOBAL -> ""
            CallByType.REGION -> "${region.server.value}."
            else -> "${region.server.value}."
        }
        return "https://$serverPrefix${(serverInfo?.baseServer ?: RiotServers.RIOT_API).value}"
    }

    private class RetroWithServices(val retrofit: Retrofit) {
        val serviceMap: HashMap<Class<*>, Any> = HashMap()
    }

    private companion object {
        const val RIOT_APY_KEY_HEADER = "X-Riot-Token"
        const val FINE_GRAIN_INTERVAL_MILLIS = 1_000L
        const val FINE_GRAIN_RATE = 20
        const val COARSE_GRAIN_INTERVAL_MILLIS = 2 * 60_000L
        const val COARSE_GRAIN_RATE = 100
    }
}