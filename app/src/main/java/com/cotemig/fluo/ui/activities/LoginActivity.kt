package com.cotemig.fluo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.cotemig.fluo.BuildConfig
import com.cotemig.fluo.R
import com.cotemig.fluo.app.FluoApp
import com.cotemig.fluo.helper.SharedPreferencesHelper
import com.cotemig.fluo.models.Account
import com.cotemig.fluo.services.RetrofitInitializer

import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            login()
        }

        btnRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        textForgot.setOnClickListener{
            forgot()
        }

        // TODO remover
        if (BuildConfig.DEBUG) {

            email.setText("dirceu@fourtime.com")
            password.setText("senha123")

        }
    }

    fun forgot(){
        var intent = Intent(this, ForgotActivity::class.java)
        intent.putExtra("email", email.text.toString())
        startActivity(intent)
    }

    fun login() {

        var account = Account()
        account.email = email.text.toString()
        account.password = password.text.toString()

        var s = RetrofitInitializer().serviceAccount()
        var call = s.auth(account)

        call.enqueue(object : retrofit2.Callback<Account> {

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {

                response?.let {

                    if (it.code() == 200) {

                        FluoApp.account = it.body()

                        SharedPreferencesHelper.saveString(
                            this@LoginActivity,
                            "account",
                            "userData",
                            account.getJSON().toString()
                        )

                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)


                        Toast.makeText(this@LoginActivity, "Tudo certo", Toast.LENGTH_LONG).show()

                    } else {

                        MaterialDialog.Builder(this@LoginActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.ops)
                            .content(R.string.invalid_user_and_pass)
                            .positiveText(R.string.ok)
                            .show()

                    }

                }

//                response.let {
//                    if(it!!.code() == 200){
//                        // Chamar outra intent
//                        Toast.makeText(this@LoginActivity,"Tudo certo", Toast.LENGTH_LONG).show()
//                    }else{
//                        Toast.makeText(this@LoginActivity,"Errou", Toast.LENGTH_LONG).show()
//                    }
//                }

            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {
                Toast.makeText(this@LoginActivity, "Errou", Toast.LENGTH_LONG).show()
//                MaterialDialog.Builder(this@LoginActivity)
//                    .theme(Theme.LIGHT)
//                    .title(R.string.ops)
//                    .content(R.string.invalid_user_and_pass)
//                    .positiveText(R.string.ok)
//                    .show()
            }

        })


    }

}
