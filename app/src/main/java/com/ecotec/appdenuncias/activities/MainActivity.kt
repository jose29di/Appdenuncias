package com.ecotec.appdenuncias.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.fragments.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Verificar sesión
        val prefs = getSharedPreferences("denunciasApp", MODE_PRIVATE)
        val token = prefs.getString("token", "")
        if (token.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        drawer = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        toolbar = findViewById(R.id.toolbar)
        bottomNav = findViewById(R.id.bottomNavigation)

        // Configurar Toolbar como ActionBar
        setSupportActionBar(toolbar)

        // DrawerToggle (botón ☰)
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.open_drawer, R.string.close_drawer
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // Título inicial
        supportActionBar?.title = "Dashboard"

        val bearerToken = "Bearer $token"

        // NAV DRAWER
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    replaceFragment(DashboardFragment(), "Dashboard")
                }
                R.id.nav_add_denuncia -> {
                    replaceFragment(DenuAddFragment(), "Crear Denuncia")
                }
                R.id.nav_list_denuncias -> {
                    replaceFragment(DenuListFragment.newInstance("todas"), "Mis Denuncias")
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment(), "Perfil")
                }
                R.id.nav_about -> {
                    replaceFragment(AboutFragment(), "Acerca de")
                }
                R.id.nav_logout -> {
                    cerrarSesion(bearerToken)
                }
            }
            drawer.closeDrawers()
            true
        }

        // BOTTOM NAVIGATION
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_privadas -> {
                    replaceFragment(DenuListFragment.newInstance("privada"), "Denuncias Privadas")
                    true
                }
                R.id.nav_crear -> {
                    replaceFragment(DenuAddFragment(), "Crear Denuncia")
                    true
                }
                R.id.nav_publicas -> {
                    replaceFragment(DenuListFragment.newInstance("publica"), "Denuncias Públicas")
                    true
                }
                else -> false
            }
        }

        // Fragment por defecto:
        replaceFragment(DashboardFragment(), "Dashboard")

        // GESTIÓN DE BACK
        onBackPressedDispatcher.addCallback(this) {
            if (drawer.isDrawerOpen(navView)) {
                drawer.closeDrawers()
            } else {
                finish()
            }
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        supportActionBar?.title = title
    }

    private fun cerrarSesion(bearerToken: String) {
        RetrofitClient.api.logout(bearerToken).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                finalizarSesion()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error de conexión, pero se cerrará la sesión localmente",
                    Toast.LENGTH_SHORT
                ).show()
                finalizarSesion()
            }
        })
    }

    private fun finalizarSesion() {
        val prefs = getSharedPreferences("denunciasApp", MODE_PRIVATE)
        prefs.edit().clear().apply()
        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::toggle.isInitialized && toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}
