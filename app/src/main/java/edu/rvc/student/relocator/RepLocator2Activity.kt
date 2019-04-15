package edu.rvc.student.relocator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class RepLocator2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rep_locator2)

        val txtAddress = findViewById<TextView>(R.id.txtAddress)
        val txtCity = findViewById<TextView>(R.id.txtCity)
        val txtState = findViewById<TextView>(R.id.txtState)
        val txtZipCode = findViewById<TextView>(R.id.txtZipCode)
        val txtshow = findViewById<TextView>(R.id.txtViewName)

        val btnBack = findViewById<Button>(R.id.btnCancel2)

        //loads intent string from MainActivity.kt
        var strShow: String = intent.getStringExtra("FullName")
        val strName : String = intent.getStringExtra("FullName")
        val strMobile : String = intent.getStringExtra("Mobile")
        val strEmail : String = intent.getStringExtra("Email")

        var ref = FirebaseDatabase.getInstance().getReference("RepLocator")

        txtshow.text = strShow + " - " + strMobile + " - " + strEmail

        val btngo = findViewById<Button>(R.id.btnSave)

        btngo.setOnClickListener(View.OnClickListener {
            //Intent is used to send data between activities
            val intent = Intent(this, RepLocator3Activity::class.java)

            //putExtra sets value to name SendStuff (Could be called whatever you want
            intent.putExtra("FullName",  strName)
            intent.putExtra("Mobile", strMobile)
            intent.putExtra("Email", strEmail)
            intent.putExtra("Address", txtAddress.text.toString())
            intent.putExtra("City",txtCity.text.toString())
            intent.putExtra("State",txtState.text.toString())
            intent.putExtra("ZipCode",txtZipCode.text.toString())

            var messageid = ref.push().key
            var rep = Representative(messageid.toString(), strName, strMobile, strEmail, txtAddress.text.toString(), txtCity.text.toString(), txtState.text.toString(), txtZipCode.text.toString()  )

            ref.child(messageid.toString()).setValue(rep).addOnCompleteListener {
                Toast.makeText(this, "Representative Added!",Toast.LENGTH_SHORT).show()
            }

            //Go to second activity
            startActivity(intent)

        })

        btnBack.setOnClickListener(View.OnClickListener {
            this.finish()
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
