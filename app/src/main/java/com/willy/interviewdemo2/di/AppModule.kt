package com.willy.interviewdemo2.di

import com.willy.interviewdemo2.data.api.repo.ApiRepository
import com.willy.interviewdemo2.ui.first.FirstViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    single { ApiRepository(get()) }

    viewModel { FirstViewModel(get()) }
}