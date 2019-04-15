package edu.rvc.student.relocator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText

class repLocatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rep_locator)

        val btnBack = findViewById<Button>(R.id.btnCancel)
        val btngo = findViewById<Button>(R.id.btnNext)

        val txtName = findViewById<EditText>(R.id.txtRepName)
        val txtMobile = findViewById<EditText>(R.id.txtPhone)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)

        btngo.setOnClickListener(View.OnClickListener {
            //Intent is used to send data between activities
            val intent = Intent(this, RepLocator2Activity::class.java)

            //putExtra sets value to name SendStuff (Could be called whatever you want
            intent.putExtra("FullName",  txtName.text.toString())
            intent.putExtra("Mobile", txtMobile.text.toString())
            intent.putExtra("Email", txtEmail.text.toString())

            //Go to second activity
            startActivity(intent)

        })

        //Fire hidekeyboard when user taps outside any text object
        //Place below code right before last right bracket in function onCreate
        findViewById<View>(android.R.id.content).setOnTouchListener { _, event ->
            hideKeyboard()
            false
        }

        btnBack.setOnClickListener(View.OnClickListener {
            this.finish()
        })


    }

    fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }
}
