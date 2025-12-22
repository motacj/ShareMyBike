package com.jmc01.sharemybike // Ajusta el nombre del paquete si es diferente

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// Asegúrate de que los imports de tu proyecto sean correctos
import com.jmc01.sharemybike.ui.theme.BikesContent

class BikeListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // El layout que contiene el RecyclerView
        // Si tu layout se llama 'fragment_item_list', asegúrate que el ID del RecyclerView dentro sea 'list'
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Configurar el RecyclerView
        val recyclerView = if (view is RecyclerView) view else view.findViewById<RecyclerView>(R.id.list)

        if (recyclerView != null) {
            with(recyclerView) {
                layoutManager = LinearLayoutManager(context)

                // Inicializa el Adaptador con los datos cargados.
                adapter = MyItemRecyclerViewAdapter(BikesContent.ITEMS) { bike ->
                    sendBikeReservationEmail(bike)
                }
            }
        }

        return view
    }

    private fun sendBikeReservationEmail(bike: BikesContent.Bike) {
        val selectedDate = BikesContent.selectedDate ?: "a date to be confirmed"

        val to = bike.email.trim()
        if (to.isBlank()) {
            android.widget.Toast.makeText(requireContext(), "La bici no tiene email", android.widget.Toast.LENGTH_LONG).show()
            return
        }

        val body = """
        Dear Mr/Mrs ${bike.owner}:

        I'd like to use your bike at ${bike.location} (${bike.city})
        for the following date: $selectedDate

        Can you confirm its availability?

        Kindest regards
    """.trimIndent()

        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
            data = android.net.Uri.parse("mailto:")
            putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(android.content.Intent.EXTRA_SUBJECT, "Bike Reservation Request - ${bike.city}")
            putExtra(android.content.Intent.EXTRA_TEXT, body)
        }

        try {
            startActivity(Intent.createChooser(intent, "Enviar email"))
        } catch (e: Exception) {
            android.widget.Toast.makeText(
                requireContext(),
                "No se puede abrir una app de correo: ${e.javaClass.simpleName}",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }

}