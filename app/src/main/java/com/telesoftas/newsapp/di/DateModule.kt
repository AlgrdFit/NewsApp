package com.telesoftas.newsapp.di

import com.telesoftas.newsapp.utlis.DateFormatter
import com.telesoftas.newsapp.utlis.DateFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DateModule {

    @Binds
    @Singleton
    abstract fun bindDateFormatter(dateFormatterImpl: DateFormatterImpl): DateFormatter
}