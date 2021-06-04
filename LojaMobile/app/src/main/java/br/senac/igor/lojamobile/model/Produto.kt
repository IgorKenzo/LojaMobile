package br.senac.igor.lojamobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Produto(
    var id : Int = 0,
    var nome : String = "",
    var preco : Double = 0.0,
    var categoria : String = "",
    var desconto : Int = 0,
    var descricao: String = "",
    var link : String = ""
) : Serializable