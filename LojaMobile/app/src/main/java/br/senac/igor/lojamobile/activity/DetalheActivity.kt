package br.senac.igor.lojamobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.database.CartDatabase
import br.senac.igor.lojamobile.databinding.ActivityDetalheBinding
import br.senac.igor.lojamobile.model.Game
import br.senac.igor.lojamobile.model.Produto
import com.squareup.picasso.Picasso

class DetalheActivity : AppCompatActivity() {

    lateinit var b : ActivityDetalheBinding
    lateinit var produto : Produto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetalheBinding.inflate(layoutInflater)

        produto = intent.getSerializableExtra("Produto") as Produto

        setContentView(b.root)
    }

    fun montarUI() {
        Log.e("Produto Vazio?", produto.toString())

        b.txtNome.setText(produto.nome)

        Picasso.get()
            .load("https://i.postimg.cc/"+produto.link+"/"+produto.id+".jpg")
            .placeholder(R.drawable.hl)
            .error(R.drawable.hl)
            .into(b.Imagem)

        b.chipCategoria.setText(produto.categoria)
        b.txtDescricao.setText(produto.descricao)
        b.txtPreco.setText("R$ " + produto.preco.toString())
        //TODO colocar desconto

        //Igor : func do botão
        b.btnAddCarrinho.setOnClickListener {
            Thread {
                val db = Room.databaseBuilder(it.context, CartDatabase::class.java, "game").build()
                db.gameDao().addToCart(produto)

            }.start()
        }
    }

}