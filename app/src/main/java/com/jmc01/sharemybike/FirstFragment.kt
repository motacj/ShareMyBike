package com.jmc01.sharemybike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import java.util.Calendar
import java.util.Locale
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.jmc01.sharemybike.ui.theme.BikesContent

class FirstFragment : Fragment() {

    // Variable para almacenar la referencia al TextView
    private lateinit var txtDate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Obtener referencias de los widgets
        val calendarView = view.findViewById<CalendarView>(R.id.cldBike)

        // Asignación de la referencia: txtDate apunta al ID textView_date_display
        txtDate = view.findViewById<TextView>(R.id.txtDate)

        // Sigancion de la referencia: btnBuscarBicicletas apunta al ID btnBuscarBicicletas
        //val btnBuscarBicicletas = view.findViewById<Button>(R.id.btnBuscarBicicletas)



        // 2. Establecer el texto inicial con la fecha actual
        val calendar = Calendar.getInstance()
        updateDateDisplay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        // 3. Añadir el Callback onSelectedDayChange
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Se llama a esta función cuando el usuario selecciona un día
            updateDateDisplay(year, month, dayOfMonth)
            findNavController().navigate(R.id.action_FirstFragment_to_ItemFragment)
        }

        //btnBuscarBicicletas.setOnClickListener {
       //     BikesContent.selectedDate = txtDate.text.toString().replace("Fecha seleccionada: ", "")

        //}
    }

    // Método para actualizar el TextView
    private fun updateDateDisplay(year: Int, month: Int, dayOfMonth: Int) {
        // Formatear la fecha como DÍA/MES/AÑO. (month + 1) porque el mes es base-cero.
        val dateString = String.format(
            Locale.getDefault(),
            "Fecha seleccionada: %d/%d/%d",
            dayOfMonth, (month + 1), year
        )
        // Asignar el texto al TextView
        txtDate.text = dateString
    }
}