package br.senac.igor.lojamobile.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.senac.igor.lojamobile.model.Game
import br.senac.igor.lojamobile.model.Produto

@Dao
interface GameDao {
    @Query("SELECT * from produto")
    fun getCart(): List<Produto>

    @Insert
    fun addToCart(game: Produto)

    @Delete
    fun removeFromCart(game: Produto)

    @Query("DELETE FROM produto")
    fun emptyCart()
}