package br.senac.igor.lojamobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import androidx.room.RoomDatabase
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.database.CartDatabase
import br.senac.igor.lojamobile.databinding.FragmentCartBinding
import br.senac.igor.lojamobile.databinding.GameCardBinding
import br.senac.igor.lojamobile.databinding.GameCardCartBinding
import br.senac.igor.lojamobile.model.Compra
import br.senac.igor.lojamobile.model.Game
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class CartFragment : Fragment() {
    lateinit var b : FragmentCartBinding
    lateinit var database: DatabaseReference
    lateinit var games: List<Game>
    lateinit var db : CartDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentCartBinding.inflate(inflater)

        setupFirebase()

        Thread {

            db = Room.databaseBuilder(container!!.context, CartDatabase::class.java, "game").build()
//            db.gameDao().addToCart(Game(1,"Hollow Knight",20f,"MetroidVania"))
//            db.gameDao().addToCart(Game(2,"Ori and the Will of the Wisps",50f,"MetroidVania"))
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

    fun buy(games: List<Game>) {
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

    fun updateUi(games: List<Game>) {
        b.container.removeAllViews()
        b.textNoItems.isVisible = games.isEmpty()
        b.buttonBuy.isVisible = games.isNotEmpty()
        games.forEach {
            val cardBinding = GameCardCartBinding.inflate(layoutInflater)

            cardBinding.textName.text = it.name
            cardBinding.textPrice.text = "R\$${it.price}"

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