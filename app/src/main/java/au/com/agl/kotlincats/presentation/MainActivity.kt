package au.com.agl.kotlincats.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import au.com.agl.kotlincats.R
import au.com.agl.kotlincats.common.Callback
import au.com.agl.kotlincats.data.OwnerApi
import au.com.agl.kotlincats.data.OwnerNetworkRepository
import au.com.agl.kotlincats.data.OwnerRepository
import au.com.agl.kotlincats.data.model.Owner
import au.com.agl.kotlincats.domain.MainFacade
import au.com.agl.kotlincats.domain.MainUseCases
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var refresher: SwipeRefreshLayout
    private lateinit var facade: MainFacade

    private val adapter = CatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refresher = findViewById(R.id.refresh)
        refresher.setOnRefreshListener(this::refresh)
        findViewById<RecyclerView>(R.id.list).adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://agl-developer-test.azurewebsites.net/")
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .build()
        val api = retrofit.create(OwnerApi::class.java)
        val repository: OwnerRepository = OwnerNetworkRepository(api)

        facade = MainUseCases(repository)
        refresh()
    }

    private fun refresh() {
        refresher.isRefreshing = true
        facade.loadGroupedCats(object: Callback<Any>{
            override fun onSuccess(data: Any) {
                (data as List<*>).let(adapter::updateCats)
                Log.d(MainActivity::class.java.simpleName, data.toString())
                refresher.isRefreshing = false
            }

            override fun onError(error: Throwable) {
                Log.e(MainActivity::class.java.simpleName, error.message ?: "Unknown error")
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                refresher.isRefreshing = false
            }
        })
    }
}
