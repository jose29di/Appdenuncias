Aplicacion de tarea para la universidad ecotec en la faculta de Ingenieria de sistemas inteligentes del Grupo 15 - materia Desarrollo de Apps

# App Actividad1 – Denuncias (Android/Kotlin)

Aplicación Android para **registro y consulta de denuncias** con autenticación por token y consumo de API REST (PHP/Laravel u otro backend). Incluye **ActionBar con menú hamburguesa (Drawer)**, **Bottom Navigation (Privadas | Crear | Públicas)** y pantallas de **Dashboard, Perfil y Acerca de**.

---

##  Funciones principales

- **Autenticación** (login, registro, logout, sesión persistente con `SharedPreferences`).
- **ActionBar** con:
  - **☰ Menú hamburguesa** para abrir **Drawer**.
  - **Título dinámico** según pantalla.
  - **Acciones** ( Información / Perfil opcional).
- **Drawer (Menú lateral)** con:
  -  **Dashboard** (KPIs + últimas denuncias).
  -  **Perfil** (ver/editar).
  -  **Acerca de** (información de la app).
  -  **Cerrar sesión**.
- **Bottom Navigation**: **Privadas | Crear | Públicas**.
- **Denuncias**:
  - Listado de **privadas** (propias).
  - Listado de **públicas** (toda la comunidad).
  - **Crear** denuncia (pública/privada).
- **Retrofit + OkHttp + Gson** para consumo de API.
- **Material Design** (Material Components).

---

##  Arquitectura de la app

- **Vista/Fragmentos**: `DashboardFragment`, `DenuListFragment` (privadas), `DenuPubFragment` (públicas), `DenuAddFragment`, `ProfileFragment`, `AboutFragment`.
- **Activity principal**: `MainActivity` integra **Toolbar**, **Drawer** y **BottomNavigationView**.
- **Autenticación**: `LoginActivity`, `RegisterActivity`, `SplashActivity` (verifica token).
- **Networking**: `RetrofitClient`, `ApiService`, `ApiRoutes` (BASE_URL).
- **UI**: `DenunciaAdapter` + layouts en `res/layout`.
- **Modelos**: `ApiModels.kt`.

> Patrón general: *Activity huésped + múltiples Fragments* con navegación controlada manualmente (reemplazos en `fragmentContainer`).

---

##  Requisitos

- **Android Studio** (Giraffe o superior).
- **JDK 17** (recomendado).
- **Android SDK** actualizado.
- **Backend/API** accesible (URL base, ver sección de configuración).

Dependencias mínimas (Gradle):
```gradle
dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib"
    implementation "androidx.core:core-ktx:1.13.1"
    implementation "androidx.appcompat:appcompat:1.7.0"
    implementation "com.google.android.material:material:1.12.0"
    implementation "androidx.recyclerview:recyclerview:1.3.2"
    implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    // Retrofit / OkHttp / Gson
    implementation "com.squareup.retrofit2:retrofit:2.11.0"
    implementation "com.squareup.retrofit2:converter-gson:2.11.0"
    implementation "com.squareup.okhttp3:okhttp:4.12.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"
}
```

---

## 🔧 Configuración

1. **Clonar** el repositorio.
2. Abrir en **Android Studio** y esperar la sincronización de Gradle.
3. **Configurar URL base** de la API:
   - En `ApiRoutes.kt` o `RetrofitClient.kt`, establece tu `BASE_URL`, por ejemplo:
     ```kotlin
     object ApiRoutes {
         const val BASE_URL = "https://tu-dominio.com/api/"
     }
     ```
4. **Permisos**: verificar `AndroidManifest.xml` (Internet, etc.).
5. **Variables de sesión**: la app guarda `token`, `username` y `nombres` en `SharedPreferences` (`denunciasApp`).

---

##  Ejecución

- **Debug en emulador/dispositivo**:
  1. Conecta dispositivo o inicia un emulador.
  2. `Run > Run 'app'` en Android Studio.

- **Generar APK**:
  - `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
  - El APK quedará en `app/build/outputs/apk/`.

---

## 🗂 Estructura del proyecto

```
app/
├─ manifests/
│  └─ AndroidManifest.xml
├─ java|kotlin/com/ecotec/appdenuncias/
│  ├─ activities/
│  │  ├─ SplashActivity.kt
│  │  ├─ LoginActivity.kt
│  │  ├─ RegisterActivity.kt
│  │  └─ MainActivity.kt
│  ├─ fragments/
│  │  ├─ DashboardFragment.kt
│  │  ├─ DenuListFragment.kt
│  │  ├─ DenuPubFragment.kt
│  │  ├─ DenuAddFragment.kt
│  │  ├─ ProfileFragment.kt
│  │  └─ AboutFragment.kt
│  ├─ adapter/
│  │  └─ DenunciaAdapter.kt
│  ├─ api/
│  │  ├─ ApiRoutes.kt
│  │  ├─ RetrofitClient.kt
│  │  └─ ApiService.kt
│  └─ models/
│     └─ ApiModels.kt
├─ res/
│  ├─ layout/ (XML de pantallas y celdas)
│  ├─ menu/ (nav_menu.xml, bottom_nav.xml, action_bar_menu.xml)
│  ├─ values/ (strings.xml, colors.xml, themes.xml)
│  └─ mipmap/ (iconos)
└─ build.gradle, settings.gradle, etc.
```

---

##  Consumo de API

Interfaz `ApiService` (ejemplos típicos, ajustar a tu backend):
- `POST   auth/login`
- `POST   auth/register`
- `POST   auth/logout`
- `GET    users/me`
- `PUT    users/me`
- `GET    app/about`
- `GET    denuncias/mias` *(Privadas)*
- `GET    denuncias/publicas` *(Públicas)*
- `POST   denuncias` *(Crear)*

> **Auth:** usar encabezado `Authorization: Bearer <token>` tras el login.  
> Respuestas modeladas en `ApiModels.kt` (e.g., `ok`, `error`, `items`).

---

##  Capturas

Coloca tus imágenes en `docs/screenshots/` y enlázalas aquí:
| Pantalla | Captura |
|---|---|
| Login | `docs/screenshots/login.png` |
| Dashboard | `docs/screenshots/dashboard.png` |
| Privadas | `docs/screenshots/privadas.png` |
| Crear denuncia | `docs/screenshots/crear.png` |
| Públicas | `docs/screenshots/publicas.png` |
| Drawer | `docs/screenshots/drawer.png` |

---



