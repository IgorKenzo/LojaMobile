package br.senac.igor.lojamobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.database.CartDatabase
import br.senac.igor.lojamobile.databinding.FragmentCartBinding
import br.senac.igor.lojamobile.databinding.GameCardBinding
import br.senac.igor.lojamobile.model.Compra
import br.senac.igor.lojamobile.model.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class CartFragment : Fragment() {
    lateinit var b : FragmentCartBinding
    lateinit var database: DatabaseReference
    lateinit var games: List<Game>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentCartBinding.inflate(inflater)

        setupFirebase()

        val thread = Thread {

            val db = Room.databaseBuilder(container!!.context, CartDatabase::class.java, "game").build()
//            db.gameDao().addToCart(Game(1,"Hollow Knight",20f,"MetroidVania"))
//            db.gameDao().addToCart(Game(2,"Ori and the Will of the Wisps",50f,"MetroidVania"))
            games = db.gameDao().getCart()

            activity?.runOnUiThread {
                updateUi(games)
            }

        }
        thread.start()


        b.buttonBuy.setOnClickListener {

            buy(games)

            val thread = Thread {
                val db = Room.databaseBuilder(container!!.context, CartDatabase::class.java, "game")
                    .build()
                db.gameDao().emptyCart()
            }
            thread.start()

            b.container.removeAllViews()
        }

        return b.root
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

        games.forEach {
            val cardBinding = GameCardBinding.inflate(layoutInflater)

            cardBinding.GameName.text = it.name
            cardBinding.GamePrice.text = it.price.toString()

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