package br.senac.igor.lojamobile.model

data class Produto(
    var id : Int,
    var nome : String,
    var preco : Double,
    var categoria : String,
    var desconto : Int
)
