# RepLocator
# Final Mobile App
# Author #
```
Joel A. Gil
Rockford, IL, USA
Rock Valley College
May 2019
```

# App Name: #

Sales Representative Locator

### Description: ###

A commercial application that will be able to locate the sales representatives of a manufacturing company located in the US, and will allow interested people to visualize the nearest sales representative's contact information through the mobile application. It uses Google Maps for Android devices.

### Target Audience: ###

Purchasing representatives of wholesale and retail companies interested in acquiring the products of this manufacturing company.

### Snippets ###
## How to populate the Locations on the Google Map reading the Firebase database
```
   val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val children = dataSnapshot.children
                children.forEach {
                    var representative: Any? = it.value

                    var fullName : Any? = it.child("fullName").value
                    var email = it.child("email").value
                    var mobile = it.child("mobile").value
                    var address = it.child("address").value
                    var city = it.child("city").value
                    var state = it.child("state").value
                    var zipCode = it.child("zipCode").value

                    //print(" record: $fullName / $email / $address / $city / $state / $zipCode")

                    val coder = Geocoder(context)
                    try {
                        var salesRep =  coder.getFromLocationName("$address, $city, $state $zipCode", 1)
                        if(salesRep != null){
                            repLocation = LatLng(salesRep[0].latitude, salesRep[0].longitude)
                            mMap.addMarker(MarkerOptions().position(repLocation ).title("$fullName / $mobile  " ).snippet("$address, $city, $state $zipCode"))
                        }

                    } catch (ex: Exception ) {

                        ex.printStackTrace();
                    }


                    //Log.d("TAG", "$representative / $representative.address ")
                }

```

## How to update a record in the database ###

```
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
```

## How to include Menu options on an Activity ##

```
 override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.add_rep -> {
                val intent = Intent(this, repLocatorActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.list_rep -> {
                val intent = Intent(this, RepListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.about-> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.sign_off -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

```
