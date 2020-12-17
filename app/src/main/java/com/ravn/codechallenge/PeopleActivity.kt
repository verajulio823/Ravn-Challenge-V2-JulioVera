package com.ravn.codechallenge

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.ravn.codechallenge.adapters.PeopleAdapter
import com.ravn.codechallenge.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeopleActivity : AppCompatActivity() {



     lateinit var rvPeoples: RecyclerView
     lateinit var adapter: PeopleAdapter
     lateinit var nstScrollView: NestedScrollView
     lateinit var linearPogress: LinearLayout
     lateinit var txtMessageConection: TextView


     val URL_BASE="https://swapi-graphql.netlify.app/.netlify/functions/index"

     var page =1
     var firstData=5

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)


        // Inicializamos Action Bar
        initActionBar()

        // Inicializamos Views
        init()

        // Cargamos Datos Primera vez
        loadDataFirstTime()





    }

    private fun loadData(first: Int){

        val apolloClient = ApolloClient.builder()
                .serverUrl(URL_BASE)
                .build()
            MainScope().launch {

                val response = try {
                    apolloClient.query(ListPeoplesQuery(size = first)).toDeferred().await()
                } catch (e: ApolloException) {

                    // Manejo de errores de conección
                    linearPogress.visibility = VISIBLE
                    txtMessageConection.text= resources.getString(R.string.message_error_conection)
                    txtMessageConection.setTextColor(resources.getColor(R.color.text_emphasis))

                    return@launch
                }

                val launch = response.data?.allPeople
                if (launch == null || response.hasErrors()) {
                    // Manejo de exceptions
                    return@launch
                }
                linearPogress.visibility= INVISIBLE
                val adapter= PeopleAdapter(launch.people)
                rvPeoples.adapter=adapter
            }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init(){

        // Inicializamos Views
        rvPeoples = findViewById(R.id.rvPeoples)
        nstScrollView = findViewById(R.id.nstScrollView)
        linearPogress = findViewById(R.id.linearProgress)
        txtMessageConection = findViewById(R.id.txtMessageConection)

        // Inicializar Recycler y Adapter
        adapter = PeopleAdapter(listOf<ListPeoplesQuery.Person>())
        rvPeoples.layoutManager = LinearLayoutManager(this)
        rvPeoples.adapter

        // Listener de cambio de scoll
        nstScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            val child = (v as ViewGroup).getChildAt(0)

            // Verificación de las medidas del scroll para siguiente página
            if(scrollY == child.measuredHeight-v.measuredHeight){
                page++
                linearPogress.visibility= VISIBLE
                txtMessageConection.text = resources.getString(R.string.message_conection)
                txtMessageConection.setTextColor(resources.getColor(R.color.text_light))

                loadData(firstData*page)

            }
        }


    }

    private fun loadDataFirstTime(){

        // Sacamos el hijo del View
        val child = nstScrollView.getChildAt(0)

        //Comparamos que la primera ves las medidas de los scroll de los view no sean 0
        //Para llamar una página mas

        if(child.measuredHeight-nstScrollView.measuredHeight==0 || page==1){
            page++
            loadData(firstData*page)
        }

    }

    private fun initActionBar(){

        // Agregamos propiedades al ActionBar
        supportActionBar?.displayOptions = DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.title_activity)

    }


}