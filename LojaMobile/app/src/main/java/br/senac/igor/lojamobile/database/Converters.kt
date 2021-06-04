package br.senac.igor.lojamobile.database

import androidx.room.TypeConverter
import br.senac.igor.lojamobile.model.Produto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromProduto(prods: Produto): String {
        val gson = Gson()

        return gson.toJson(prods)
    }

    @TypeConverter
    fun toProduto(value: String): Produto {
        val gson = Gson()

        return gson.fromJson(value, object : TypeToken<Produto>() {}.type)
    }


}