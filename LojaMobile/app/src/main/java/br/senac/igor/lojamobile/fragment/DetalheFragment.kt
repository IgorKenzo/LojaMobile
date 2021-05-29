package br.senac.igor.lojamobile.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.databinding.FragmentDetalheBinding
import br.senac.igor.lojamobile.model.Descricao
import br.senac.igor.lojamobile.model.Produto
import br.senac.igor.lojamobile.services.ProdutoService
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


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

        buscarDescricao()

        montarUI()

        //TODO configurar botão carrinho

        return b.root
    }

    fun montarUI() {
        Log.e("Produto Vazio?", produto.toString())

        b.txtNome.setText(produto.nome)
        //TODO colocar imagem
        b.chipCategoria.setText(produto.categoria)
        b.txtPreco.setText("R$ " + produto.preco.toString())
        //TODO colocar desconto
    }

    fun colocarDescricao() {
        b.txtDescricao.setText(desricao)
    }

    fun buscarDescricao() {
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

        val call = service.descricao(produto.id)

        var callback = object : Callback<Descricao> {
            override fun onResponse(call: Call<Descricao>, response: Response<Descricao>) {
                if (response.isSuccessful) {
                    desricao = response.body()!!.descricao
                    colocarDescricao()
                }
                else {
                    Snackbar
                        .make(b.txtDescricao, R.string.CallbackErrorResponse, Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("ERRO", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Descricao>, t: Throwable) {
                Snackbar
                    .make(b.txtDescricao, R.string.CallbackErrorConection, Snackbar.LENGTH_LONG)
                    .show()

                Log.e("ERRO", "Falha ao chamar o serviço", t)
            }

        }

        call.enqueue(callback)
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