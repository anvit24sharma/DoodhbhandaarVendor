package com.doodhbhandaarvendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.auth.LoginActivity
import com.doodhbhandaarvendor.auth.LoginActivity.Companion.prefs
import com.doodhbhandaarvendor.auth.UserDetailActivity
import com.doodhbhandaarvendor.utils.Constants.Companion.ADDRESS
import com.doodhbhandaarvendor.utils.Constants.Companion.EMAIL
import com.doodhbhandaarvendor.utils.Constants.Companion.NAME
import com.doodhbhandaarvendor.utils.Constants.Companion.PHONE_NUMBER
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvUserName.text = prefs.getString(NAME,"")
        tvMobile.text = prefs.getString(PHONE_NUMBER,"")
        tv_email.text = prefs.getString(EMAIL,"")
        tvAddress.text = prefs.getString(ADDRESS,"")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivEdit.setOnClickListener {
            val intent = Intent(context,UserDetailActivity::class.java)
            intent.putExtra("from","ProfileFragment")
            startActivity(intent)
        }

        txt_logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.finish()
            startActivity(Intent(context,LoginActivity::class.java))
            prefs.edit().clear().apply()

        }
    }

}
