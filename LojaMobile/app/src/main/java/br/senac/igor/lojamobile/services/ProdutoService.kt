package br.senac.igor.lojamobile.services

import br.senac.igor.lojamobile.model.Produto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProdutoService {

    @GET("/produtos")
    fun list(): Call<List<Produto>>

    @GET("/produtos/{id}")
    fun descricao(@Path("id") id: Int): Call<String>

}