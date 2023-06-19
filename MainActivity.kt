package com.example.pagination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination.Adaptor.Adaptor
import com.example.pagination.Adaptor.Retry_Adaptor
import com.example.pagination.ViewModel.viewmodel
import com.example.pagination.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Binding
    private lateinit var Binding:ActivityMainBinding
    //Inject
    private  val ViewModel:viewmodel by viewModels()
    @Inject
     lateinit var MoviesAdaptor : Adaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        //InitViews
        Binding.apply {
            //CallApi
            lifecycleScope.launch {
                ViewModel.MovieList.collect{ responses->
                    MoviesAdaptor.submitData(responses)
                }
            }
            //Loading
            lifecycleScope.launch {
                MoviesAdaptor.loadStateFlow.collect{
                    Loading.isVisible = it.refresh is LoadState.Loading
                }
            }
            //RecyclerView
            recyclerMain.apply {
                adapter=MoviesAdaptor
                layoutManager=GridLayoutManager(this@MainActivity,2)
            }
            //SwipeRefresh
            Swipe.setOnRefreshListener {
                Swipe.isRefreshing=false
                MoviesAdaptor.refresh()
            }
            recyclerMain.adapter=MoviesAdaptor.withLoadStateFooter(
                Retry_Adaptor{
                    MoviesAdaptor.retry()
                }
            )
        }

    }
}