package com.example.restaurantemexicano

import OrderAdapter
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantemexicano.R
import com.example.restaurantemexicano.Pedido

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: OrderAdapter
    private val orders = mutableListOf<Pedido>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        listView = findViewById(R.id.listViewOrders)
        adapter = OrderAdapter(this, orders)
        listView.adapter = adapter

        addStaticOrders()

        adapter.notifyDataSetChanged()
    }

    private fun addStaticOrders() {
        orders.add(Pedido("Erick", listOf("Tacos", "Michelada"), "12.00"))
        orders.add(Pedido("Francisco", listOf("Burritos", "Refresco"), "8.00"))
        orders.add(Pedido("Justin", listOf("Quesadillas", "Jugo"), "8.00"))
    }
}
