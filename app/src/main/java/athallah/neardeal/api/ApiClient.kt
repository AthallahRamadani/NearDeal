package athallah.neardeal.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {
    private val BASE_URL = "http://192.168.0.104/newdeal_api/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        var currentRetrofit = retrofit
        if (currentRetrofit != null) return currentRetrofit

            currentRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit = currentRetrofit
        return currentRetrofit
    }

    private fun createOkhttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()
    }
}