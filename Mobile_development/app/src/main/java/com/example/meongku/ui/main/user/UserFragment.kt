package com.example.meongku.ui.main.user

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.user.UserResponse
import com.example.meongku.databinding.FragmentUserBinding
import com.example.meongku.preference.UserPreferences
import com.example.meongku.ui.edituser.EditUserActivity
import com.example.meongku.ui.login.LoginActivity
import com.example.meongku.ui.theme.ThemeActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreferences: UserPreferences
    private lateinit var retrofitClient: RetrofitClient
//    private lateinit var viewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userPreferences = UserPreferences(requireContext())
        retrofitClient = RetrofitClient(userPreferences)

        binding.buttonTheme.setOnClickListener {
            val intent = Intent(requireContext(), ThemeActivity::class.java)
            startActivity(intent)
        }

        binding.buttonEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditUserActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSignOut.setOnClickListener {
            val token = userPreferences.sessionId
            if (token != null) {
                retrofitClient.apiInstance().logout(token).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            userPreferences.clear()
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)

                            Log.d("Logout", "Logout successful: ${response.body()?.string()}")
                        } else {
                            Log.d("Logout", "Logout failed: ${response.errorBody()?.string()}")
                            Toast.makeText(requireContext(), "Logout Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("Logout", "${t.message}")
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = userPreferences.uid

        retrofitClient.apiInstance().getUserByUid(uid!!).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.user
                    userPreferences.userData = Gson().toJson(user)
                    binding.textName.text = user?.name
                    binding.textEmail.text = user?.email
                } else {
                    Log.d("User", response.message())
                    Toast.makeText(requireContext(), "Failed to get user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("User", "${t.message}")
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onResume() {
        super.onResume()
    }

}