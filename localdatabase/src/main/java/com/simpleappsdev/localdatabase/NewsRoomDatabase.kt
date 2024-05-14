package com.simpleappsdev.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simpleappsdev.localdatabase.dao.ArticlesDao
import com.simpleappsdev.localdatabase.models.ArticleDBO
import com.simpleappsdev.localdatabase.utils.Converters


class NewsDatabase internal constructor(private val database: NewsRoomDatabase) {

    val articlesDao: ArticlesDao
        get() = database.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase: RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}

fun newsDatabase(applicationContext: Context): NewsDatabase {
    val newsRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsRoomDatabase::class.java,
        "newsDatabase"
    ).build()
    return NewsDatabase(newsRoomDatabase)
}