package br.senac.igor.lojamobile.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.senac.igor.lojamobile.model.Game

@Dao
interface GameDao {
    @Query("SELECT * from game")
    fun getCart(): List<Game>

    @Insert
    fun addToCart(game: Game)

    @Delete
    fun removeFromCart(game: Game)

    @Query("DELETE FROM game")
    fun emptyCart()
}