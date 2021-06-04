package br.senac.igor.lojamobile.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.databinding.CompraCardBinding
import br.senac.igor.lojamobile.databinding.GameCardBinding
import br.senac.igor.lojamobile.model.Compra
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso

class CompraRecyvlerViewAdapter(options: FirebaseRecyclerOptions<Compra>): FirebaseRecyclerAdapter<Compra, CompraRecyvlerViewAdapter.CompraViewHolder>(options) {

    class CompraViewHolder(private val binding : CompraCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun  bind(compra : Compra) {
            binding.dataCompra.text = compra.dateOfPurchase.toString()

            compra.games?.forEach {
                val cardBinding = GameCardBinding.inflate(LayoutInflater.from(binding.root.context))
                cardBinding.GameName.text = it.produto.nome
                cardBinding.GamePrice.text = "R$ " + (it.quantidade * (it.produto.preco - it.produto.preco * it.produto.desconto/100)).toString()
                cardBinding.GamePrice2.text = "Quantidade: " + it.quantidade.toString() + " -"

                binding.container.addView(cardBinding.root)

                Picasso.get()
                        .load("https://i.postimg.cc/"+it.produto.link+"/"+it.produto.id+".jpg")
                        .placeholder(R.drawable.hl)
                        .error(R.drawable.hl)
                        .into(cardBinding.imageView2)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompraViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CompraCardBinding.inflate(layoutInflater, parent, false)

        return CompraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompraViewHolder, position: Int, model: Compra) {
        Log.e("AAAAAAAAAA", model.toString())
        holder.bind(model)
    }

}