package br.senac.igor.lojamobile.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import br.senac.igor.lojamobile.R
import br.senac.igor.lojamobile.databinding.ActivityMainBinding
import br.senac.igor.lojamobile.fragment.CartFragment
import br.senac.igor.lojamobile.fragment.CatalogoFragment
import br.senac.igor.lojamobile.fragment.ComprasFragment
import br.senac.igor.lojamobile.fragment.SobreFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        setupFirebase()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val email = getUserEmail()
        val headerView = b.navigationView.getHeaderView(0)
        val textEmail = headerView.findViewById<TextView>(R.id.textHeaderEmail)
        textEmail.text = email
        //Gerencia Toggle
        toggle = ActionBarDrawerToggle(
            this,
            b.drawerLayout,
            R.string.AbrirMenu,
            R.string.FecharMenu
        )
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Define o frag default
        val frag = CatalogoFragment.newInstance(null)
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, frag,"CatalogoFragment").commit()
        b.navigationView.setCheckedItem(R.id.catalogo)
        //Troca Fragmentos
        b.navigationView.setNavigationItemSelectedListener {
            b.drawerLayout.closeDrawers()

            when (it.itemId) {
                R.id.catalogo -> {
                    if(b.navigationView.checkedItem?.itemId != R.id.catalogo) {
                        val frag = CatalogoFragment.newInstance(null)
                        supportFragmentManager
                            .beginTransaction()
                            .replace(b.fragContainer.id, frag, "CatalogoFragment")
                            .commit()
                    }
                    true
                }
                R.id.sobre -> {
                    val frag = SobreFragment()

                    supportFragmentManager
                        .beginTransaction()
                        .replace(b.fragContainer.id, frag)
                        .commit()

                    true
                }
                R.id.cartMenuItem -> {
                    val frag = CartFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(b.fragContainer.id, frag)
                        .commit()

                    true
                }
                R.id.compra -> {
                    val frag = ComprasFragment()
                    supportFragmentManager
                            .beginTransaction()
                            .replace(b.fragContainer.id, frag)
                            .commit()

                    true
                }

                else -> false
            }

        }
    }

    fun getUserEmail() : String? {
        val user = getCurrentUser()
        user?.let {
            return user.email
        }
        return null
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {

                    query?.let {
//                        val frag = CatalogoFragment.newInstance(query)
//                        supportFragmentManager
//                            .beginTransaction()
//                            .replace(b.fragContainer.id, frag)
//                            .commit()
                        val myFragment: CatalogoFragment? =
                            supportFragmentManager.findFragmentByTag("CatalogoFragment") as CatalogoFragment?
                        if (myFragment != null && myFragment.isVisible()) {
                            myFragment.receberPesquisa(query)
                        }
                        else{
                            val frag = CatalogoFragment.newInstance(query)
                            supportFragmentManager
                                    .beginTransaction()
                                    .replace(b.fragContainer.id, frag, "CatalogoFragment")
                                    .commit()
                        }

                    } ?: run {
                        val myFragment: CatalogoFragment? =
                            supportFragmentManager.findFragmentByTag("CatalogoFragment") as CatalogoFragment?
                        if (myFragment != null && myFragment.isVisible()) {
                            myFragment.fecharPesquisa()
                        }
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    newText?.let {
                        val myFragment: CatalogoFragment? =
                            supportFragmentManager.findFragmentByTag("CatalogoFragment") as CatalogoFragment?
                        if (myFragment != null && myFragment.isVisible()) {
                            myFragment.receberPesquisa(newText)
                        }
                    } ?: run {
                        val myFragment: CatalogoFragment? =
                            supportFragmentManager.findFragmentByTag("CatalogoFragment") as CatalogoFragment?
                        if (myFragment != null && myFragment.isVisible()) {
                            myFragment.fecharPesquisa()
                        }
                    }

                    return true
                }

            })
            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {

                    val frag = CatalogoFragment.newInstance(null)
                    supportFragmentManager
                        .beginTransaction()
                        .replace(b.fragContainer.id, frag)
                        .commit()

                    return true
                }

            })

//            searchView.setOnSearchClickListener {
//                searchView.requestFocusFromTouch()
//            }
//            searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
//                if (hasFocus) {
//                    showInputMethod(v)
//                }
//            }

        }
        return true
    }

//    fun showInputMethod(v : View) {
//        val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        if(input != null) {
//            input.showSoftInput(v, 0)
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }

        return super.onOptionsItemSelected(item)
    }
}