package com.milkman.demo.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.milkman.demo.model.BeerCacheModel

@Database(entities = [BeerCacheModel::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun beerDao(): BeerDao

    companion object{
        @Volatile
        private var DB: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase =
            DB ?: synchronized(this) {
                DB
                    ?: buildDatabase(context).also { DB = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                LocalDatabase::class.java, "Beer.db")
                .build()
    }


}