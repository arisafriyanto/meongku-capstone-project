package com.example.meongku.ui.main.cat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.catlist.CatResponse
import com.example.meongku.databinding.FragmentCatListBinding
import com.example.meongku.preference.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatListFragment : Fragment() {
    private var _binding: FragmentCatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var catAdapter: CatListAdapter
    private lateinit var retrofitClient: RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatListBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView: RecyclerView = binding.catRecyclerView

        retrofitClient = RetrofitClient(UserPreferences(requireContext()))

        retrofitClient.apiInstance().getAllCats().enqueue(object : Callback<CatResponse> {
            override fun onResponse(call: Call<CatResponse>, response: Response<CatResponse>) {
                if (response.isSuccessful) {
                    val catResponse = response.body()
                    val cats = catResponse?.cats ?: emptyList()
                    catAdapter.updateCats(cats)
                } else {
                    Log.d("CAT LIST", "FAILED: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CatResponse>, t: Throwable) {
                Log.d("CAT LIST", "${t.message}")
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })

        catAdapter = CatListAdapter()

        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = catAdapter

        catAdapter.setOnItemClickListener { catId ->
            Log.d("CAT LIST", catId.toString())
            val action = CatListFragmentDirections.actionCatListFragmentToCatDetailFragment(catId)
            findNavController().navigate(action)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
