// Define el paquete (la “carpeta lógica”) donde está esta clase.
package com.jmc01.sharemybike

// Importa Bundle para recibir/guardar estado (por ejemplo rotación de pantalla).
import android.os.Bundle

// Importa Fragment para crear una pantalla modular dentro de una Activity.
import androidx.fragment.app.Fragment

// Importa LayoutInflater para inflar (convertir) el XML en una vista real.
import android.view.LayoutInflater

// Importa View (base de cualquier componente visual en Android).
import android.view.View

// Importa ViewGroup (contenedor padre donde se coloca el fragment).
import android.view.ViewGroup

// Importa CalendarView, el calendario que se ve en pantalla.
import android.widget.CalendarView

// Importa Calendar para obtener la fecha actual.
import java.util.Calendar

// Importa Locale para formatear texto según el idioma/región del móvil.
import java.util.Locale

// Importa findNavController para navegar a otro fragment con Navigation Component.
import androidx.navigation.fragment.findNavController

// Importa la clase de binding generada desde fragment_first.xml.
// Si tu layout se llama distinto, esta clase cambiará de nombre.
import com.jmc01.sharemybike.databinding.FragmentFirstBinding

// Declara el fragment donde el usuario selecciona una fecha en un CalendarView.
class FirstFragment : Fragment() {

    // Variable privada para guardar el binding.
    // Es nullable porque la vista del fragment puede destruirse y volver a crearse.
    private var _binding: FragmentFirstBinding? = null

    // Propiedad “segura”: aquí usaremos binding sin escribir !! por todo el código.
    // OJO: solo usar entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    // Crea e infla el layout del fragment (aquí se crea la vista).
    override fun onCreateView(
        // inflater infla el XML.
        inflater: LayoutInflater,
        // container es el padre donde se inserta este fragment.
        container: ViewGroup?,
        // savedInstanceState es el estado anterior si Android recrea el fragment.
        savedInstanceState: Bundle?
    ): View {

        // Crea el binding inflando fragment_first.xml.
        // Esto sustituye a inflater.inflate(R.layout.fragment_first, container, false).
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // Devuelve la vista raíz (root) del binding: es la vista principal del fragment.
        return binding.root
    }

    // Se llama cuando la vista ya existe: aquí ponemos listeners, lógica de UI, etc.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Llama al método de la clase padre (buena práctica).
        super.onViewCreated(view, savedInstanceState)

        // Obtiene un Calendar con la fecha actual del sistema.
        val calendar = Calendar.getInstance()

        // Muestra la fecha actual en el TextView nada más abrir el fragment.
        updateDateDisplay(
            calendar.get(Calendar.YEAR),          // Año actual.
            calendar.get(Calendar.MONTH),         // Mes actual (0 = enero, 11 = diciembre).
            calendar.get(Calendar.DAY_OF_MONTH)   // Día del mes actual.
        )

        // Accede al CalendarView directamente con binding (sin findViewById).
        // cldBike debe existir como id en fragment_first.xml.
        binding.cldBike.setOnDateChangeListener { _: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            // Actualiza el texto con la fecha que el usuario selecciona.
            updateDateDisplay(year, month, dayOfMonth)

            // Navega al siguiente fragment usando la acción del grafo de navegación.
            findNavController().navigate(R.id.action_FirstFragment_to_ItemFragment)
        }
    }

    // Función privada para actualizar el TextView con una fecha formateada.
    private fun updateDateDisplay(year: Int, month: Int, dayOfMonth: Int) {

        // Construye el texto final con día/mes/año.
        // (month + 1) porque month viene empezando en 0 (enero = 0).
        val dateString = String.format(
            Locale.getDefault(),                 // Usa el idioma/región del dispositivo.
            "Fecha seleccionada: %d/%d/%d",      // Plantilla del texto.
            dayOfMonth,                          // Día.
            (month + 1),                         // Mes real (sumamos 1).
            year                                 // Año.
        )

        // Escribe el texto en el TextView usando binding (sin txtDate lateinit).
        // txtDate debe existir como id en fragment_first.xml.
        binding.txtDate.text = dateString
    }

    // MUY IMPORTANTE: se llama cuando la vista del fragment se destruye.
    // Aquí se limpia el binding para evitar fugas de memoria (memory leaks).
    override fun onDestroyView() {
        // Llama al método de la clase padre.
        super.onDestroyView()

        // Limpia el binding porque la vista ya no existe.
        _binding = null
    }
}
