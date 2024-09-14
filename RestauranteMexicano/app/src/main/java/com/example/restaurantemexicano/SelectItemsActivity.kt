package com.example.restaurantemexicano

import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SelectItemsActivity : AppCompatActivity() {
    private lateinit var spnComidas: Spinner
    private lateinit var spnBebidas: Spinner
    private lateinit var btnAddComida: Button
    private lateinit var btnAddBebida: Button
    private lateinit var btnPlaceOrder: Button
    private lateinit var btnClearAll: Button
    private lateinit var edtUserName: EditText
    private lateinit var txtOrderSummary: TextView
    private lateinit var listViewItems: ListView
    private lateinit var database: DatabaseReference

    private val selectedItems = mutableListOf<Pair<String, String>>()
    private val selectedItemsAdapter by lazy {
        ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            selectedItems.map { "${it.first} - $${it.second}" }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_items)

        spnComidas = findViewById(R.id.spnComidas)
        spnBebidas = findViewById(R.id.spnBebidas)
        btnAddComida = findViewById(R.id.btnAddComida)
        btnAddBebida = findViewById(R.id.btnAddBebida)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        btnClearAll = findViewById(R.id.btnClearAll)
        edtUserName = findViewById(R.id.edtUserName)
        txtOrderSummary = findViewById(R.id.txtOrderSummary)
        listViewItems = findViewById(R.id.listViewItems)

        database = FirebaseDatabase.getInstance().getReference("pedidos")

        val comidas = listOf("Tacos", "Burritos", "Quesadillas", "Enchiladas", "Tamales", "Chilaquiles", "Pozole", "Sopes", "Tostadas", "Mole")
        val preciosComidas = listOf("6.00", "5.00", "4.00", "5.00", "6.00", "4.00", "3.00", "7.00", "6.00", "8.00")

        val bebidas = listOf("Agua", "Refresco", "Cerveza", "Jugo", "Agua de Horchata", "Té Helado", "Michelada", "Mezcal", "Tequila", "Café de Olla")
        val preciosBebidas = listOf("2.00", "3.00", "5.00", "4.00", "3.00", "3.50", "6.00", "10.00", "12.00", "3.50")

        val comidasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, comidas.zip(preciosComidas) { comida, precio -> "$comida - $$precio" })
        val bebidasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bebidas.zip(preciosBebidas) { bebida, precio -> "$bebida - $$precio" })

        comidasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bebidasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spnComidas.adapter = comidasAdapter
        spnBebidas.adapter = bebidasAdapter

        listViewItems.adapter = selectedItemsAdapter

        btnAddComida.setOnClickListener {
            val selectedComida = comidas[spnComidas.selectedItemPosition].split(" - ")[0]
            val selectedComidaPrecio = preciosComidas[spnComidas.selectedItemPosition]

            selectedItems.add(Pair(selectedComida, selectedComidaPrecio))
            selectedItemsAdapter.notifyDataSetChanged()
            updateOrderSummary()
        }

        btnAddBebida.setOnClickListener {
            val selectedBebida = bebidas[spnBebidas.selectedItemPosition].split(" - ")[0]
            val selectedBebidaPrecio = preciosBebidas[spnBebidas.selectedItemPosition]

            selectedItems.add(Pair(selectedBebida, selectedBebidaPrecio))
            selectedItemsAdapter.notifyDataSetChanged()
            updateOrderSummary()
        }

        listViewItems.setOnItemClickListener { _, _, position, _ ->
            selectedItems.removeAt(position)
            selectedItemsAdapter.notifyDataSetChanged()
            updateOrderSummary()
        }

        btnClearAll.setOnClickListener {
            selectedItems.clear()
            selectedItemsAdapter.notifyDataSetChanged()
            updateOrderSummary()
        }

        btnPlaceOrder.setOnClickListener {
            val userName = edtUserName.text.toString()
            if (selectedItems.isNotEmpty() && userName.isNotEmpty()) {
                val orderId = database.push().key ?: ""
                val order = mapOf(
                    "usuario" to userName,
                    "items" to selectedItems.map { it.first },
                    "total" to calculateTotal()
                )

                database.child(orderId).setValue(order).addOnSuccessListener {
                    showOrderDetails(userName, selectedItems, calculateTotal())
                    selectedItems.clear()
                    selectedItemsAdapter.notifyDataSetChanged()
                    updateOrderSummary()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al realizar el pedido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateOrderSummary() {
        val total = calculateTotal()
        txtOrderSummary.text = "Total: $$total\nNúmero de ítems: ${selectedItems.size}"
    }

    private fun calculateTotal(): Double {
        return selectedItems.sumOf { it.second.toDouble() }
    }

    private fun showOrderDetails(userName: String, items: List<Pair<String, String>>, total: Double) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Detalles del Pedido")

        val orderDetails = StringBuilder()
        orderDetails.append("Nombre del Usuario: $userName\n\n")
        orderDetails.append("Detalles del Pedido:\n")
        items.forEach {
            orderDetails.append("${it.first} - $${it.second}\n")
        }
        orderDetails.append("\nTotal: $$total")

        builder.setMessage(orderDetails.toString())

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}
