package com.cotemig.fluo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.cotemig.fluo.R
import com.cotemig.fluo.app.FluoApp
import com.cotemig.fluo.helper.SharedPreferencesHelper
import com.cotemig.fluo.models.Account
import com.cotemig.fluo.services.RetrofitInitializer
import retrofit2.Call
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activity)

        Handler().postDelayed({
            auth()
        }, 2000)
    }

    fun auth() {

        var json = SharedPreferencesHelper.readString(this, "account", "userData")

        if (json == null) {

            goToLogin()

        } else {

            var account = Account()
            account.parseJSON(json)
            login(account)

        }

    }

    fun login(account: Account) {

        var s = RetrofitInitializer().serviceAccount()
        var call = s.auth(account)

        call.enqueue(object : retrofit2.Callback<Account> {

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {

                response?.let {

                    if (it.code() == 200) {

                        FluoApp.account = it.body()
                        goToMain()

                    } else {

                        goToLogin()

                    }

                }

            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {
                Toast.makeText(this@SplashActivity, "Errou", Toast.LENGTH_LONG).show()
            }

        })

    }

    fun goToMain() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToLogin() {
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
