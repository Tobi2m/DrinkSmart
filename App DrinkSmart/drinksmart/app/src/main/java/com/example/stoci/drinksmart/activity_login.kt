package com.example.stoci.drinksmart

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.stoci.drinksmart.Retrofit.INodeJS
import com.example.stoci.drinksmart.Retrofit.RetrofitClient
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.rengwuxian.materialedittext.MaterialEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class activity_login : AppCompatActivity() {

    lateinit var  myAPI:INodeJS
    var compositeDisposable=CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Init API
        val retrofit=RetrofitClient.instance
        myAPI=retrofit.create(INodeJS::class.java)

        login_button.setOnClickListener{
            login(edt_email.text.toString(),edt_password.text.toString())
<<<<<<< HEAD
=======
            Handler().postDelayed({
                startActivity(Intent(this@activity_login,LoadingActivity::class.java))
                finish()
            }, 2000)
>>>>>>> 1bf9eb24fc69419d3f16ac1848e4cbe2d6a01544
        }

        register_button.setOnClickListener{
            register(edt_email.text.toString(),edt_password.text.toString())
        }
    }

    private fun register(email: String, password: String) {

        val enter_name_view= LayoutInflater.from(this@activity_login)
                .inflate(R.layout.activity_enter_name,null)

        MaterialStyledDialog.Builder(this@activity_login)
                .setTitle("Register")
                .setDescription("One more step!")
                .setCustomView(enter_name_view)
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
                .onNegative{dialog,_ -> dialog.dismiss()}
                .setPositiveText("Register")
                .onPositive{ _,_->

                    val edt_name=enter_name_view.findViewById<View>(R.id.edt_name) as MaterialEditText

                    compositeDisposable.add(myAPI.registerUser(email,edt_name.text.toString(),password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe{message ->
                                    Toast.makeText(this@activity_login,message,Toast.LENGTH_SHORT).show()
                            })
                }.show()

    }

    private fun login(email: String,password:String){
        compositeDisposable.add(myAPI.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{message ->
                    /*val builder = AlertDialog.Builder(this)
                    builder.setTitle("Message")
                    builder.setMessage(message)
                    builder.show()*/
                    if(message.contains("encrypted_password")) {
                        Toast.makeText(this@activity_login, "Login success", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            startActivity(Intent(this@activity_login, LoadingActivity::class.java))
                            finish()
                        }, 2000)
                    }
                    else {
                        Toast.makeText(this@activity_login, message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
