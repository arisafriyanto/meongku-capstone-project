package com.example.meongku.ui.main.cat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.meongku.R
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.catlist.Cat
import com.example.meongku.api.catlist.CatIdResponse
import com.example.meongku.preference.UserPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatDetailFragment : Fragment() {
    private lateinit var ivFotoKucing: ImageView
    private lateinit var tvRasKucing: TextView
    private lateinit var tvDeskripsiKucing: TextView
    private lateinit var retrofitClient: RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cat_detail, container, false)

        ivFotoKucing = view.findViewById(R.id.ivFotoKucing)
        tvRasKucing = view.findViewById(R.id.tvRasKucing)
        tvDeskripsiKucing = view.findViewById(R.id.tvDeskripsiKucing)

        retrofitClient = RetrofitClient(UserPreferences(requireContext()))

        val catId = arguments?.getInt("catId")

        catId?.let {
            retrofitClient.apiInstance().getCatById(it).enqueue(object : Callback<CatIdResponse> {
                override fun onResponse(call: Call<CatIdResponse>, response: Response<CatIdResponse>) {
                    if (response.isSuccessful) {
                        val catResponse = response.body()
                        val cat = catResponse?.cat
                        cat?.let { showCatDetails(it) }
                    } else {
                        Log.d("Home Fragment", "GAGAL: ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CatIdResponse>, t: Throwable) {
                    Log.d("CATDETAILS", "${t.message}")
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigationBar()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigation_cat)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigationBar()
    }

    private fun showBottomNavigationBar() {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun hideBottomNavigationBar() {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun showCatDetails(cat: Cat) {
        Glide.with(requireContext())
            .load(cat.photo)
            .into(ivFotoKucing)

        tvRasKucing.text = cat.race
        tvDeskripsiKucing.text = cat.desc
    }
}
