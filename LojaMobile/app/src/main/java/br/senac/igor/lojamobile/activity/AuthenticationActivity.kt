package br.senac.igor.lojamobile.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.senac.igor.lojamobile.databinding.ActivityAuthenticationBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AuthenticationActivity : AppCompatActivity() {

    lateinit var b : ActivityAuthenticationBinding
    lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(b.root)

        if( getCurrentUser() == null) {
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

            val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()

            startActivityForResult(intent, 0)
        }
        else {
            setupFirebase()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Autenticado", Toast.LENGTH_SHORT)
                setupFirebase()
                startActivity(Intent(this,MainActivity::class.java))
            } else {
                finishAffinity()
            }
        }
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
}