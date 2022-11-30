package com.squaretakehome.utils.wsutils

import com.squaretakehome.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Building Retrofit client and all apis needed
 */

object RetrofitHelper {

    /**
     * as dependency injection is not used in this project for brevity,
     * static object initialization is done for demo purpose.
     *
     * @return EmployeeApi: Instance of the api retrofit interface
     **/
    fun getRetrofitInstance(): EmployeeApi {
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(10, TimeUnit.SECONDS)
        httpClient.connectTimeout(5, TimeUnit.SECONDS)
//              In debug/development version, for logging purposes
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
            httpClient.addInterceptor(interceptor)
        }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient.build())
            .build()
            .create(EmployeeApi::class.java)
    }
}
