package br.senac.igor.lojamobile.dao

import androidx.room.*
import br.senac.igor.lojamobile.model.ItemPedido
import br.senac.igor.lojamobile.model.Produto

@Dao
interface GameDao {
    @Query("SELECT * from itempedido")
    fun getCart(): List<ItemPedido>

    @Insert
    fun addToCart(game: ItemPedido)

    @Delete
    fun removeFromCart(game: ItemPedido)

    @Query("DELETE FROM itempedido")
    fun emptyCart()

    @Query("SELECT * FROM itempedido WHERE produto LIKE :prod ")
    fun getItemFromCart(prod: String) : ItemPedido

    @Update
    fun updateItemPedido(itemPedido: ItemPedido)
}