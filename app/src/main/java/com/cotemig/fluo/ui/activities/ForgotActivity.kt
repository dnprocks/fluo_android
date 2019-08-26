package com.cotemig.fluo.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.cotemig.fluo.R
import com.cotemig.fluo.models.Account
import com.cotemig.fluo.services.RetrofitInitializer
import kotlinx.android.synthetic.main.activity_forgot.*
import retrofit2.Call
import retrofit2.Response

class ForgotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        email.setText(intent.getStringExtra("email"))

        btnForgot.setOnClickListener{
            forgot()
        }
    }

    fun forgot(){
        var account = Account()
        account.email = email.text.toString()

        var s = RetrofitInitializer().serviceAccount()
        var call = s.forgetPassword(account)

        call.enqueue(object: retrofit2.Callback<Void>{

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {

                response?.let {

                    if(it.code() == 204){

                        MaterialDialog.Builder(this@ForgotActivity)
                            .theme(Theme.LIGHT)
                            .title(R.string.success)
                            .content(R.string.forgot_message)
                            .positiveText(R.string.ok)
                            .onPositive{ dialog, which ->
                                finish()
                            }
                            .show()

                    }
                }

            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}
