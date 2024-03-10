package com.example.workreminder.di

import android.content.Context
import androidx.room.Room
import com.example.workreminder.data.local.dao.WorkDao
import com.example.workreminder.data.local.database.WorkDatabase
import com.example.workreminder.usecase.UserSharePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): WorkDatabase {
        return Room.databaseBuilder(context, WorkDatabase::class.java, name = "work_db").build()
    }

    @Provides
    @Singleton
    fun provideDao(db: WorkDatabase): WorkDao {
        return db.getWorkDao()
    }

    @Provides
    @Singleton
    fun provideSharePreference(@ApplicationContext context: Context): UserSharePreference {
        return UserSharePreference(context)
    }
}