package com.example.meongku.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.meongku.R
//import com.example.meongku.api.RetrofitClient
//import com.example.meongku.api.catlist.Cat
//import com.example.meongku.api.catlist.CatResponse
//import com.example.meongku.api.catlist.CatService
//import com.example.meongku.api.retrofit.RetrofitClient
//import com.example.meongku.preference.UserPreferences
//import com.example.meongku.ui.main.cat.CatAdapter
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class CatListFragment : Fragment() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: CatAdapter
//    private lateinit var retrofitClient: RetrofitClient
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_cat_list, container, false)
//
//        recyclerView = view.findViewById(R.id.catRaceRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(activity)
//
//        // Get the Retrofit instance from the RetrofitClient
//        retrofitClient = RetrofitClient(UserPreferences(this))
//
//        // Create a CatService instance
//        val catService = retrofit.create(CatService::class.java)
//
//        // Make a GET request to the cat list endpoint
//        catService.getAllCats().enqueue(object : Callback<CatResponse> {
//            override fun onResponse(call: Call<CatResponse>, response: Response<CatResponse>) {
//                if (response.isSuccessful) {
//                    val cats = response.body()?.cats ?: emptyList()
//                    adapter = CatAdapter(cats)
//                    recyclerView.adapter = adapter
//                }
//            }
//
//            override fun onFailure(call: Call<CatResponse>, t: Throwable) {
//                // Handle failure
//            }
//        })
//
//        return view
//    }
//}