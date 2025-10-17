package com.ecotec.appdenuncias.models

// ===========================================
// Request Models
// ===========================================
data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val nombres: String,
    val sexo: String,
    val secret_hint: String,
    val secret_answer: String,
    val username: String,
    val password: String,
    val password2: String
)

data class UpdateProfileRequest(
    val nombres: String? = null,
    val sexo: String? = null,
    val username: String? = null,
    val secret_hint: String? = null,
    val secret_answer: String? = null,
    val password: String? = null
)

data class ResetPassRequest(
    val username: String,
    val secret_answer: String,
    val new_password: String,
    val new_password2: String
)

// ===========================================
// Response Models
// ===========================================

data class User(
    val id: Int,
    val username: String,
    val nombres: String,
    val sexo: String,
    val created_at: String? = null
)

data class LoginResponse(
    val ok: Boolean,
    val token: String?,
    val expires_at: String?,
    val user: User?,
    val error: String? = null
)

data class RegisterResponse(
    val ok: Boolean,
    val token: String?,
    val expires_at: String?,
    val user: User?,
    val error: String? = null
)

data class UpdateProfileResponse(
    val ok: Boolean,
    val user: User?,
    val error: String? = null
)

data class Denuncia(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val ciudad: String,
    val provincia: String,
    val fecha_evento: String,
    val tipo: String,
    val visibilidad: String,
    val username: String?,
    val created_at: String? = null
)

data class DenunciaListResponse(
    val ok: Boolean,
    val items: List<Denuncia>?,
    val error: String? = null
)

data class AboutInfo(
    val app_name: String?,
    val version: String?,
    val description: String?,
    val developer: String?,
    val contact_email: String?,
    val website: String?,
    val last_update: String?,
    val created_at: String?
)

data class AboutResponse(
    val ok: Boolean,
    val info: AboutInfo?,
    val error: String? = null
)

data class ResetPassResponse(
    val ok: Boolean,
    val error: String? = null
)

// ===========================================
// Dashboard Models
// ===========================================
data class DashboardResponse(
    val ok: Boolean,
    val total: Int,
    val publicas: Int,
    val privadas: Int,
    val ultimas: List<Denuncia>,
    val items: List<Denuncia>?,
    val error: String? = null
)

// ===========================================
// User Response (para getMe)
// ===========================================
data class UserResponse(
    val ok: Boolean,
    val user: User?,
    val error: String? = null
)

// ===========================================
// Denuncia Request/Response (simplificado)
// ===========================================
data class DenunciaRequest(
    val titulo: String,
    val descripcion: String,
    val ciudad: String,
    val provincia: String,
    val fecha_evento: String,
    val tipo: String,
    val visibilidad: String
)

data class DenunciaResponse(
    val ok: Boolean,
    val denuncia: Denuncia?,
    val error: String? = null
)