package br.senac.igor.lojamobile.fragment

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.activity.DetalheActivity
import br.senac.igor.lojamobile.databinding.FragmentCatalogoBinding
import br.senac.igor.lojamobile.databinding.GameCardBinding
import br.senac.igor.lojamobile.model.Produto
import br.senac.igor.lojamobile.services.ProdutoService
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CatalogoFragment : Fragment() {

    lateinit var b : FragmentCatalogoBinding
    var filtroPesquisa : String? = null
    var filtrosCategorias = arrayListOf<String>()
    var produtos = arrayListOf<Produto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filtroPesquisa = getArguments()?.getString("Pesquisa");
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        b = FragmentCatalogoBinding.inflate(inflater)

        var categorias = arrayListOf("MetroidVania", "Plataforma", "Puzzle", "Shooter", "Rogue Like")
        categorias.forEach {
            val chip = Chip(this.context)

            when (it) {
                "MetroidVania" -> chip.setText(R.string.ChipMetroidVania)
                "Plataforma" -> chip.setText(R.string.ChipPlataforma)
                "Puzzle" -> chip.setText(R.string.ChipPuzzle)
                "Shooter" -> chip.setText(R.string.ChipShooter)
                "Rogue Like" -> chip.setText(R.string.ChipRogueLike)
            }
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    filtrosCategorias.add(it)
                }
                else {
                    filtrosCategorias.remove(it)
                }
                atualizarUI()

            }

            b.chipGroup.addView(chip)
        }


        return b.root
    }

    override fun onResume() {
        super.onResume()

        atualizarProdutos()
    }

    fun atualizarUI() {

        b.container.removeAllViews()

        produtos?.forEach {

            //filtro categoria
            if (filtrosCategorias.count() <= 0 || filtrosCategorias.contains(it.categoria)) {
                //filtro pesquisa nome
                if (filtroPesquisa != null) {
                    if (it.nome.contains(filtroPesquisa!!, true)) {

                        val cardBinding = GameCardBinding.inflate(layoutInflater)

                        cardBinding.GameName.text = it.nome
                        cardBinding.GamePrice.text = "R$ " + it.preco.toString()
                        if (it.desconto > 0) {
                            var txt = SpannableString("R$ " + it.preco.toString())
                            txt.setSpan(StrikethroughSpan(),0, txt.length,0)
                            cardBinding.GamePrice2.text = txt
                            cardBinding.GamePrice.text = "-" + it.desconto + "% | RS " + (it.preco - it.preco * it.desconto/100).toString()
                        }

                        val produto = it

                        cardBinding.root.setOnClickListener {

                            //val frag = DetalheFragment.newInstance(produto)

                            //parentFragmentManager
                                //.beginTransaction()
                                //.replace(R.id.fragContainer, frag)
                                //.commit()

                            var intent = Intent(it.context, DetalheActivity::class.java)
                            intent.putExtra("Produto", produto)
                            startActivity(intent)
                        }

                        b.container.addView(cardBinding.root)

                        Picasso.get()
                                .load("https://i.postimg.cc/"+it.link+"/"+it.id+".jpg")
                                .placeholder(R.drawable.hl)
                                .error(R.drawable.hl)
                                .into(cardBinding.imageView2)

                    }
                }
                else {
                    val cardBinding = GameCardBinding.inflate(layoutInflater)

                    cardBinding.GameName.text = it.nome
                    cardBinding.GamePrice.text = "R$ " + it.preco.toString()
                    if (it.desconto > 0) {
                        var txt = SpannableString("R$ " + it.preco.toString())
                        txt.setSpan(StrikethroughSpan(),0, txt.length,0)
                        cardBinding.GamePrice2.text = txt
                        cardBinding.GamePrice.text = "-" + it.desconto + "% | RS " + (it.preco - it.preco * it.desconto/100).toString()
                    }

                    val produto = it

                    cardBinding.root.setOnClickListener {

                        var intent = Intent(it.context, DetalheActivity::class.java)
                        intent.putExtra("Produto", produto)
                        startActivity(intent)

//                        val frag = DetalheFragment.newInstance(produto)
//
//                        parentFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.fragContainer, frag)
//                            .commit()
                    }

                    Picasso.get()
                            .load("https://i.postimg.cc/"+it.link+"/"+it.id+".jpg")
                            .placeholder(R.drawable.hl)
                            .error(R.drawable.delete)
                            .into(cardBinding.imageView2)

                    b.container.addView(cardBinding.root)
                }
            }

        }
    }

    fun atualizarProdutos() {

        val http = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://murmuring-wave-61983.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()

        val service = retrofit.create(ProdutoService::class.java)

        val call = service.list()

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                b.progressBar.visibility = View.INVISIBLE

                if (response.isSuccessful) {
                    produtos = response.body() as ArrayList<Produto>
                    atualizarUI()
                }
                else {
                    Snackbar
                        .make(b.container, R.string.CallbackErrorResponse, Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("ERRO", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                b.progressBar.visibility = View.INVISIBLE
                Snackbar
                    .make(b.container, R.string.CallbackErrorConection, Snackbar.LENGTH_LONG)
                    .show()

                Log.e("ERRO", "Falha ao chamar o serviço", t)
            }
        }

        b.progressBar.visibility = View.VISIBLE
        call.enqueue(callback)
    }

    fun receberPesquisa(texto : String) {
        filtroPesquisa = texto
        atualizarUI()
    }

    fun fecharPesquisa() {
        filtroPesquisa = null
        atualizarUI()
    }

    companion object {
        @JvmStatic
//        fun newInstance() = CatalogoFragment()

        fun newInstance(TextoPesquisa: String?): CatalogoFragment {
            val myFragment = CatalogoFragment()
            val args = Bundle()
            args.putString("Pesquisa", TextoPesquisa)
            myFragment.setArguments(args)
            return myFragment
        }

    }


}