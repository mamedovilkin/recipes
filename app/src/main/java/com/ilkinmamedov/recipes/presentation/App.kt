package com.ilkinmamedov.recipes.presentation

import android.app.Application
import com.ilkinmamedov.data.client.RecipesHttpClient
import com.ilkinmamedov.data.database.RecipesDatabase
import com.ilkinmamedov.data.repository.RecipesRepositoryImpl
import com.ilkinmamedov.domain.repository.RecipesRepository
import com.ilkinmamedov.domain.usecase.DetailUseCase
import com.ilkinmamedov.domain.usecase.HomeUseCase
import com.ilkinmamedov.recipes.presentation.screen.detail.DetailViewModel
import com.ilkinmamedov.recipes.presentation.screen.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                module {
                    androidContext(this@App)

                    single { RecipesHttpClient.getInstance() }

                    single { RecipesDatabase.getDatabase(androidContext()).recipesDao() }

                    single<RecipesRepository> { RecipesRepositoryImpl(get(), get()) }

                    single { HomeUseCase(get()) }

                    single { HomeViewModel(get()) }

                    single { DetailUseCase(get()) }

                    factory { (id: Long) -> DetailViewModel(get(), id) }
                }
            )
        }
    }
}