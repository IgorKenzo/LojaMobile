package br.senac.igor.lojamobile.model

import java.util.*

data class Compra(
        var id: String? = null,
        val games: List<Produto>,
        val dateOfPurchase: Date
)
