package br.senac.igor.lojamobile.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.database.CartDatabase
import br.senac.igor.lojamobile.databinding.FragmentCartBinding
import br.senac.igor.lojamobile.databinding.GameCardCartBinding
import br.senac.igor.lojamobile.model.Compra
import br.senac.igor.lojamobile.model.ItemPedido
import br.senac.igor.lojamobile.model.Produto
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.util.*


class CartFragment : Fragment() {
    lateinit var b : FragmentCartBinding
    lateinit var database: DatabaseReference
    lateinit var games: List<ItemPedido>
    lateinit var db : CartDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentCartBinding.inflate(inflater)

        setupFirebase()

        Thread {

            db = Room.databaseBuilder(container!!.context, CartDatabase::class.java, "produto").build()

            updateCart()

            activity?.runOnUiThread {
                updateUi(games)
            }

        }.start()

        b.buttonBuy.setOnClickListener {

            if (games.isNotEmpty()) {
                buy(games)

                Thread {
                    db.gameDao().emptyCart()
                }.start()

                b.container.removeAllViews()

                context?.let { _ ->
                    Snackbar.make(requireContext(), it, getText(R.string.MessagePurchaseSuccess), Snackbar.LENGTH_SHORT).show()
                }

                Thread {
                    activity?.runOnUiThread {
                        updateUi(arrayListOf())
                    }
                }.start()
            }
        }

        return b.root
    }

    fun updateCart() {
        games = db.gameDao().getCart()
    }

    fun buy(games: List<ItemPedido>) {
        val compra = Compra(games = games, dateOfPurchase = Date())
        val newNode = database?.child("comprado")?.push()
        compra.id = newNode.key
        newNode.setValue(compra)
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun setupFirebase() {
        val user = getCurrentUser()
        user?.let {
            database = FirebaseDatabase.getInstance().reference.child(user.uid)
        }
    }

    fun updateUi(games: List<ItemPedido>) {
        b.container.removeAllViews()
        b.textNoItems.isVisible = games.isEmpty()
        b.buttonBuy.isVisible = games.isNotEmpty()
        games.forEach {
            val cardBinding = GameCardCartBinding.inflate(layoutInflater)

            cardBinding.textName.text = it.produto!!.nome
            cardBinding.textPrice.text = "R$" + ((it.quantidade * it.produto!!.preco).toString())
            cardBinding.txtQtd.setText(it.quantidade.toString())
            if (it.produto!!.desconto > 0) {
                var txt = SpannableString( "R$ " + ((it.quantidade * it.produto!!.preco).toString()))
                txt.setSpan(StrikethroughSpan(),0, txt.length,0)
                cardBinding.txtPrice2.text = txt
                cardBinding.textPrice.text = " -" + it.quantidade * it.produto!!.preco + "% | RS " + (it.quantidade * (it.produto!!.preco - it.produto!!.preco * it.produto!!.desconto/100)).toString()
            }
            var prod = it
            cardBinding.imgMenos.setOnClickListener {
                prod.quantidade -= 1
                if (prod.quantidade <= 0) {
                    Thread {
                        db.gameDao().removeFromCart(prod)
                        updateCart()
                        activity?.runOnUiThread {
                            updateUi(this.games)
                        }
                    }.start()
                }else {
                    Thread {
                        db.gameDao().updateItemPedido(prod)
                    }.start()
                    updateUi(games)
                }
            }
            cardBinding.imgMais.setOnClickListener {
                prod.quantidade += 1
                Thread {
                    db.gameDao().updateItemPedido(prod)
                }.start()
                updateUi(games)
            }

            Picasso.get()
                    .load("https://i.postimg.cc/"+it.produto!!.link+"/"+it.produto!!.id + ".jpg")
                    .placeholder(R.drawable.hl)
                    .error(R.drawable.hl)
                    .into(cardBinding.imageViewGame)

            cardBinding.buttonRemove.setOnClickListener {_ ->
                Thread {
                    db.gameDao().removeFromCart(it)
                    updateCart()
                    activity?.runOnUiThread {
                        updateUi(this.games)
                    }
                }.start()
            }

            b.container.addView(cardBinding.root)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CartFragment().apply {

            }
    }
}