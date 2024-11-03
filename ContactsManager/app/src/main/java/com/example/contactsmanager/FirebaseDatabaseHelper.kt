package com.example.contactsmanager

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseDatabaseHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("contacts")

    fun addContact(contact: Contact) {
        val contactId = database.push().key
        if (contactId != null) {
            contact.id = contactId
            database.child(contactId).setValue(contact)
        }
    }

    fun getContacts(callback: (List<Contact>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contactsList = mutableListOf<Contact>()
                for (contactSnapshot in snapshot.children) {
                    val contact = contactSnapshot.getValue(Contact::class.java)
                    contact?.let { contactsList.add(it) }
                }
                callback(contactsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    fun updateContact(contact: Contact) {
        database.child(contact.id).setValue(contact)
    }

    fun deleteContact(contactId: String) {
        database.child(contactId).removeValue()
    }

    fun getContact(contactId: String, callback: (Contact?) -> Unit) {
        database.child(contactId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val contact = snapshot.getValue(Contact::class.java)
                callback(contact)
            } else {
                callback(null)
            }
        }.addOnFailureListener {
            // Handle error here
        }
    }
}
