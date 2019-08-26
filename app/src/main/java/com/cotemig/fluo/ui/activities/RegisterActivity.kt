package com.cotemig.fluo.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.cotemig.fluo.R
import com.cotemig.fluo.app.FluoApp
import com.cotemig.fluo.helper.SharedPreferencesHelper
import com.cotemig.fluo.models.Account
import com.cotemig.fluo.services.RetrofitInitializer
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btnRegister
import kotlinx.android.synthetic.main.activity_register.email
import kotlinx.android.synthetic.main.activity_register.password

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            register()
        }

    }

    fun register() {

        var account = Account()
        account.name = name.text.toString()
        account.email = email.text.toString()
        account.password = password.text.toString()

        var s = RetrofitInitializer().serviceAccount()
        var call = s.register(account)

        call.enqueue(object : retrofit2.Callback<Account>{

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {
                response.let {

                    if(it!!.code() == 200){
                        // Chamar outra intent
//                        Toast.makeText(this@RegisterActivity,"Cadastro ok", Toast.LENGTH_LONG).show()

                        MaterialDialog.Builder(this@RegisterActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.ops)
                            .content(R.string.register_success)
                            .positiveText(R.string.ok)
                            .onPositive{ dialog, which ->

                                var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)


                            }
                            .show()

                    }else{
                        MaterialDialog.Builder(this@RegisterActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.ops)
                            .content(R.string.register_duplicate)
                            .positiveText(R.string.ok)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {
                Toast.makeText(this@RegisterActivity, "Errou", Toast.LENGTH_LONG).show()
            }


        })

    }
}


