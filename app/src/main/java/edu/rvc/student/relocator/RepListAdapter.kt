package edu.rvc.student.relocator

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class RepListAdapter(private val context: Activity, private val reps: ArrayList<Representative>)
    : ArrayAdapter<Representative>(context, R.layout.custom_list, reps) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val imageView = rowView.findViewById(R.id.icon) as ImageView
        val emailText = rowView.findViewById(R.id.email) as TextView
        val subtitleText = rowView.findViewById(R.id.description) as TextView

        var rep = reps.get(position)
        titleText.text = rep.FullName
        emailText.text = "Mobile " + rep.Mobile + " Email: " + rep.Email
        imageView.setImageResource(R.drawable.pin_map)
        subtitleText.text = rep.Address + ", " + rep.City + ", " + rep.State + ", " + rep.ZipCode

        return rowView
    }

}