package com.example.balo.utils

import com.chibatching.kotpref.KotprefModel

object Pref : KotprefModel() {
    var idUser by stringPref("")
}
