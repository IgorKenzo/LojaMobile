package br.senac.igor.lojamobile.model

import java.io.Serializable

data class Produto(
    var id : Int,
    var nome : String,
    var preco : Double,
    var categoria : String,
    var desconto : Int
) : Serializable

data class Descricao(
    var descricao: String
)