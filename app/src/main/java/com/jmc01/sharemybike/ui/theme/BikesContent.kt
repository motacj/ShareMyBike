package com.jmc01.sharemybike.ui.theme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

object BikesContent {
    // Lista de todas las bicicletas a listar en el RecyclerView
    val ITEMS: MutableList<Bike> = ArrayList()
    var selectedDate: String? = null // Almacena la fecha seleccionada

    /**
     * Carga la lista de bicicletas desde el archivo bikeList.json ubicado en la carpeta assets.
     */
    fun loadBikesFromJSON(context: Context) {
        // Aseguramos que la lista se cargue solo una vez
        if (ITEMS.isNotEmpty()) return

        var json: String? = null
        try {
            // 1. Leer el archivo JSON de assets
            val inputStream = context.assets.open("bikeList.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)

            // 2. Parsear el JSON
            val jsonObject = JSONObject(json)
            val bikeList = jsonObject.getJSONArray("bike_list")

            for (i in 0 until bikeList.length()) {
                val jsonBike = bikeList.getJSONObject(i)
                val owner = jsonBike.getString("owner")
                val description = jsonBike.getString("description")
                val city = jsonBike.getString("city")
                val location = jsonBike.getString("location")
                val email = jsonBike.getString("email")

                var photo: Bitmap? = null

                try {
                    // 3. Cargar la imagen de assets
                    val imageStream = context.assets.open("images/" + jsonBike.getString("image"))
                    photo = BitmapFactory.decodeStream(imageStream)
                    imageStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }


                // 4. Añadir la bicicleta a la lista
                ITEMS.add(
                    Bike(
                        photo, owner, description, city, location, email
                    )
                )



            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Clase que representa una Bicicleta
     */
    data class Bike(
        val image: Bitmap?,
        val owner: String,
        val description: String,
        val city: String,
        val location: String,
        val email: String
    ) {
        override fun toString(): String {
            return "$owner $description"
        }
    }
}