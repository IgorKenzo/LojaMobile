package br.senac.igor.lojamobile.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.senac.igor.lojamobile.dao.GameDao
import br.senac.igor.lojamobile.model.Game
import br.senac.igor.lojamobile.model.Produto

@Database(entities = arrayOf(Produto::class), version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract fun gameDao() : GameDao
}