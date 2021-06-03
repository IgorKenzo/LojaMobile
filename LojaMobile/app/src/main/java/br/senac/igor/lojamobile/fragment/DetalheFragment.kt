package br.senac.igor.lojamobile.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.database.CartDatabase
import br.senac.igor.lojamobile.databinding.FragmentDetalheBinding
import br.senac.igor.lojamobile.model.Game
import br.senac.igor.lojamobile.model.Produto
import com.squareup.picasso.Picasso


class DetalheFragment : Fragment() {

    lateinit var b : FragmentDetalheBinding
    lateinit var produto : Produto
    var desricao : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        produto = getArguments()?.getSerializable("Produto") as Produto
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentDetalheBinding.inflate(inflater)

        montarUI()

        return b.root
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
        b.txtPreco.setText("R$ " + produto.preco.toString())
        //TODO colocar desconto

        //Igor : func do botão
        b.btnAddCarrinho.setOnClickListener {
            Thread {
                val db = Room.databaseBuilder(it.context, CartDatabase::class.java, "produto").build()
                db.gameDao().addToCart(produto)

            }.start()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(produto: Produto) : DetalheFragment {
            val myFragment = DetalheFragment()
            val args = Bundle()
            args.putSerializable("Produto", produto)
            myFragment.setArguments(args)
            return myFragment
        }


    }
}