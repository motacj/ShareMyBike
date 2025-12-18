// Define el paquete (namespace) donde se encuentra este archivo.
// Sirve para organizar el código y evitar conflictos de nombres.
package com.jmc01.sharemybike.ui.theme

// Importa Context, necesario para acceder a recursos de la app (assets, recursos, etc.).
import android.content.Context

// Importa Bitmap, la clase que representa una imagen en memoria.
import android.graphics.Bitmap

// Importa BitmapFactory, usada para convertir un archivo de imagen en un Bitmap.
import android.graphics.BitmapFactory

// Importa JSONException, excepción que se lanza si el JSON está mal formado.
import org.json.JSONException

// Importa JSONObject, la clase que permite leer y recorrer objetos JSON.
import org.json.JSONObject

// Importa IOException, excepción típica al leer archivos.
import java.io.IOException

// Importa ArrayList, una lista mutable clásica de Java/Kotlin.
import java.util.ArrayList

// Declara un objeto singleton llamado BikesContent.
// "object" significa que existe UNA sola instancia en toda la app.
// Se usa aquí como almacén global de datos.
object BikesContent {

    // Declara una lista mutable que contendrá todas las bicicletas.
    // Esta lista se usará en el RecyclerView para mostrar los datos.
    val ITEMS: MutableList<Bike> = ArrayList()

    // Variable global para guardar la fecha seleccionada en el calendario.
    // Puede ser null si aún no se ha seleccionado ninguna fecha.
    var selectedDate: String? = null

    /**
     * Función que carga las bicicletas desde un archivo JSON
     * ubicado en la carpeta assets de la aplicación.
     */
    fun loadBikesFromJSON(context: Context) {

        // Si la lista ya tiene elementos, no vuelve a cargar el JSON.
        // Esto evita duplicar bicicletas si se llama varias veces.
        if (ITEMS.isNotEmpty()) return

        // Variable que almacenará el contenido del archivo JSON como texto.
        var json: String? = null

        try {
            // 1. Abrir el archivo bikeList.json desde la carpeta assets.
            // context.assets permite acceder a archivos no compilados (assets).
            val inputStream = context.assets.open("bikeList.json")

            // Obtiene el tamaño del archivo en bytes.
            val size = inputStream.available()

            // Crea un array de bytes del tamaño del archivo.
            val buffer = ByteArray(size)

            // Lee todo el archivo y lo guarda en el buffer.
            inputStream.read(buffer)

            // Cierra el archivo para liberar recursos.
            inputStream.close()

            // Convierte el array de bytes en un String usando UTF-8.
            json = String(buffer, Charsets.UTF_8)

            // 2. Parsear el texto JSON
            // Convierte el String en un objeto JSONObject.
            val jsonObject = JSONObject(json)

            // Obtiene el array "bike_list" del JSON.
            val bikeList = jsonObject.getJSONArray("bike_list")

            // Recorre todas las bicicletas del array JSON.
            for (i in 0 until bikeList.length()) {

                // Obtiene el objeto JSON de una bicicleta concreta.
                val jsonBike = bikeList.getJSONObject(i)

                // Extrae el nombre del propietario.
                val owner = jsonBike.getString("owner")

                // Extrae la descripción de la bicicleta.
                val description = jsonBike.getString("description")

                // Extrae la ciudad donde se encuentra la bicicleta.
                val city = jsonBike.getString("city")

                // Extrae la localización/dirección.
                val location = jsonBike.getString("location")

                // Extrae el email de contacto.
                val email = jsonBike.getString("email")

                // Declara una variable para la foto de la bicicleta.
                // Inicialmente es null por si falla la carga.
                var photo: Bitmap? = null

                try {
                    // 3. Cargar la imagen desde assets/images/
                    // Abre el archivo de imagen indicado en el JSON.
                    val imageStream = context.assets.open(
                        "images/" + jsonBike.getString("image")
                    )

                    // Convierte el archivo de imagen en un Bitmap.
                    photo = BitmapFactory.decodeStream(imageStream)

                    // Cierra el archivo de imagen.
                    imageStream.close()

                } catch (e: IOException) {
                    // Si la imagen no existe o falla la lectura, se imprime el error.
                    e.printStackTrace()
                }

                // 4. Añadir la bicicleta a la lista ITEMS
                // Se crea un objeto Bike con todos los datos leídos.
                ITEMS.add(
                    Bike(
                        photo,
                        owner,
                        description,
                        city,
                        location,
                        email
                    )
                )
            }

        } catch (e: JSONException) {
            // Captura errores si el JSON está mal formado.
            e.printStackTrace()
        } catch (e: IOException) {
            // Captura errores de lectura del archivo JSON.
            e.printStackTrace()
        }
    }

    /**
     * Data class que representa una bicicleta.
     * Una data class en Kotlin genera automáticamente:
     * - constructor
     * - getters
     * - equals()
     * - hashCode()
     * - toString()
     */
    data class Bike(

        // Imagen de la bicicleta (puede ser null si no se cargó).
        val image: Bitmap?,

        // Nombre del propietario.
        val owner: String,

        // Descripción de la bicicleta.
        val description: String,

        // Ciudad donde se encuentra.
        val city: String,

        // Dirección o localización concreta.
        val location: String,

        // Email de contacto del propietario.
        val email: String
    ) {

        // Sobrescribe el método toString().
        // Se usa, por ejemplo, al imprimir el objeto en logs.
        override fun toString(): String {
            return "$owner $description"
        }
    }
}
