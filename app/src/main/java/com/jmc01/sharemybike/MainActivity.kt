// Define el paquete del archivo.
package com.jmc01.sharemybike

// Importa Intent para lanzar acciones (Maps, navegar a otra Activity, recibir contenido compartido, etc.).
import android.content.Intent

// Importa Uri para manejar URIs tipo geo:, mailto:, https:, etc.
import android.net.Uri

// Importa Bundle para estado de la Activity.
import android.os.Bundle

// Importa View para listeners y referencias a vistas.
import android.view.View

// Importa widgets “clásicos” (TextView, Button, ImageButton, etc.).
import android.widget.*

// Importa ComponentActivity: Activity base usada en apps con Jetpack Compose.
import androidx.activity.ComponentActivity

// Importa setContent para definir UI con Compose.
import androidx.activity.compose.setContent

// Importa enableEdgeToEdge para dibujar la UI ocupando hasta bordes (status bar/navigation bar).
import androidx.activity.enableEdgeToEdge

// Importa utilidades de Compose para layouts.
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

// Importa Scaffold (estructura de pantalla material).
import androidx.compose.material3.Scaffold

// Importa Text (composable que muestra texto).
import androidx.compose.material3.Text

// Importa Composable annotation para funciones composables.
import androidx.compose.runtime.Composable

// Importa Modifier para modificar layouts en Compose.
import androidx.compose.ui.Modifier

// Importa Preview para vista previa en Android Studio.
import androidx.compose.ui.tooling.preview.Preview

// Importa el tema Compose de tu app.
import com.jmc01.sharemybike.ui.theme.ShareMyBikeTheme

// Importa HttpURLConnection para resolver redirecciones de URLs.
import java.net.HttpURLConnection

// Importa URL para abrir conexiones HTTP.
import java.net.URL

// Declara la Activity principal de la app.
class MainActivity : ComponentActivity() {

    // Declara un ImageButton para la acción de geolocalización (abrir Maps).
    private lateinit var imgGeo : ImageButton

    // Declara un TextView donde mostrarás información geográfica (coordenadas, mensajes, etc.).
    private lateinit var txtGeo : TextView

    // Declara un ImageButton para correo (no se usa en el código actual).
    private lateinit var imgMail : ImageButton

    // Declara un TextView para correo (no se usa en el código actual).
    private lateinit var txtMail : TextView

    // Declara un Button para login/entrar a la pantalla de bicis.
    private lateinit var btnLogin : Button

    // Método de ciclo de vida que se ejecuta al crear la Activity.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama al onCreate de la clase padre.
        super.onCreate(savedInstanceState)

        // Activa edge-to-edge (UI puede dibujar bajo la status bar).
        enableEdgeToEdge()

        // Define un contenido Compose (aunque luego lo sobreescribes con setContentView).
        setContent {
            // Aplica tu tema Compose.
            ShareMyBikeTheme {
                // Crea la estructura base con Scaffold.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llama al composable Greeting.
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Establece el layout clásico XML activity_main.xml como contenido visible de la Activity.
        setContentView(R.layout.activity_main)

        // Busca el ImageButton de geolocalización por ID.
        imgGeo = findViewById(R.id.imgGeo)

        // Busca el TextView de geolocalización por ID.
        txtGeo = findViewById(R.id.txtGeo)

        // Busca el botón de login por ID.
        btnLogin = findViewById(R.id.btnLogin)

        // Define qué ocurre al pulsar el botón de geolocalización.
        imgGeo.setOnClickListener {
            // Cambia el texto del TextView para informar al usuario.
            txtGeo.text = "Abriendo Google Maps..."

            // Crea un Intent ACTION_VIEW con una URI geo: (esquema para mapas).
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0"))

            // Lanza la app que pueda manejar geo: (normalmente Google Maps).
            startActivity(i)
        }

        // Comprueba si btnLogin no es null (en Kotlin con lateinit, normalmente no hace falta, pero aquí está).
        if (btnLogin != null) {
            // Asigna un listener al botón de login.
            btnLogin.setOnClickListener(object : View.OnClickListener {
                // Se ejecuta cuando se pulsa el botón.
                override fun onClick(view: View?) {
                    // Crea un Intent explícito para abrir BikeActivity.
                    val intent = Intent(this@MainActivity, BikeActivity::class.java)

                    // Inicia BikeActivity.
                    startActivity(intent)
                }
            })
        }

        // Procesa un Intent de tipo geo: si esta Activity fue abierta con un enlace geo:.
        handleGeoIntent(intent)

        // Procesa texto compartido si la Activity fue abierta con ACTION_SEND.
        handleSharedText(intent)
    }

    // Procesa un Intent que contenga una URI geo: y extrae coordenadas para mostrarlas en pantalla.
    private fun handleGeoIntent(intent: Intent?) {
        // Obtiene la Uri de los datos del intent; si es null, sale.
        val uri: Uri = intent?.data ?: return

        // Comprueba que el esquema sea "geo"; si no lo es, sale.
        if (uri.scheme != "geo") return

        // Obtiene la parte específica del esquema (por ejemplo "lat,lon?..." sin "geo:").
        val raw = uri.schemeSpecificPart

        // Se queda con la parte antes de "?" (solo coordenadas).
        val coords = raw.substringBefore("?")

        // Separa latitud y longitud por coma.
        val parts = coords.split(",")

        // Si hay al menos 2 partes, asume que son lat y lon.
        if (parts.size >= 2) {
            // Latitud.
            val lat = parts[0]
            // Longitud.
            val lon = parts[1]
            // Muestra coordenadas en el TextView.
            txtGeo.text = "$lat,$lon"
        } else {
            // Si no se pudieron leer dos valores, muestra un mensaje de error.
            txtGeo.text = "No se pudieron leer las coordenadas"
        }
    }

    // Procesa texto compartido con ACTION_SEND (por ejemplo, desde Google Maps).
    private fun handleSharedText(intent: Intent?) {
        // Si la acción no es ACTION_SEND, no hay texto compartido y se sale.
        if (intent?.action != Intent.ACTION_SEND) return

        // Si el tipo no es texto plano, se sale.
        if (intent.type != "text/plain") return

        // Obtiene el texto compartido; si no hay, se sale.
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return

        // Si es un enlace corto de Google Maps, lo resuelve (siguiendo redirección).
        if (sharedText.startsWith("https://maps.app.goo.gl")) {
            // Llama a la función que resuelve la URL corta en un hilo secundario.
            resolveShortMapsUrl(sharedText)
        } else {
            // Si no es enlace corto, intenta extraer lat/lon directamente desde el texto.
            val latLon = extractLatLonFromGoogleData(sharedText)

            // Si se pudo extraer, muestra latitud/longitud.
            if (latLon != null) {
                txtGeo.text = "Latitud: ${latLon.first}\nLongitud: ${latLon.second}"
            } else {
                // Si no se pudo extraer, muestra mensaje de error.
                txtGeo.text = "No se pudieron extraer coordenadas"
            }
        }
    }

    // Resuelve una URL corta de Google Maps siguiendo la redirección HTTP en un hilo secundario.
    private fun resolveShortMapsUrl(shortUrl: String) {
        // Crea un nuevo hilo para no bloquear la interfaz (UI).
        Thread {
            try {
                // Abre conexión HTTP a la URL corta.
                val connection = URL(shortUrl).openConnection() as HttpURLConnection

                // Evita seguir redirecciones automáticamente (queremos leer el header Location).
                connection.instanceFollowRedirects = false

                // Conecta.
                connection.connect()

                // Lee la cabecera Location, donde suele venir la URL final.
                val location = connection.getHeaderField("Location")

                // Vuelve al hilo principal para actualizar la UI.
                runOnUiThread {
                    // Si hay URL redirigida…
                    if (location != null) {
                        // Intenta extraer lat/lon de la URL final.
                        val latLon = extractLatLonFromGoogleData(location)

                        // Si pudo extraer, muestra coordenadas.
                        if (latLon != null) {
                            txtGeo.text = "Lat: ${latLon.first} Long: ${latLon.second}"
                        } else {
                            // Si no pudo extraer coordenadas, muestra la redirección completa.
                            txtGeo.text = "Redirección recibida:\n$location"
                        }
                    } else {
                        // Si no hay cabecera Location, no se pudo resolver.
                        txtGeo.text = "No se pudo resolver el enlace"
                    }
                }
            } catch (e: Exception) {
                // Si ocurre cualquier error (red, permisos, etc.), vuelve al hilo UI y muestra mensaje.
                runOnUiThread {
                    txtGeo.text = "Error al resolver enlace: ${e.message}"
                }
            }
            // Arranca el hilo.
        }.start()
    }

    // Extrae latitud y longitud de un texto/URL de Google Maps usando expresiones regulares.
    private fun extractLatLonFromGoogleData(url: String): Pair<String, String>? {
        // Regex para encontrar latitud en formato "!3d<numero>".
        val latRegex = Regex("!3d(-?\\d+(?:\\.\\d+)?)")

        // Regex para encontrar longitud en formato "!4d<numero>".
        val lonRegex = Regex("!4d(-?\\d+(?:\\.\\d+)?)")

        // Busca la primera coincidencia de latitud.
        val latMatch = latRegex.find(url)

        // Busca la primera coincidencia de longitud.
        val lonMatch = lonRegex.find(url)

        // Si existen ambas coincidencias, devuelve el par (lat, lon); si no, devuelve null.
        return if (latMatch != null && lonMatch != null) {
            latMatch.groupValues[1] to lonMatch.groupValues[1]
        } else {
            null
        }
    }

}

// Función composable que muestra un texto “Hello <name>!”.
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // Muestra el texto dentro de Compose.
    Text(
        // Texto que se renderiza.
        text = "Hello $name!",
        // Aplica el modifier (tamaño, padding, etc.).
        modifier = modifier
    )
}

// Vista previa en Android Studio del composable Greeting.
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    // Aplica el tema para la preview.
    ShareMyBikeTheme {
        // Llama a Greeting con un ejemplo.
        Greeting("Android")
    }
}
