package com.milkman.demo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.milkman.demo.model.BeerCacheModel

@Dao
interface BeerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(beers : List<BeerCacheModel>)

    @Query("SELECT * FROM beers")
    suspend fun getAllBeers(): List<BeerCacheModel>

    @Query("SELECT * FROM beers WHERE id LIKE :id")
    suspend fun getBeerById(id: String): BeerCacheModel



}