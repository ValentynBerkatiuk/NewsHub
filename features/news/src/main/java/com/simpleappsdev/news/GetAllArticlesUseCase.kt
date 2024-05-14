package com.simpleappsdev.news

import com.simpleappsdev.news_data_mediator.ArticleRepository
import com.simpleappsdev.news_data_mediator.model.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticleRepository) {

    operator fun invoke(): Flow<List<Article>> {
        return repository.getAll()
    }
}