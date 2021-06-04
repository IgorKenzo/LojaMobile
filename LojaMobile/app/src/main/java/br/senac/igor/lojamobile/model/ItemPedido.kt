package br.senac.igor.lojamobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemPedido(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var produto : Produto = Produto(),
    var quantidade : Int = 0
)
