package com.most4dev.cryptocurrencycourses.retrofits

import android.content.Context
import com.most4dev.cryptocurrencycourses.Config
import com.most4dev.cryptocurrencycourses.models.CryptoModel
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RetrofitService {

    @GET("api/v3/coins/markets")
    suspend fun top100Cryptocurrencies(
        @Query("vs_currency") vs_currency: String,
        @Query("order") order: String,
        @Query("per_page") per_page: String
    ): List<CryptoModel>



    companion object {
        var retrofitService: RetrofitService? = null

        fun getInstance(context:Context): RetrofitService {

            if (retrofitService == null) {

                val okHttpClient = OkHttpClient.Builder()
                    .cache(Cache(context.cacheDir, 5 * 1024 * 1024))
                    .addInterceptor { chain ->
                        var request: Request = chain.request()
                        request = if (NetworkManager.isNetworkAvailable(context)) request.newBuilder()
                            .header("Cache-Control", "public, max-age=" + 60)
                            .build() else request.newBuilder()
                            .header(
                                "Cache-Control",
                                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                            )
                            .build()
                        chain.proceed(request)
                    }
                    .addNetworkInterceptor { chain ->
                        val response = chain.proceed(chain.request())

                        val cacheControl = CacheControl.Builder()
                            .maxAge(1, TimeUnit.MINUTES)
                            .build()

                        response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheControl.toString())
                            .build()
                    }
                    .build()

                val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Config.baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }

}