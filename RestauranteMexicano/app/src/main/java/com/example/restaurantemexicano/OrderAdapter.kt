import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.restaurantemexicano.R
import com.example.restaurantemexicano.Pedido

class OrderAdapter(context: Context, orders: List<Pedido>) :
    ArrayAdapter<Pedido>(context, R.layout.item_order, orders) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)

        val pedido = getItem(position)

        val txtUserName: TextView = view.findViewById(R.id.txtUserName)
        val txtOrderItems: TextView = view.findViewById(R.id.txtOrderItems)
        val txtOrderTotal: TextView = view.findViewById(R.id.txtOrderTotal)

        txtUserName.text = "Usuario: ${pedido?.usuario}"
        txtOrderItems.text = "Ítems: ${pedido?.items?.joinToString(", ")}"
        txtOrderTotal.text = "Total: $${pedido?.total?.format(2)}"

        return view
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
