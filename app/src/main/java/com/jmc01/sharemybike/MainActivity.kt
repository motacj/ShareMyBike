package com.jmc01.sharemybike

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jmc01.sharemybike.ui.theme.ShareMyBikeTheme
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {

    
    private lateinit var imgGeo : ImageButton
    private lateinit var txtGeo : TextView
    private lateinit var imgMail : ImageButton
    private lateinit var txtMail : TextView
    private lateinit var btnLogin : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShareMyBikeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        setContentView(R.layout.activity_main)

        imgGeo = findViewById(R.id.imgGeo)
        txtGeo = findViewById(R.id.txtGeo)
        btnLogin = findViewById(R.id.btnLogin)

        // Al pulsar el ImageButton, abrimos Google Maps
        imgGeo.setOnClickListener {
            txtGeo.text = "Abriendo Google Maps..."
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0"))
            startActivity(i)
        }

        if (btnLogin != null) {
            btnLogin.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    // Crea un Intent para la nueva actividad
                    val intent = Intent(this@MainActivity, BikeActivity::class.java)
                    // Inicia la actividad
                    startActivity(intent)
                }
            })
        }


        // Si la Activity se abrió desde Maps con geo:..., procesamos las coordenadas
        handleGeoIntent(intent)
        handleSharedText(intent)
    }

    /**
     * Método que procesa el Intent recibido desde Google Maps.
     */
    private fun handleGeoIntent(intent: Intent?) {
        val uri: Uri = intent?.data ?: return
        if (uri.scheme != "geo") return

        val raw = uri.schemeSpecificPart
        val coords = raw.substringBefore("?")
        val parts = coords.split(",")

        if (parts.size >= 2) {
            val lat = parts[0]
            val lon = parts[1]
            txtGeo.text = "$lat,$lon"
        } else {
            txtGeo.text = "No se pudieron leer las coordenadas"
        }
    }

    /**
     * Método que procesa texto compartido desde Google Maps.
     */
    private fun handleSharedText(intent: Intent?) {
        if (intent?.action != Intent.ACTION_SEND) return
        if (intent.type != "text/plain") return

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return

        // Si es enlace corto, resolvemos la redirección. Esta función ya usa Thread.
        if (sharedText.startsWith("https://maps.app.goo.gl")) {
            resolveShortMapsUrl(sharedText)
        } else {
            // Si no es un enlace corto, podemos extraer las coordenadas directamente
            val latLon = extractLatLonFromGoogleData(sharedText)
            if (latLon != null) {
                txtGeo.text = "Latitud: ${latLon.first}\nLongitud: ${latLon.second}"
            } else {
                txtGeo.text = "No se pudieron extraer coordenadas"
            }
        }
    }

    // LA LÓGICA DE RED ESTÁ CORRECTAMENTE EN EL HILO SECUNDARIO
    private fun resolveShortMapsUrl(shortUrl: String) {
        Thread {
            try {
                val connection = URL(shortUrl).openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                connection.connect()

                val location = connection.getHeaderField("Location")

                runOnUiThread {
                    if (location != null) {
                        val latLon = extractLatLonFromGoogleData(location)
                        if (latLon != null) {
                            txtGeo.text = "Latitud: ${latLon.first}\nLongitud: ${latLon.second}"
                        } else {
                            txtGeo.text = "Redirección recibida:\n$location"
                        }
                    } else {
                        txtGeo.text = "No se pudo resolver el enlace"
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    txtGeo.text = "Error al resolver enlace: ${e.message}"
                }
            }
        }.start()
    }

    private fun extractLatLonFromGoogleData(url: String): Pair<String, String>? {
        val latRegex = Regex("!3d(-?\\d+(?:\\.\\d+)?)")
        val lonRegex = Regex("!4d(-?\\d+(?:\\.\\d+)?)")

        val latMatch = latRegex.find(url)
        val lonMatch = lonRegex.find(url)

        return if (latMatch != null && lonMatch != null) {
            latMatch.groupValues[1] to lonMatch.groupValues[1]
        } else {
            null
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShareMyBikeTheme {
        Greeting("Android")
    }
}

