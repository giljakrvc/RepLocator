package edu.rvc.student.relocator

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RepListActivity : AppCompatActivity() {

    var repList = ArrayList<Representative>()
    var repNameList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rep_list)

        val context = this
        var ref = FirebaseDatabase.getInstance().getReference("RepLocator")
        val lv = findViewById<ListView>(R.id.lstRepresentatives)

        val intent = Intent(this, EditRepLocatorActivity::class.java)


        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val children = dataSnapshot.children
                children.forEach {

                    var fullName  = it.child("fullName").value.toString()
                    var email = it.child("email").value.toString()
                    var mobile = it.child("mobile").value.toString()
                    var address = it.child("address").value.toString()
                    var city = it.child("city").value.toString()
                    var state = it.child("state").value.toString()
                    var zipCode = it.child("zipCode").value.toString()
                    var id = it.key

                    //print(" Listing records: $id $fullName / $email / $address / $city / $state / $zipCode")
                    var rep = Representative ( id, fullName, mobile, email, address, city, state, zipCode)
                    repList.add(rep)
                    repNameList.add(fullName)

                    Log.e("TAG", " Listing records: $id $fullName / $email / $address / $city / $state / $zipCode")
                }

                val myListAdapter = RepListAdapter(context,repList)
                lv.adapter = myListAdapter

                lv.setOnItemClickListener(){adapterView, view, position, id ->
                    val itemAtPos  = adapterView.getItemAtPosition(position) as Representative
                    val itemIdAtPos = adapterView.getItemIdAtPosition(position)

                    Log.e("TAG", " Listing records: $id ${itemAtPos.FullName} $itemIdAtPos")

                    Toast.makeText(context, "Click on item at ${itemAtPos.FullName} / ${itemAtPos.Email} - Position $itemIdAtPos ", Toast.LENGTH_LONG).show()


                    //putExtra sets value to name SendStuff (Could be called whatever you want
                    intent.putExtra("id",  itemAtPos.id)
                    //Go to second activity
                    startActivity(intent)
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        ref.addListenerForSingleValueEvent(eventListener)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.home -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
