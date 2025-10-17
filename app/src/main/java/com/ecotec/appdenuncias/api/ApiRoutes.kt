package com.ecotec.appdenuncias.api

object ApiRoutes {

    // Dominio base de la API
    const val BASE_URL = "https://appdenuncias.alwaysdata.net/api/"

    // Endpoints completos
    const val LOGIN = "${BASE_URL}login.php"
    const val REGISTER = "${BASE_URL}reg.php"
    const val LOGOUT = "${BASE_URL}logout.php"
    const val ME = "${BASE_URL}me.php"
    const val SET_HINT = "${BASE_URL}sethint.php"
    const val GET_HINT = "${BASE_URL}gethint.php"
    const val RESET_PASS = "${BASE_URL}resetpass.php"
    const val ADD_DENUNCIA = "${BASE_URL}denu_add.php"
    const val DENU_PUB = "${BASE_URL}denu_pub.php"
    const val DENU_MIS = "${BASE_URL}denu_mis.php"
    const val ABOUT = "${BASE_URL}about.php"
    const val UPDATE_PROFILE = "${BASE_URL}updateprofile.php"
    const val DASHBOARD = "${BASE_URL}dashboard.php"
}