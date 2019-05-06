package edu.rvc.student.relocator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener




class EditRepLocatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_rep_locator)

        val btnBack = findViewById<Button>(R.id.btnCancel)
        val btngo = findViewById<Button>(R.id.btnSave)

        val txtName = findViewById<EditText>(R.id.txtRepName)
        val txtMobile = findViewById<EditText>(R.id.txtPhone)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)

        val txtAddress = findViewById<EditText>(R.id.txtAddress)
        val txtCity = findViewById<EditText>(R.id.txtCity)
        val txtState = findViewById<EditText>(R.id.txtState)
        val txtZipCode = findViewById<EditText>(R.id.txtZipCode)

        var strId: String = intent.getStringExtra("id")

        var ref = FirebaseDatabase.getInstance().getReference("RepLocator")

        ref.orderByChild("id").equalTo(strId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (it in dataSnapshot.children) {

                        txtName.setText(it.child("fullName").value.toString())
                        txtEmail.setText(it.child("email").value.toString())
                        txtMobile.setText(it.child("mobile").value.toString())
                        txtAddress.setText(it.child("address").value.toString())
                        txtCity.setText(it.child("city").value.toString())
                        txtState.setText(it.child("state").value.toString())
                        txtZipCode.setText(it.child("zipCode").value.toString())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        btngo.setOnClickListener(View.OnClickListener {
            //Intent is used to send data between activities
            val intent = Intent(this, RepListActivity::class.java)

            val updates = HashMap<String, Any>()

            updates["fullName"] = txtName.text.toString()
            updates["mobile"] = txtMobile.text.toString()
            updates["email"] = txtEmail.text.toString()
            updates["address"] = txtAddress.text.toString()
            updates["city"] = txtCity.text.toString()
            updates["state"] = txtState.text.toString()
            updates["zipCode"] = txtZipCode.text.toString()
            updates["id"] = strId

            var rep = Representative( strId, txtName.text.toString(), txtMobile.text.toString(), txtEmail.text.toString(), txtAddress.text.toString(), txtCity.text.toString(), txtState.text.toString(), txtZipCode.text.toString()  )

            ref.child(strId).updateChildren(updates).addOnCompleteListener {
                Toast.makeText(this, "Representative Updated!", Toast.LENGTH_SHORT).show()
            }

            //Go to second activity
            startActivity(intent)

        })

        btnBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RepListActivity::class.java)
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
