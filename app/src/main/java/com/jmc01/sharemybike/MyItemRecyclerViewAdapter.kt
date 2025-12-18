// Indica el paquete (namespace) donde vive este archivo.
// Android y Kotlin usan esto para organizar clases y evitar conflictos de nombres.
package com.jmc01.sharemybike

// Importa la clase base RecyclerView.
// RecyclerView es el componente que muestra listas “eficientes” en Android.
import androidx.recyclerview.widget.RecyclerView

// Importa LayoutInflater, que sirve para “convertir” un XML de layout en una vista real (View) en tiempo de ejecución.
import android.view.LayoutInflater

// Importa View, la clase base de cualquier elemento visual en Android (TextView, ImageView, etc.).
import android.view.View

// Importa ViewGroup, que es un contenedor de Views (por ejemplo, LinearLayout, ConstraintLayout, etc.).
import android.view.ViewGroup

// Importa ImageView, una vista para mostrar imágenes (bitmaps, recursos drawable, etc.).
import android.widget.ImageView

// Importa TextView, una vista para mostrar texto.
import android.widget.TextView

// Importa Intent, que es el mecanismo estándar en Android para “pedirle” al sistema que abra otra pantalla/app o ejecute una acción.
import android.content.Intent

// Importa Uri, que representa una URI (por ejemplo, mailto:, geo:, https:, etc.).
import android.net.Uri

// Importa tu clase de datos BikesContent.
// BikesContent parece ser un objeto/clase donde guardas la lista de bicis y datos compartidos (como selectedDate).
import com.jmc01.sharemybike.ui.theme.BikesContent

// Declara la clase del Adapter del RecyclerView.
// Un Adapter es el “puente” entre tu lista de datos (values) y las filas visibles (Views) del RecyclerView.
class MyItemRecyclerViewAdapter(
    // Guarda la lista de datos que el RecyclerView va a mostrar.
    // Cada elemento es un BikesContent.Bike (tu modelo de bicicleta).
    private val values: List<BikesContent.Bike>,
    private val onBikeClick: (BikesContent.Bike) -> Unit
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    // Esta función se llama cuando el RecyclerView necesita crear una nueva fila (ViewHolder) porque no hay una reutilizable.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea (infla) la vista de una fila a partir del XML fragment_item.xml.
        // parent.context es el Context necesario para acceder a recursos del sistema/app.
        val view = LayoutInflater.from(parent.context)
            // inflate convierte el archivo XML (R.layout.fragment_item) en un objeto View real.
            .inflate(R.layout.fragment_item, parent, false)

        // Devuelve un ViewHolder, que es un objeto que “sujeta” referencias a las vistas internas de esa fila.
        return ViewHolder(view)
    }

    // Esta función se llama para “rellenar” una fila con los datos del elemento correspondiente.
    // position es el índice de la lista values que se está mostrando en esa fila.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Obtiene el objeto Bike de la posición actual.
        val item = values[position]

        // Asigna la ciudad al TextView correspondiente en la fila.
        holder.cityView.text = item.city

        // Asigna el propietario al TextView correspondiente en la fila.
        holder.ownerView.text = item.owner

        // Asigna la localización/dirección al TextView correspondiente en la fila.
        holder.locationView.text = item.location

        // Asigna la descripción al TextView correspondiente en la fila.
        holder.descriptionView.text = item.description

        // Comprueba si la bici trae una imagen (Bitmap) o no.
        if (item.image != null) {
            // Si hay imagen, la coloca en el ImageView de la fila.
            holder.photoView.setImageBitmap(item.image)
        } else {
            // Si no hay imagen, pone un icono del sistema Android como “fallback” (sustituto).
            holder.photoView.setImageResource(android.R.drawable.ic_dialog_alert)
        }

        // Configura un listener (escuchador) de clicks para el icono de email.
        // Cuando el usuario pulsa el icono, se llama a sendBikeReservationEmail.
        holder.emailIconView.setOnClickListener {
            // Llama a la función que construye y lanza el Intent de email.
            // holder.itemView es la vista “raíz” de la fila (sirve para obtener el context).
            onBikeClick(item)
        }

        // Configura también el click en toda la fila (no solo en el icono).
        // Si el usuario pulsa en cualquier parte del item, se intenta enviar el email.
        holder.itemView.setOnClickListener {
            // it es la vista pulsada (en este caso, la fila completa).
            onBikeClick(item)
        }
    }

    // Devuelve cuántos elementos hay en la lista; el RecyclerView necesita esto para saber cuántas filas dibujar.
    override fun getItemCount(): Int = values.size

    // Declara una función privada (solo visible dentro de este archivo/clase).
    // Su objetivo es construir un email de “reserva de bici” y pedirle a Android que abra una app de correo.
    private fun sendBikeReservationEmail(view: View, bike: BikesContent.Bike) {
        // Obtiene la fecha seleccionada desde BikesContent.
        // Si selectedDate es null, usa un texto por defecto.
        val selectedDate = BikesContent.selectedDate ?: "a date to be confirmed"

        // Construye el cuerpo del email usando un string multilínea.
        // trimIndent() elimina indentación extra para que el texto final quede “limpio”.
        val emailBody = """
        Dear Mr/Mrs ${bike.owner}:
        
        I'd like to use your bike at <span class="katex"><span class="katex-mathml"><math xmlns="http://www.w3.org/1998/Math/MathML"><semantics><mrow><mrow><mi>b</mi><mi>i</mi><mi>k</mi><mi>e</mi><mi mathvariant="normal">.</mi><mi>l</mi><mi>o</mi><mi>c</mi><mi>a</mi><mi>t</mi><mi>i</mi><mi>o</mi><mi>n</mi></mrow><mo stretchy="false">(</mo></mrow><annotation encoding="application/x-tex">{bike.location} (</annotation></semantics></math></span><span class="katex-html" aria-hidden="true"><span class="base"><span class="strut" style="height:1em;vertical-align:-0.25em;"></span><span class="mord"><span class="mord mathnormal" style="margin-right:0.03148em;">bik</span><span class="mord mathnormal">e</span><span class="mord">.</span><span class="mord mathnormal" style="margin-right:0.01968em;">l</span><span class="mord mathnormal">oc</span><span class="mord mathnormal">a</span><span class="mord mathnormal">t</span><span class="mord mathnormal">i</span><span class="mord mathnormal">o</span><span class="mord mathnormal">n</span></span><span class="mopen">(</span></span></span></span>{bike.city})
        for the following date: $selectedDate
        
        Can you confirm its availability?
        
        Kindest regards
    """.trimIndent()

        // Crea un Intent “implícito” para enviar correo.
        // ACTION_SENDTO indica que quieres enviar a un destinatario (por ejemplo mailto:).
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            // Define la URI del destinatario usando el esquema mailto: (correo).
            // Aquí estás metiendo el email directamente dentro de la URI.
            data = Uri.parse("mailto:${bike.email}")

            // EXTRA_SUBJECT define el asunto del email.
            putExtra(Intent.EXTRA_SUBJECT, "Bike Reservation Request - ${bike.city}")

            // EXTRA_TEXT define el cuerpo del email.
            putExtra(Intent.EXTRA_TEXT, emailBody)
        }

        // Comprueba si existe alguna app instalada capaz de manejar ese Intent.
        if (emailIntent.resolveActivity(view.context.packageManager) != null) {
            // Si hay app compatible, lanza la actividad (abrirá Gmail/Outlook, etc., para redactar el correo).
            view.context.startActivity(emailIntent)
        }
    }

    // Declara la clase ViewHolder dentro del adapter.
    // ViewHolder “cachea” (guarda) referencias a las vistas para no hacer findViewById repetidamente.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Busca el ImageView de la foto de la bici dentro del layout fragment_item.xml.
        val photoView: ImageView = view.findViewById(R.id.bike_photo)

        // Busca el TextView de ciudad.
        val cityView: TextView = view.findViewById(R.id.bike_city)

        // Busca el TextView de propietario.
        val ownerView: TextView = view.findViewById(R.id.bike_owner)

        // Busca el TextView de localización.
        val locationView: TextView = view.findViewById(R.id.bike_location)

        // Busca el TextView de descripción.
        val descriptionView: TextView = view.findViewById(R.id.bike_description)

        // Busca el ImageView del icono de email.
        val emailIconView: ImageView = view.findViewById(R.id.bike_email_icon)

        // Sobrescribe toString() para que, cuando se imprima el ViewHolder, incluya el texto del ownerView.
        override fun toString(): String {
            // Llama al toString() original de la clase padre y le concatena el texto del propietario.
            return super.toString() + " '" + ownerView.text + "'"
        }
    }
}
