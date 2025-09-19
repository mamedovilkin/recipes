package com.ilkinmamedov.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ilkinmamedov.data.dao.RecipesDao
import com.ilkinmamedov.data.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

    companion object {
        @Volatile
        private var INSTANCE: RecipesDatabase? = null

        fun getDatabase(context: Context): RecipesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    RecipesDatabase::class.java,
                    "Recipes"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}