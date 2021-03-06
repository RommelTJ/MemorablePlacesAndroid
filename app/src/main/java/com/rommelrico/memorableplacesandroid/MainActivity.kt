package com.rommelrico.memorableplacesandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import java.util.ArrayList

import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {

    companion object {
        var places = ArrayList<String>()
        var locations = ArrayList<LatLng>()
        var arrayAdapter: ArrayAdapter<*>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.listView)
        places.add("Add a new place...")
        locations.add(LatLng(0.0, 0.0))

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, places)

        listView.setAdapter(arrayAdapter)

        listView.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("placeNumber", i)
            startActivity(intent)
        }
    }


}
