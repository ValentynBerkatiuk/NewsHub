package com.simpleappsdev.data

import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.simpleappsdev.data.models.ArticleDTO
import com.simpleappsdev.data.models.Language
import com.simpleappsdev.data.models.ResponseDTO
import com.simpleappsdev.data.models.SortBy
import com.simpleappsdev.data.utils.NewsApiKeyInterceptor
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface NewsApi {

    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 1, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1
    ): Result<ResponseDTO<ArticleDTO>>
}
    fun newsApi(
        baseUrl: String,
        apiKey: String,
        okHttpClient: OkHttpClient? = null,
        json: Json = Json,
    ): NewsApi {
        return retrofit(baseUrl, apiKey, okHttpClient, json).create()
    }

    private fun retrofit(
        baseUrl: String,
        apiKey: String,
        okHttpClient: OkHttpClient?,
        json: Json,
    ): Retrofit {

        val modifiedOkHttpClient: OkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
            .addInterceptor(NewsApiKeyInterceptor(apiKey))
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .client(modifiedOkHttpClient)
            .build()
    }

