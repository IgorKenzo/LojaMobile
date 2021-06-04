package br.senac.igor.lojamobile.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import br.senac.igor.lojamobile.dao.GameDao
import br.senac.igor.lojamobile.model.ItemPedido

@Database(entities = arrayOf(ItemPedido::class), version = 2)
@TypeConverters(Converters::class)
abstract class CartDatabase : RoomDatabase() {
    abstract fun gameDao() : GameDao
}