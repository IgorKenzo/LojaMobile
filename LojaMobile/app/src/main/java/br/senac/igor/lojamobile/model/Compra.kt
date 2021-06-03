package br.senac.igor.lojamobile.model

import java.time.LocalDateTime
import java.util.*

data class Compra(
        var id: String? = null,
        val games: List<Produto> = emptyList(),
        val dateOfPurchase: Date = java.util.Calendar.getInstance().time
)
