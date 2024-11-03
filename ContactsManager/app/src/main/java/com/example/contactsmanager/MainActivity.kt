package com.example.contactsmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseDatabaseHelper: FirebaseDatabaseHelper
    private lateinit var contactListView: ListView
    private lateinit var buttonAddContact: Button
    private lateinit var contactsList: List<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseDatabaseHelper = FirebaseDatabaseHelper()
        contactListView = findViewById(R.id.contactListView)
        buttonAddContact = findViewById(R.id.buttonAddContact)

        buttonAddContact.setOnClickListener {
            val intent = Intent(this, ContactDetailActivity::class.java)
            startActivity(intent)
        }

        loadContacts()
    }

    private fun loadContacts() {
        firebaseDatabaseHelper.getContacts { contacts ->
            contactsList = contacts
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList.map { it.name })
            contactListView.adapter = adapter

            contactListView.setOnItemClickListener { _, _, position, _ ->
                // Mở ContactDetailActivity với ID liên hệ
                val selectedContact = contactsList[position]
                val intent = Intent(this, ContactDetailActivity::class.java)
                intent.putExtra("CONTACT_ID", selectedContact.id)
                startActivity(intent) // Chuyển đến activity xem chi tiết
            }
        }
    }




}
