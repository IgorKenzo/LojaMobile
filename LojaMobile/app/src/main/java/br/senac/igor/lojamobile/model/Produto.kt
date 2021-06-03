package br.senac.igor.lojamobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Produto(
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0,
    var nome : String = "",
    var preco : Double = 0.0,
    var categoria : String = "",
    var desconto : Int = 0,
    var descricao: String = "",
    var link : String = ""
) : Serializable