package br.senac.igor.lojamobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.adapters.CompraRecyvlerViewAdapter
import br.senac.igor.lojamobile.databinding.FragmentComprasBinding
import br.senac.igor.lojamobile.model.Compra
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ComprasFragment : Fragment() {

    lateinit var adapter: CompraRecyvlerViewAdapter
    lateinit var db : DatabaseReference
    lateinit var b : FragmentComprasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        b = FragmentComprasBinding.inflate(inflater)
        configurarFireBase()

        return b.root
    }

        fun getcurrentuser() : FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

    fun configurarFireBase() {
        val usuario = getcurrentuser()

        usuario?.let {
            db = FirebaseDatabase.getInstance().reference.child(usuario.uid)

            val options = FirebaseRecyclerOptions.Builder<Compra>()
                    .setQuery(db.child("comprado"), Compra::class.java)
                    .build()

            adapter = CompraRecyvlerViewAdapter(options)

            b.recyclrerView.adapter = adapter

            b.recyclrerView.layoutManager = LinearLayoutManager(b.root.context)

            adapter.startListening()
        }
    }

    override fun onResume() {
        super.onResume()

        adapter?.let {
            adapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        adapter?.let {
            adapter.stopListening()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ComprasFragment()
    }
}