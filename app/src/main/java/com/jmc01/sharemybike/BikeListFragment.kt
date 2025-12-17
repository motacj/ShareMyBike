package com.jmc01.sharemybike // Ajusta el nombre del paquete si es diferente

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
        // 🚨 CRÍTICO: Si el ID de tu RecyclerView en fragment_item_list.xml no es 'list', usa findViewByID
        val recyclerView = if (view is RecyclerView) view else view.findViewById<RecyclerView>(R.id.list)

        if (recyclerView != null) {
            with(recyclerView) {
                layoutManager = LinearLayoutManager(context)

                // ⭐ PUNTO CLAVE: Inicializa el Adaptador con los datos cargados.
                adapter = MyItemRecyclerViewAdapter(BikesContent.ITEMS)
            }
        }

        return view
    }
}