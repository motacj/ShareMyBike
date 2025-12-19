// Define el paquete del archivo.
package com.jmc01.sharemybike

// Importa Bundle para estado del fragment.
import android.os.Bundle

// Importa Fragment para crear una pantalla modular dentro de una Activity.
import androidx.fragment.app.Fragment

// Importa LayoutInflater para inflar layouts XML.
import android.view.LayoutInflater

// Importa View (base de cualquier componente visual).
import android.view.View

// Importa ViewGroup (contenedor padre del layout del fragment).
import android.view.ViewGroup

// Importa CalendarView, el widget de calendario del sistema.
import android.widget.CalendarView

// Importa TextView para mostrar texto.
import android.widget.TextView

// Importa Calendar para manejar fechas.
import java.util.Calendar

// Importa Locale para formatear texto según idioma/región del dispositivo.
import java.util.Locale

// Importa Button (aunque aquí finalmente no se usa).
import android.widget.Button

// Importa findNavController para navegar entre fragments mediante Navigation Component.
import androidx.navigation.fragment.findNavController

// Importa BikesContent (aunque aquí no se usa actualmente para guardar selectedDate).
import com.jmc01.sharemybike.ui.theme.BikesContent

// Declara el fragment donde el usuario selecciona una fecha en un CalendarView.
class FirstFragment : Fragment() {

    // Declara un TextView que se inicializará más tarde (lateinit) cuando la vista exista.
    private lateinit var txtDate: TextView

    // Crea e infla el layout del fragment.
    override fun onCreateView(
        // inflater permite inflar XML.
        inflater: LayoutInflater,
        // container es el contenedor padre.
        container: ViewGroup?,
        // savedInstanceState estado previo.
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout fragment_first.xml.
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    // Se llama cuando la vista ya existe y puedes hacer findViewById y listeners.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Llama al comportamiento base.
        super.onViewCreated(view, savedInstanceState)

        // Busca el CalendarView del layout por su ID.
        val calendarView = view.findViewById<CalendarView>(R.id.cldBike)

        // Asigna a txtDate el TextView donde se mostrará la fecha seleccionada.
        txtDate = view.findViewById<TextView>(R.id.txtDate)

        // Obtiene un Calendar con la fecha actual del sistema.
        val calendar = Calendar.getInstance()

        // Llama a updateDateDisplay para mostrar la fecha actual como texto inicial.
        updateDateDisplay(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Establece un listener: se ejecuta cuando el usuario cambia/selecciona un día en el calendario.
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Actualiza el TextView con la fecha seleccionada.
            updateDateDisplay(year, month, dayOfMonth)

            // Navega al destino definido en el grafo de navegación (action_FirstFragment_to_ItemFragment).
            findNavController().navigate(R.id.action_FirstFragment_to_ItemFragment)
        }
    }

    // Función privada para actualizar el TextView txtDate con una fecha formateada.
    private fun updateDateDisplay(year: Int, month: Int, dayOfMonth: Int) {
        // Construye un string formateado con día/mes/año.
        // (month + 1) porque CalendarView/Calendar trabajan con meses base 0 (enero = 0).
        val dateString = String.format(
            Locale.getDefault(),
            "Fecha seleccionada: %d/%d/%d",
            dayOfMonth,
            (month + 1),
            year
        )

        // Asigna el texto formateado al TextView para que se muestre en pantalla.
        txtDate.text = dateString
    }
}
