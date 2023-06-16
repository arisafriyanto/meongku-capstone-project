package com.example.meongku.ui.main.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.meongku.R
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.article.Article
import com.example.meongku.api.article.ArticleIdResponse
import com.example.meongku.api.article.CreatedAt
import com.example.meongku.databinding.FragmentArticleDetailBinding
import com.example.meongku.preference.UserPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticleDetailFragment : Fragment() {
    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var retrofitClient: RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofitClient = RetrofitClient(UserPreferences(requireContext()))

        hideBottomNavigationBar()

        val articleId = arguments?.getString("articleId")

        if (!articleId.isNullOrEmpty()) {
            fetchArticleDetails(articleId)
        }
    }

    private fun fetchArticleDetails(articleId: String) {
        retrofitClient.apiInstance().getArticleById(articleId)
            .enqueue(object : Callback<ArticleIdResponse> {
                override fun onResponse(
                    call: Call<ArticleIdResponse>,
                    response: Response<ArticleIdResponse>
                ) {
                    if (response.isSuccessful) {
                        val articleResponse = response.body()
                        val article = articleResponse?.article
                        article?.let { showArticleDetails(it) }
                    } else {
                        handleErrorResponse()
                    }
                }

                override fun onFailure(call: Call<ArticleIdResponse>, t: Throwable) {
                    Log.d("ArticleDetailFragment", "${t.message}")
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showArticleDetails(article: Article) {
        Glide.with(requireContext())
            .load(article.article_image)
            .into(binding.ivFotoArtikel)

        binding.tvJudulArtikel.text = article.article_title
        binding.tvKategoriArtikel.text = article.article_category.joinToString(", ")
        binding.tvTanggalArtikel.text = formatDate(article.created_at)
        binding.tvDeskripsiArtikel.text = article.article_body
    }

    private fun handleErrorResponse() {
        Log.d("ArticleDetailFragment", "Failed to retrieve article details")
        Toast.makeText(requireContext(), "Failed to retrieve article details", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideBottomNavigationBar() {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun formatDate(created_at: CreatedAt): String {
        val timestamp = created_at.seconds * 1000L + created_at.nanoseconds / 1000000L
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
}
