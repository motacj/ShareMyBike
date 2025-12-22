// Define el paquete del archivo.
package com.jmc01.sharemybike

// Importa Bundle para estado del fragment.
import android.os.Bundle

// Importa Fragment: componente de UI dentro de una Activity.
import androidx.fragment.app.Fragment

// Importa LayoutInflater para inflar el layout XML.
import android.view.LayoutInflater

// Importa View: base de elementos visuales.
import android.view.View

// Importa ViewGroup: contenedor padre del layout del fragment.
import android.view.ViewGroup

// Importa findNavController para navegar entre fragments.
import androidx.navigation.fragment.findNavController

// Importa el binding generado a partir de fragment_second.xml (ViewBinding).
import com.jmc01.sharemybike.databinding.FragmentSecondBinding

// Declara el fragment “SecondFragment”.
class SecondFragment : Fragment() {

    // Variable privada que guardará el binding mientras exista la vista.
    private var _binding: FragmentSecondBinding? = null

    // Propiedad que devuelve _binding no nulo; solo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    // Infla y devuelve la vista del fragment usando ViewBinding.
    override fun onCreateView(
        // inflater infla layouts.
        inflater: LayoutInflater,
        // container contenedor padre.
        container: ViewGroup?,
        // savedInstanceState estado previo.
        savedInstanceState: Bundle?
    ): View {

        // Crea el binding inflando el layout fragment_second.xml.
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        // Devuelve la vista raíz del binding como UI del fragment.
        return binding.root
    }

    // Se llama cuando la vista ya está creada; aquí se ponen listeners.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Llama al comportamiento base.
        super.onViewCreated(view, savedInstanceState)

        // Asigna un listener al botón buttonSecond definido en el layout.
        binding.buttonSecond.setOnClickListener {
            // Navega a FirstFragment usando la acción definida en el grafo de navegación.
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    // Se llama cuando la vista del fragment se destruye; importante para evitar fugas de memoria.
    override fun onDestroyView() {
        // Llama al comportamiento base.
        super.onDestroyView()

        // Libera el binding para que la vista pueda ser recolectada por el GC.
        _binding = null
    }
}
