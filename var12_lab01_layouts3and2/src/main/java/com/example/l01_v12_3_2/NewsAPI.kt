package com.example.l01_v12_3_2

import androidx.annotation.Nullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.lang.reflect.Type


class NewsAPI {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.opennet.ru/")
        .addConverterFactory(FACTORY)
        .build()

    private val newsService = retrofit.create(NewsService::class.java)

    suspend fun getAllPosts(): List<PostSummary> {
        return withContext(Dispatchers.IO){
            newsService.getAllPosts().await().list
        }
    }

    interface NewsService {
        @GET("opennews/index.shtml")
        fun getAllPosts(@Query("skip") skip: Number = 0): Call<PostSummaries>
    }

    class PostSummary(val num: Number, val title: String, val text: String)
    class PostSummaries(val list: List<PostSummary>)

    companion object {
        private var INSTANCE: NewsAPI? = null
        fun getInstance(): NewsAPI {
            if(INSTANCE === null)
                INSTANCE = NewsAPI()
            return INSTANCE!!
        }

        val FACTORY: Converter.Factory = object : Converter.Factory() {
            @Nullable
            override fun responseBodyConverter(
                type: Type, annotations: Array<Annotation?>?, retrofit: Retrofit?
            ): Converter<ResponseBody, *>? {
                return if (type === PostSummaries::class.java) PostSummariesAdapter() else null
            }
        }
    }

    class PostSummariesAdapter: Converter<ResponseBody, PostSummaries> {
        @Throws(IOException::class)
        override fun convert(responseBody: ResponseBody): PostSummaries {
            return PostSummaries(
                Regex(
                    "<a href=\"/opennews/art\\.shtml\\?num=(\\d+)\" class=title2>(.*?)</a>.*?<td class=chtext2 colspan=2>\\n(.*?...)\\n<div",
                    setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
                ).findAll(responseBody.string()).map {
                    PostSummary(it.groupValues[1].toInt(), it.groupValues[2], it.groupValues[3])
                }.toList()
            )
        }
    }
}