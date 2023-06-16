package com.example.meongku.ui.main.food

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import com.example.meongku.databinding.ActivityRecommendationsBinding

class RecommendationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        val name = intent.getStringExtra("name")
        val brand = intent.getStringExtra("brand")
        val productName = intent.getStringExtra("productName")
        val race = intent.getStringExtra("race")
        val productLink = intent.getStringExtra("productLink")

        binding.nameTextView.text = "Nama Kucing: "+ name
        binding.brandTextView.text = "Merek: "+ brand
        binding.productNameTextView.text = "Nama Produk: "+productName
        binding.raceTextView.text = "Ras: "+race
        binding.linkButton.text = "Dapatkan Produk"

        binding.linkButton.setOnClickListener {
            if (URLUtil.isValidUrl(productLink)) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(productLink)
                startActivity(i)
            } else {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
            }
        }
    }
}