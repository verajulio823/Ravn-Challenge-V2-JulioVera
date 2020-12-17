package com.ravn.codechallenge

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.ravn.codechallenge.models.PeopleDetail

class PersonDetailActivity : AppCompatActivity() {

    lateinit var people: PeopleDetail
    val HEADER_INFORMATION ="General Information"
    val HEADER_VEHICLES="Vehicles"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        //Sacamos la información que pasamos del adapter
        people= intent.getSerializableExtra("people") as PeopleDetail

        initActionBar()
        init()



    }

    private fun init(){
        // Inicializamos view
        val linearContent = findViewById<LinearLayout>(R.id.linearContent)
        val viewHeaderGeneral =layoutInflater.inflate(R.layout.item_detail_info, null)
        val viewHeaderVehicles =layoutInflater.inflate(R.layout.item_detail_info, null)

        viewHeaderGeneral.findViewById<TextView>(R.id.txtHeader).text= HEADER_INFORMATION
        viewHeaderVehicles.findViewById<TextView>(R.id.txtHeader).text= HEADER_VEHICLES

        // Llenamos datos a la vista con datos generales
        linearContent.addView(viewHeaderGeneral)
        linearContent.addView(createDetail("Eye Color", people.eyeColor))
        linearContent.addView(createDetail("Hair Color", people.hairColor))
        linearContent.addView(createDetail("Skin Color", people.skinColor))
        linearContent.addView(createDetail("Birth Year", people.birthYear))

        // Llenamos datos a la vista de vehículos
        linearContent.addView(viewHeaderVehicles)

        for(vehicles: String in people.vehicles!!){
            linearContent.addView(createDetail(vehicles, ""))
        }

    }

    private fun createDetail(detail: String, value: String): View? {

        // Llenamos datos a vehículos
        val viewItemDetail = layoutInflater.inflate(R.layout.item_detail_info_child, null)
        viewItemDetail.findViewById<TextView>(R.id.txtDetail).text=detail
        viewItemDetail.findViewById<TextView>(R.id.txtValue).text=value

        return viewItemDetail
    }

    private fun initActionBar(){

        // Agregamos propiedades al ActionBar
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.title_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
