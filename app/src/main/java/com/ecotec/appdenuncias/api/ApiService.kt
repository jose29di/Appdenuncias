package com.ecotec.appdenuncias.api

import com.ecotec.appdenuncias.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // LOGIN
    @POST(ApiRoutes.LOGIN)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // REGISTER
    @POST(ApiRoutes.REGISTER)
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    // LOGOUT
    @POST(ApiRoutes.LOGOUT)
    fun logout(@Header("Authorization") authHeader: String): Call<Void>

    // ME - Obtener datos del usuario
    @GET(ApiRoutes.ME)
    fun getMe(@Header("Authorization") authHeader: String): Call<UserResponse>

    // UPDATE PROFILE
    @POST(ApiRoutes.UPDATE_PROFILE)
    fun updateProfile(
        @Header("Authorization") authHeader: String,
        @Body request: UpdateProfileRequest
    ): Call<UpdateProfileResponse>

    // RESET PASSWORD
    @POST(ApiRoutes.RESET_PASS)
    fun resetPassword(@Body request: ResetPassRequest): Call<ResetPassResponse>

    // ADD DENUNCIA
    @POST(ApiRoutes.ADD_DENUNCIA)
    fun addDenuncia(
        @Header("Authorization") authHeader: String,
        @Body request: DenunciaRequest
    ): Call<DenunciaResponse>

    // GET MIS DENUNCIAS
    @GET(ApiRoutes.DENU_MIS)
    fun getMisDenuncias(@Header("Authorization") authHeader: String): Call<DenunciaListResponse>

    // GET DENUNCIAS PUBLICAS
    @GET(ApiRoutes.DENU_PUB)
    fun getDenunciasPublicas(@Header("Authorization") authHeader: String): Call<DenunciaListResponse>

    // ABOUT
    @GET(ApiRoutes.ABOUT)
    fun getAbout(@Header("Authorization") authHeader: String): Call<AboutResponse>

    // DASHBOARD
    @GET(ApiRoutes.DASHBOARD)
    fun getDashboard(@Header("Authorization") authHeader: String): Call<DashboardResponse>
}