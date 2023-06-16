package com.example.meongku.api

import com.example.meongku.api.CatFoodRecommendation.CatFoodRecommendationRequest
import com.example.meongku.api.CatFoodRecommendation.CatFoodRecommendationResponse
import com.example.meongku.api.article.ArticleIdResponse
import com.example.meongku.api.article.ArticleResponse
import com.example.meongku.api.catlist.CatIdResponse
import com.example.meongku.api.catlist.CatResponse
import com.example.meongku.api.login.LoginRequest
import com.example.meongku.api.login.LoginResponse
import com.example.meongku.api.ml.PredictionResponse
import com.example.meongku.api.register.RegisterRequest
import com.example.meongku.api.register.RegisterResponse
import com.example.meongku.api.user.UpdateUserRequest
import com.example.meongku.api.user.UserResponse
import com.example.meongku.api.user.editpassword.EditPasswordRequest
import com.example.meongku.api.user.editpassword.EditPasswordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("v1/login")
    fun userLogin(@Body info: LoginRequest): Call<LoginResponse>

    @POST("v1/register")
    fun userRegister(@Body info: RegisterRequest): Call<RegisterResponse>

    @GET("v1/users/{uid}")
    fun getUserByUid(@Path("uid") uid: String): Call<UserResponse>

    @GET("v1/cats")
    fun getAllCats(): Call<CatResponse>

    @GET("v1/cats/{id}")
    fun getCatById(@Path("id") id: Int): Call<CatIdResponse>

    @GET("v1/articles")
    fun getAllArticles(): Call<ArticleResponse>

    @GET("v1/articles/{id}")
    fun getArticleById(@Path("id") id: String): Call<ArticleIdResponse>

    @POST("/v1/logout")
    fun logout(token: String): Call<ResponseBody>

    @PUT("v1/users/{uid}")
    fun editUserByUid(
        @Path("uid") uid: String,
        @Body body: UpdateUserRequest
    ): Call<ResponseBody>

    @PUT("v1/users/{uid}/edit-password")
    fun editPassword(
        @Path("uid") uid: String,
        @Body request: EditPasswordRequest
    ): Call<EditPasswordResponse>

    @Multipart
    @POST("predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<PredictionResponse>

    @POST("v1/cat-food-recommendations")
    fun recommendFood(@Body body: CatFoodRecommendationRequest): Call<CatFoodRecommendationResponse>
}