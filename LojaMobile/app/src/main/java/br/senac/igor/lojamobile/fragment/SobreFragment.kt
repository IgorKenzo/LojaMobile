package br.senac.igor.lojamobile.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.databinding.FragmentSobreBinding

class SobreFragment : Fragment() {

    lateinit var b : FragmentSobreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentSobreBinding.inflate(inflater)

        b.imgEmailIgor.setOnClickListener {

            var email = arrayOf<String>("igor.kenzo.m@gmail.com")

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, "Loja de Jogos")
                putExtra(Intent.EXTRA_TEXT, "Oi Igor")
            }
            startActivity(intent)
        }

        b.imgEmailClaro.setOnClickListener {

            var email = arrayOf<String>("lucasbrclaro@gmail.com")

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, "Loja de Jogos")
                putExtra(Intent.EXTRA_TEXT, "Oi Lucas")
            }
            startActivity(intent)
        }

        b.imgInfoSenac.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sp.senac.br/"))
            startActivity(intent)
        }

        return b.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SobreFragment()
    }
}