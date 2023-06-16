package com.example.meongku.ui.main.food

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.meongku.MainActivity
import com.example.meongku.api.CatFoodRecommendation.CatFoodRecommendationRequest
import com.example.meongku.api.CatFoodRecommendation.CatFoodRecommendationResponse
import com.example.meongku.api.RetrofitClient
import com.example.meongku.databinding.FragmentFoodBinding
import com.example.meongku.preference.UserPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FoodFragment : Fragment() {

    private var _binding: FragmentFoodBinding? = null
    private lateinit var userPreferences: UserPreferences

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.d("FoodFragment", "Fragment view created")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FoodFragment", "View created")

        binding.radioAboveOne.text="1 - 7 tahun"
        binding.radioBelowOne.text="0 - 1 tahun"
        // list of cat breeds
        val rasList = arrayOf("Abyssinian", "Bengal", "Birman", "Bombay", "British Shorthair", "Egyptian Mau", "Maine Coon", "Persian"
            , "Ragdoll", "Russian Blue", "Siamese", "Sphynx")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            rasList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userPreferences = UserPreferences(requireContext())
        binding.spinnerRas.adapter = adapter

        binding.spinnerRas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.button.setOnClickListener {
            val nameInput = binding.editTextName.text.toString()

            val foodTypeInput = when {
                binding.radioDry.isChecked -> "Dry"
                binding.radioWet.isChecked -> "Wet"
                else -> ""
            }

            val ageInput = when {
                binding.radioAboveOne.isChecked -> 2
                binding.radioBelowOne.isChecked -> 1
                else -> 1
            }

            val activityInput = when {
                binding.radioIndoor.isChecked -> "Indoor"
                binding.radioOutdoor.isChecked -> "Outdoor"
                binding.radioBoth.isChecked -> "Both"
                else -> ""
            }
            val raceInput = binding.spinnerRas.selectedItem.toString()

            val request = CatFoodRecommendationRequest(nameInput, foodTypeInput, ageInput, activityInput, raceInput)

            val apiService = RetrofitClient(userPreferences).apiInstance()

            apiService.recommendFood(request).enqueue(object:
                Callback<CatFoodRecommendationResponse> {
                override fun onResponse(call: Call<CatFoodRecommendationResponse>, response: Response<CatFoodRecommendationResponse>) {
                    if(response.isSuccessful) {

                        val recommendationResponse = response.body()
                        val data = recommendationResponse?.data
                        Log.d("FoodFragment", "Data received: $data")

                        val intent = Intent(context, RecommendationsActivity::class.java)

                        intent.putExtra("name", data?.name)
                        intent.putExtra("brand", data?.brand)
                        intent.putExtra("productName", data?.productName)
                        intent.putExtra("race", data?.race)
                        intent.putExtra("productLink", data?.productLink)

                        startActivity(intent)
                    } else {
                        Log.d("FoodFragment", "Error in response: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<CatFoodRecommendationResponse>, t: Throwable) {
                    Log.d("FoodFragment", "API call failed", t)
                }
            })
        }
    }

    fun clearInput() {
        binding.editTextName.text.clear()
        binding.radioDry.isChecked = false
        binding.radioWet.isChecked = false
        binding.radioAboveOne.isChecked = false
        binding.radioBelowOne.isChecked = false
        binding.radioIndoor.isChecked = false
        binding.radioOutdoor.isChecked = false
        binding.radioBoth.isChecked = false
        binding.spinnerRas.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        clearInput()
    }
}