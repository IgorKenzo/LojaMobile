package br.senac.igor.lojamobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import androidx.room.Room
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.database.CartDatabase
import br.senac.igor.lojamobile.databinding.ActivityDetalheBinding
import br.senac.igor.lojamobile.model.ItemPedido
import br.senac.igor.lojamobile.model.Produto
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class DetalheActivity : AppCompatActivity() {

    lateinit var b : ActivityDetalheBinding
    lateinit var produto : Produto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetalheBinding.inflate(layoutInflater)

        produto = intent.getSerializableExtra("Produto") as Produto

        montarUI()

        setContentView(b.root)
    }

    fun montarUI() {

        b.txtNome.setText(produto.nome)

        Picasso.get()
            .load("https://i.postimg.cc/"+produto.link+"/"+produto.id+".jpg")
            .placeholder(R.drawable.hl)
            .error(R.drawable.hl)
            .into(b.Imagem)

        b.chipCategoria.setText(produto.categoria)
        b.txtDescricao.setText(produto.descricao)
        b.txtPreco.setText("R$ " + produto.preco.toString())
        if (produto.desconto > 0) {
            var txt = SpannableString("R$ " + produto.preco.toString())
            txt.setSpan(StrikethroughSpan(),0, txt.length,0)
            b.txtPreco2.text = txt
            b.txtPreco.text = "-" + produto.desconto + "% | RS " + (produto.preco - produto.preco * produto.desconto/100).toString()
        }

        //Igor : func do botão
        b.btnAddCarrinho.setOnClickListener {
            Thread {
                val db = Room.databaseBuilder(it.context, CartDatabase::class.java, "produto").build()

                val prodJson = fromProduto(produto)

                val prodCart = db.gameDao().getItemFromCart(prodJson) as? ItemPedido

                if (prodCart != null) {
                    prodCart.quantidade += 1
                    db.gameDao().updateItemPedido(prodCart)
                    Snackbar.make(it, "Quantidade atualizada no carrinho", Snackbar.LENGTH_SHORT).show()
                }
                else{
                    val item = ItemPedido(produto = produto, quantidade = 1)
                    db.gameDao().addToCart(item)
                    Snackbar.make(it, "Produto adicionado no carrinho", Snackbar.LENGTH_SHORT).show()
                }

            }.start()

            finish()
        }
    }

    fun fromProduto(prods: Produto): String {
        val gson = Gson()

        return gson.toJson(prods)
    }

}