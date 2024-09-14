package com.example.restaurantemexicano

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MenuActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var selectItemsButton: Button
    private lateinit var orderHistoryButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()

        selectItemsButton = findViewById(R.id.select_items_button)
        orderHistoryButton = findViewById(R.id.order_history_button)
        logoutButton = findViewById(R.id.logout_button)

        selectItemsButton.setOnClickListener {
            startActivity(Intent(this, SelectItemsActivity::class.java))
        }

        orderHistoryButton.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
