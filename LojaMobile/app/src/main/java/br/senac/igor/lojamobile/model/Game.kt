package br.senac.igor.lojamobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = false)
    var id : Int,
    var name : String,
    var price : Float,
    var category : String
)
