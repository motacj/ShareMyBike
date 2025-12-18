// Define el paquete (namespace) donde se encuentra esta clase.
package com.jmc01.sharemybike

// Importa la clase Bundle, usada para recibir/guardar estado (por ejemplo al rotar pantalla).
import android.os.Bundle

// Importa AppCompatActivity: una Activity “clásica” (pantalla) compatible con muchas versiones de Android.
import androidx.appcompat.app.AppCompatActivity

// Importa findNavController para obtener el controlador de navegación asociado a un NavHost.
import androidx.navigation.findNavController

// Importa AppBarConfiguration para configurar cómo se comporta la barra superior (Up/back) con Navigation.
import androidx.navigation.ui.AppBarConfiguration

// Importa navigateUp para gestionar la navegación “Up” (volver dentro del grafo).
import androidx.navigation.ui.navigateUp

// Importa setupActionBarWithNavController para sincronizar la ActionBar/Toolbar con el NavController.
import androidx.navigation.ui.setupActionBarWithNavController

// Importa el binding generado para activity_bike.xml (ViewBinding).
import com.jmc01.sharemybike.databinding.ActivityBikeBinding

// Importa BikesContent, tu “fuente” de datos de bicis y datos compartidos (como selectedDate).
import com.jmc01.sharemybike.ui.theme.BikesContent

// Declara una Activity: una pantalla que contiene un NavHost y navega entre fragments.
class BikeActivity : AppCompatActivity() {

    // Declara una variable para configurar la ActionBar con el grafo de navegación.
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Declara una variable para acceder al layout mediante ViewBinding.
    private lateinit var binding: ActivityBikeBinding

    // Método del ciclo de vida que se ejecuta al crear la pantalla.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama al onCreate de la clase padre (imprescindible).
        super.onCreate(savedInstanceState)

        // Crea el objeto binding inflando el layout XML asociado.
        binding = ActivityBikeBinding.inflate(layoutInflater)

        // Establece como contenido de la pantalla la vista raíz del binding.
        setContentView(binding.root)

        // Carga las bicis desde un JSON usando el contexto de la aplicación (no el del fragment).
        BikesContent.loadBikesFromJSON(applicationContext)

        // Indica que tu toolbar (del layout) será la ActionBar de esta Activity.
        setSupportActionBar(binding.toolbar)

        // Obtiene el NavController asociado al NavHostFragment del layout activity_bike.xml.
        val navController = findNavController(R.id.nav_host_fragment_content_bike)

        // Crea la configuración de la AppBar a partir del grafo de navegación.
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Conecta la ActionBar/Toolbar con el NavController para que el título y el botón “Up” funcionen.
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Método llamado cuando el usuario pulsa el botón “Up” (flecha atrás de la Toolbar).
    override fun onSupportNavigateUp(): Boolean {
        // Obtiene de nuevo el NavController (mismo que en onCreate).
        val navController = findNavController(R.id.nav_host_fragment_content_bike)

        // Intenta navegar “Up” dentro del grafo; si no puede, delega en el comportamiento por defecto.
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
