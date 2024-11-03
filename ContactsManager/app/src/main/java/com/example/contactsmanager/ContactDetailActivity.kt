package com.example.contactsmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button
    private var contactId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonSave = findViewById(R.id.buttonSave)
        buttonDelete = findViewById(R.id.buttonDelete)

        // Nhận ID của liên hệ từ Intent
        contactId = intent.getStringExtra("CONTACT_ID")
        contactId?.let {
            // Nếu có ID, tải thông tin liên hệ từ Firebase
            loadContactDetails(it)
        }

        buttonSave.setOnClickListener {
            if (contactId != null) {
                saveContact(contactId!!)
            } else {
                Toast.makeText(this, "Contact ID is null", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDelete.setOnClickListener {
            contactId?.let { id -> deleteContact(id) }
        }
    }

    private fun loadContactDetails(id: String) {
        // Tải thông tin liên hệ từ Firebase và hiển thị trong các EditText
        FirebaseDatabase.getInstance().getReference("contacts").child(id)
            .get().addOnSuccessListener { snapshot ->
                val contact = snapshot.getValue(Contact::class.java)
                contact?.let {
                    editTextName.setText(it.name)
                    editTextPhone.setText(it.phoneNumber)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load contact details", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveContact(id: String) {
        // Lưu thông tin liên hệ vào Firebase
        val name = editTextName.text.toString()
        val phone = editTextPhone.text.toString()

        // Kiểm tra các trường đầu vào
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val contact = Contact(id, name, phone)
        FirebaseDatabase.getInstance().getReference("contacts").child(id).setValue(contact)
            .addOnSuccessListener {
                Toast.makeText(this, "Contact updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Quay lại activity trước đó
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to update contact", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteContact(id: String) {
        // Xóa liên hệ khỏi Firebase
        FirebaseDatabase.getInstance().getReference("contacts").child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
                finish() // Quay lại activity trước đó
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to delete contact", Toast.LENGTH_SHORT).show()
            }
    }
}
