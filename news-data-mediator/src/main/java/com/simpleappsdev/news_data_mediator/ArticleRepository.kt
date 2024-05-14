package com.simpleappsdev.news_data_mediator

import com.simpleappsdev.data.NewsApi
import com.simpleappsdev.data.models.ArticleDTO
import com.simpleappsdev.data.models.ResponseDTO
import com.simpleappsdev.localdatabase.NewsDatabase
import com.simpleappsdev.localdatabase.models.ArticleDBO
import com.simpleappsdev.news_data_mediator.mappers.toArticle
import com.simpleappsdev.news_data_mediator.mappers.toAtricleDbo
import com.simpleappsdev.news_data_mediator.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticleRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(
        requestResponseMergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy(),
    ): Flow<RequestResult<List<Article>>> {

        val cacheAllArticles: Flow<RequestResult<List<Article>>> = getCachedFromDatabase()
            .map { result: RequestResult<List<ArticleDBO>> ->
                result.map { articleDBOsList: List<ArticleDBO> ->
                    articleDBOsList.map { it.toArticle() }
                }
            }

        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer()
            .map { result: RequestResult<ResponseDTO<ArticleDTO>> ->
                result.map { articleDTOsList: ResponseDTO<ArticleDTO> ->
                    articleDTOsList.articles.map { it.toArticle() }
                }
            }

        return cacheAllArticles.combine(remoteArticles, requestResponseMergeStrategy::merge)
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if(result.isSuccess) {
                    saveToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
    }

    private suspend fun saveToCache(data: List<ArticleDTO>) {
        val dbo = data.map { articleDto -> articleDto.toAtricleDbo() }
        database.articlesDao.insert(dbo)
    }

    private fun getCachedFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest = database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(dbRequest, start)
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO( "not implemented")
    }

}

