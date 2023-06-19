package com.example.pagination.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.pagination.Repository.Repository
import com.example.pagination.paging.MoviPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
@HiltViewModel
class viewmodel @Inject constructor(private val Repository:Repository):ViewModel(){
    val MovieList=Pager(PagingConfig(1)){
        MoviPagingSource(Repository)
    }.flow.cachedIn(viewModelScope)
}