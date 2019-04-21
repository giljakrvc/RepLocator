package edu.rvc.student.relocator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView

class RepLocator3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rep_locator3)

        val txtShowName = findViewById<TextView>(R.id.txtShowName)
        val txtShowMobile = findViewById<TextView>(R.id.txtShowMobile)
        val txtShowEmail = findViewById<TextView>(R.id.txtShowEmail)
        val txtShowAddress = findViewById<TextView>(R.id.txtShowAddress)
        val txtShowAddress2 = findViewById<TextView>(R.id.txtShowAddress2)

        val btnBack = findViewById<Button>(R.id.btnReturn)
        //loads intent string from MainActivity.kt

        txtShowName.text = intent.getStringExtra("FullName")
        txtShowMobile.text = intent.getStringExtra("Mobile")
        txtShowEmail.text = intent.getStringExtra("Email")
        txtShowAddress.text = intent.getStringExtra("Address")
        txtShowAddress2.text = intent.getStringExtra("City") + ", " + intent.getStringExtra("State") + ", " + intent.getStringExtra("ZipCode")

        btnBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        })

        //Fire hidekeyboard when user taps outside any text object
        //Place below code right before last right bracket in function onCreate
        findViewById<View>(android.R.id.content).setOnTouchListener { _, event ->
            hideKeyboard()
            false
        }

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
