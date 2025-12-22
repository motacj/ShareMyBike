// Paquete al que pertenece este archivo.
// Sirve para organizar el proyecto.
package com.jmc01.sharemybike

// Importa Bundle, que sirve para guardar/restaurar estado.
import android.os.Bundle

// Importa las clases necesarias para crear y mostrar pantallas (Fragments).
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

// Importa Navigation Component para poder moverse entre pantallas.
import androidx.navigation.fragment.findNavController

// Importa la clase de View Binding generada automáticamente
// a partir del archivo fragment_first.xml.
import com.jmc01.sharemybike.databinding.FragmentFirstBinding

// Importa Calendar para trabajar con fechas.
import java.util.Calendar

// Importa Locale para mostrar el texto según el idioma del móvil.
import java.util.Locale

// Declara la clase FirstFragment.
// Un Fragment es una pantalla que se muestra dentro de una Activity.
class FirstFragment : Fragment() {

    // _binding es una variable que guarda la conexión con el layout.
    // Es nullable porque la vista puede no existir todavía.
    private var _binding: FragmentFirstBinding? = null

    // binding es una forma cómoda y segura de usar _binding.
    // El !! indica que solo se usará cuando la vista exista.
    private val binding get() = _binding!!

    // Este método se llama cuando Android necesita crear la pantalla.
    override fun onCreateView(
        inflater: LayoutInflater,   // Sirve para cargar el XML.
        container: ViewGroup?,      // Contenedor padre de la vista.
        savedInstanceState: Bundle? // Estado anterior (si existe).
    ): View {
        // Aquí se crea el binding a partir del layout fragment_first.xml.
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // Se devuelve la vista raíz del layout.
        return binding.root
    }

    // Este método se ejecuta cuando la vista ya está creada.
    // Aquí es donde se ponen listeners y lógica.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Llama al método de la clase padre.
        super.onViewCreated(view, savedInstanceState)

        // Obtiene la fecha actual del sistema.
        val calendar = Calendar.getInstance()

        // Muestra la fecha actual al iniciar la pantalla.
        updateDateDisplay(
            calendar.get(Calendar.YEAR),         // Año actual
            calendar.get(Calendar.MONTH),        // Mes actual (empieza en 0)
            calendar.get(Calendar.DAY_OF_MONTH)  // Día actual
        )

        // Listener del CalendarView.
        // Se ejecuta cuando el usuario selecciona una fecha.
        binding.cldBike.setOnDateChangeListener { _, year, month, dayOfMonth ->

            // Actualiza el texto con la fecha seleccionada.
            updateDateDisplay(year, month, dayOfMonth)

            // Navega a la siguiente pantalla definida en nav_graph.xml.
            findNavController().navigate(
                R.id.action_FirstFragment_to_ItemFragment
            )
        }
    }

    // Función privada que actualiza el texto con la fecha seleccionada.
    private fun updateDateDisplay(year: Int, month: Int, dayOfMonth: Int) {

        // Crea un texto con el formato día/mes/año.
        // Se suma 1 al mes porque empieza en 0 (enero = 0).
        val dateString = String.format(
            Locale.getDefault(),
            "Fecha seleccionada: %d/%d/%d",
            dayOfMonth,
            (month + 1),
            year
        )

        // Asigna el texto al TextView usando View Binding.
        binding.txtDate.text = dateString
    }

    // Este método se llama cuando la vista se destruye.
    // MUY IMPORTANTE: limpiar el binding para evitar errores de memoria.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
