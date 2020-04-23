package com.doodhbhandaarvendor.auth

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.util.Patterns.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.ui.MainActivity
import com.doodhbhandaarvendor.utils.Constants
import com.doodhbhandaarvendor.utils.Constants.Companion.NAME
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_email
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    companion object {
        fun getLaunchIntent(from: Context) = Intent(
            from,
            MainActivity::class.java
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        var currentUser: FirebaseUser? = null
        private lateinit var auth: FirebaseAuth
        lateinit var usersDR: DatabaseReference
        lateinit var prefs: SharedPreferences
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    private val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        prefs = getSharedPreferences("Db Vendor", Context.MODE_PRIVATE)
        usersDR = FirebaseDatabase.getInstance().getReference("Users")
        supportActionBar?.hide()

        txt_SignUp.setOnClickListener {
            startActivity((Intent(this, SignUpActivity::class.java)))
        }
        btn_login.setOnClickListener {
            validate()
        }
        text_forgot_password.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password?")

            val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
            val useremail = view.findViewById<EditText>(R.id.et_userEmail)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which->
                forgotPassword(useremail)
            })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener { dialog, which->
                dialog.dismiss()
            })
             val dialog: AlertDialog = builder.create()
            dialog.show()


        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_google_signIn.setOnClickListener {
            signInGoogle()
        }


    }

    private fun forgotPassword(useremail: EditText) {
        if (useremail.text.toString().isEmpty()) {
            useremail.error = "Please enter email"
            useremail.requestFocus()
            return
        }
        if (!EMAIL_ADDRESS.matcher(useremail.text.toString()).matches()) {
            useremail.error = "Please enter valid email"
            useremail.requestFocus()
            return
        }
        auth.sendPasswordResetEmail(useremail.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
            }
        }
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
        if (!EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()) {
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

        auth.signInWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
            .addOnCompleteListener(this) { task ->
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
        currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        //if current user is not null than login successfully
        if (currentUser != null) {
            //if email is verified
            if (prefs.getString(NAME, "") == "") {
                usersDR.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(datasnapShot: DataSnapshot) {
                        if (datasnapShot.child(currentUser.uid).exists()) {
                            val editor = prefs.edit()
                            editor.putString(
                                NAME,
                                datasnapShot.child(currentUser.uid).child("name").getValue().toString()
                            )
                            editor.putString(
                                Constants.PHONE_NUMBER,
                                datasnapShot.child(currentUser.uid).child("phone_number").getValue().toString()
                            )
                            editor.putString(
                                Constants.EMAIL,
                                datasnapShot.child(currentUser.uid).child("email").getValue().toString()
                            )
                            editor.putString(
                                Constants.ADDRESS,
                                datasnapShot.child(currentUser.uid).child("address").getValue().toString()
                            )
                            editor.putString(Constants.USER_ID, currentUser.uid)
                            editor.apply()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        } else {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    UserDetailActivity::class.java
                                )
                            )
                            finish()
                        }
                    }

                })
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }


    //the selected google account is retrieved and sen to firebase for authentication
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //user selected account
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null)
                    authWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed " + e.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun authWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                usersDR.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(datasnapShot: DataSnapshot) {
                        if (datasnapShot.child(user?.uid!!).exists()) {
                            val editor = prefs.edit()
                            editor.putString(
                                NAME,
                                datasnapShot.child(user.uid).child("name").getValue().toString()
                            )
                            editor.putString(
                                Constants.PHONE_NUMBER,
                                datasnapShot.child(user.uid).child("phone_number").getValue().toString()
                            )
                            editor.putString(
                                Constants.EMAIL,
                                datasnapShot.child(user.uid).child("email").getValue().toString()
                            )
                            editor.putString(
                                Constants.ADDRESS,
                                datasnapShot.child(user.uid).child("address").getValue().toString()
                            )
                            editor.putString(Constants.USER_ID, user.uid)
                            editor.apply()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        } else {
                            //start user Details Activity
                            startActivity(
                                Intent(
                                    applicationContext,
                                    UserDetailActivity::class.java
                                )
                            )
                            finish()
                        }
                    }

                })

            } else {
                Toast.makeText(
                    this,
                    "Google sign in failed" + it.exception.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

}





