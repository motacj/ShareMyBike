package com.jmc01.sharemybike

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import android.net.Uri

// Importamos nuestra clase de datos
import com.jmc01.sharemybike.ui.theme.BikesContent

// Recibe la lista de objetos Bike en el constructor
class MyItemRecyclerViewAdapter(
    private val values: List<BikesContent.Bike>,
    private val onBikeClick: (BikesContent.Bike) -> Unit//*****************
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


        holder.emailIconView.setOnClickListener { onBikeClick(item) }
        holder.itemView.setOnClickListener { onBikeClick(item) }

    }

    override fun getItemCount(): Int = values.size
    // Función para enviar el email de reserva
    private fun sendBikeReservationEmail(view: View, bike: BikesContent.Bike) {
        // Obtener la fecha seleccionada (si existe)
        val selectedDate = BikesContent.selectedDate ?: "a date to be confirmed"

        // Construir el cuerpo del email
        val emailBody = """
        Dear Mr/Mrs ${bike.owner}:
        
        I'd like to use your bike at <span class="katex"><span class="katex-mathml"><math xmlns="http://www.w3.org/1998/Math/MathML"><semantics><mrow><mrow><mi>b</mi><mi>i</mi><mi>k</mi><mi>e</mi><mi mathvariant="normal">.</mi><mi>l</mi><mi>o</mi><mi>c</mi><mi>a</mi><mi>t</mi><mi>i</mi><mi>o</mi><mi>n</mi></mrow><mo stretchy="false">(</mo></mrow><annotation encoding="application/x-tex">{bike.location} (</annotation></semantics></math></span><span class="katex-html" aria-hidden="true"><span class="base"><span class="strut" style="height:1em;vertical-align:-0.25em;"></span><span class="mord"><span class="mord mathnormal" style="margin-right:0.03148em;">bik</span><span class="mord mathnormal">e</span><span class="mord">.</span><span class="mord mathnormal" style="margin-right:0.01968em;">l</span><span class="mord mathnormal">oc</span><span class="mord mathnormal">a</span><span class="mord mathnormal">t</span><span class="mord mathnormal">i</span><span class="mord mathnormal">o</span><span class="mord mathnormal">n</span></span><span class="mopen">(</span></span></span></span>{bike.city})
        for the following date: $selectedDate
        
        Can you confirm its availability?
        
        Kindest regards
    """.trimIndent()

        // Crear el Intent implícito con mailto:
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto: atom.susej@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Bike Reservation Request - ${bike.city}")
            putExtra(Intent.EXTRA_TEXT, emailBody)
        }

        // Verificar que hay una app de email disponible
        if (emailIntent.resolveActivity(view.context.packageManager) != null) {
            view.context.startActivity(Intent.createChooser(emailIntent, "Send email"))
        }
    }

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