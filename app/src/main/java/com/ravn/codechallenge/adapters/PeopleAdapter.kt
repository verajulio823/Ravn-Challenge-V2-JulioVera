package com.ravn.codechallenge.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ravn.codechallenge.ListPeoplesQuery
import com.ravn.codechallenge.PersonDetailActivity
import com.ravn.codechallenge.R
import com.ravn.codechallenge.models.*

//Creamos un adapter con las Clases de Apollo Graphql
class PeopleAdapter(val listPeoples: List<ListPeoplesQuery.Person?>?): RecyclerView.Adapter<PeopleAdapter.PeopleHolder>(){


    // Indicamos cual será el layout para cargar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return PeopleHolder(layoutInflater.inflate(R.layout.item_list_people,parent,false))
    }

    override fun onBindViewHolder(holder: PeopleHolder, position: Int) {
        holder.render(listPeoples?.get(position))
    }

    override fun getItemCount(): Int {
        if (listPeoples != null) {
            return listPeoples.size
        }
        return 0
    }

    class PeopleHolder(val view: View): RecyclerView.ViewHolder(view){
        //Función para cargar datos al layout
        fun render(people: ListPeoplesQuery.Person?){

            val txtNamePeople = view.findViewById<TextView>(R.id.txtNamePeople)
            val txtTypePeople = view.findViewById<TextView>(R.id.txtTypePeople)

            if (people != null) {
                txtNamePeople.text= people.name
                if(people.species?.name==null && people.species?.homeworld?.name==null){
                    txtTypePeople.text="Unknow"
                }else{
                    if(people.species?.name!=null && people.species?.homeworld?.name!=null){
                        txtTypePeople.text= people.species?.name+" from "+ people.species?.homeworld?.name
                    }else{
                        if(people.species?.name==null){
                            txtTypePeople.text= "from "+ people.species?.homeworld?.name
                        }else{
                            if(people.species?.homeworld?.name==null){
                                txtTypePeople.text= people.species?.name
                            }
                        }

                    }
                }
               /*

               */



            }

            view.setOnClickListener {

                val intent = Intent(view.context, PersonDetailActivity::class.java)
                val lv = ArrayList<String>()
                val listVehicles=people?.vehicleConnection?.vehicles?.toList()

                if (listVehicles != null) {
                    for ( vehicle in listVehicles){
                            lv.add(vehicle?.name.toString())
                    }
                }

                // Cargamos el intent para mandar datos de detalle
                val peopleInent = PeopleDetail(people?.eyeColor.toString(),people?.hairColor.toString(),people?.skinColor.toString(),
                    people?.birthYear.toString(),lv)
                intent.putExtra("people",peopleInent)
                view.context.startActivity(intent)
            }

        }
    }

}