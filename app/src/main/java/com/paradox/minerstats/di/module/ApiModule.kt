package com.paradox.minerstats.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializer
import com.paradox.minerstats.core.config.Constants.BASE_URL
import com.paradox.minerstats.core.extensions.toDate
import com.paradox.minerstats.core.extensions.toString
import com.paradox.minerstats.model.source.api.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    fun providesApiService(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder().registerTypeAdapter(Date::class.java, JsonSerializer<Date> { date: Date?, _, context ->
            val parsed = date?.toString("yyyy-MM-dd'T'HH:mm:ss'Z'")
            if (parsed == null) null
            else context?.serialize(parsed)
        }).registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json: JsonElement?, _, _ ->
            json?.asString?.toDate("yyyy-MM-dd'T'HH:mm:ss'Z'")
        }).create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build().create(ApiService::class.java)
    }

    @Provides
    fun providesGSON() = Gson()
}