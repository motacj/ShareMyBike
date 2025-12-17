package com.jmc01.sharemybike

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
// Importamos nuestra clase de datos
import com.jmc01.sharemybike.ui.theme.BikesContent

// Recibe la lista de objetos Bike en el constructor
class MyItemRecyclerViewAdapter(
    private val values: List<BikesContent.Bike>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    // 1. Infla el layout de la fila (fragment_item.xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false) // 👈 Debe apuntar a tu layout de fila
        return ViewHolder(view)
    }

    // 2. Vincula los datos de un objeto Bike con la vista de la fila
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        // Asignación de datos a los TextViews
        holder.cityView.text = item.city
        holder.ownerView.text = item.owner
        holder.locationView.text = item.location
        holder.descriptionView.text = item.description

        // Asignación de la imagen (Bitmap)
        if (item.image != null) {
            holder.photoView.setImageBitmap(item.image)
        } else {
            // Un icono de alerta como fallback si la imagen no existe
            holder.photoView.setImageResource(android.R.drawable.ic_dialog_alert)
        }
    }

    override fun getItemCount(): Int = values.size

    // 3. Clase ViewHolder: Almacena las referencias a los componentes de UI de la fila
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Asume que los IDs en fragment_item.xml son:
        val photoView: ImageView = view.findViewById(R.id.bike_photo)
        val cityView: TextView = view.findViewById(R.id.bike_city)
        val ownerView: TextView = view.findViewById(R.id.bike_owner)
        val locationView: TextView = view.findViewById(R.id.bike_location)
        val descriptionView: TextView = view.findViewById(R.id.bike_description)
        val emailIconView: ImageView = view.findViewById(R.id.bike_email_icon)

        override fun toString(): String {
            return super.toString() + " '" + ownerView.text + "'"
        }
    }
}