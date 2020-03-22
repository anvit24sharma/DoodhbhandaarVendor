package com.doodhbhandaarvendor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns

import android.widget.Toast
import com.doodhbhandaarvendor.Constants.Companion.PRODUCTS_TABLE

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_password

class LoginActivity : AppCompatActivity() {

    companion object {
        fun getLaunchIntent(from:Context)=Intent(from,MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        var currentUser :FirebaseUser? =null
        private lateinit var auth: FirebaseAuth
        lateinit var productsDR :DatabaseReference
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso:GoogleSignInOptions
    private val RC_SIGN_IN:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        productsDR = FirebaseDatabase.getInstance().getReference(PRODUCTS_TABLE)

        supportActionBar?.hide()

        txt_SignUp.setOnClickListener {
            startActivity((Intent(this, SignUpActivity::class.java)))
            finish()
        }
        btn_login.setOnClickListener {
            validate()
        }
        txt_forgot_password.setOnClickListener {
            forgotPassword()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        //Google signin button click.
        btn_google_signIn.setOnClickListener {
            signInGoogle()
        }
    }

    private fun forgotPassword() {

        startActivity(Intent(this, ForgotPasswordActivity::class.java))
        finish()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //Google signin.
    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun validate() {
        //email is empty
        if (et_email.text.toString().isEmpty()) {
            et_email.error = "Please enter email"
            et_password.requestFocus()
            return
        }
        //enteremail is not valid
        if (!Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()) {
            et_email.error = "Please enter valid email"
            et_email.requestFocus()
            return
        }
        //if password is empty
        if (et_password.text.toString().isEmpty()) {
            et_password.error = "Please enter password"
            et_password.requestFocus()
            return
        }
        //if no error then login
        auth.signInWithEmailAndPassword(et_email.text.toString(), et_email.text.toString())
            .addOnCompleteListener(this) { task ->
                //if login is successful
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Login Failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        currentUser= auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        //if current user is not null than login successfully
        if (currentUser != null) {
            //if email is verified
            if (currentUser.isEmailVerified) {
                startActivity(getLaunchIntent(this))
                finish()
            }
            else {
                Toast.makeText(baseContext, "Please verify your email.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //the selected google account is retrieved and sen to firebase for authentication
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //user selected account
        if(requestCode==RC_SIGN_IN)
        {
            val task:Task<GoogleSignInAccount> =GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=task.getResult(ApiException::class.java)
                if(account!= null)
                    authWithGoogle(account)
            }
            catch (e:ApiException){
                Toast.makeText(this,"Google sign in failed "+ e.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun authWithGoogle(account: GoogleSignInAccount) {
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)

        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                this.startActivity (getLaunchIntent(this))
            } else {
                Toast.makeText(this,"Google sign in failed" + it.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }


        }

    }





