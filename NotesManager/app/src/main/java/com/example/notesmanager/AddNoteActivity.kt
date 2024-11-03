package com.example.notesmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var buttonSave: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        buttonSave = findViewById(R.id.buttonSave)

        database = FirebaseDatabase.getInstance().getReference("notes")

        buttonSave.setOnClickListener {
            addNote()
        }
    }

    private fun addNote() {
        val title = editTextTitle.text.toString()
        val content = editTextContent.text.toString()
        val createdAt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val noteId = database.push().key ?: return
            val note = Note(noteId, title, content, createdAt)

            database.child(noteId).setValue(note).addOnCompleteListener {
                Toast.makeText(this, "Ghi chú đã được thêm", Toast.LENGTH_SHORT).show()
                finish() // Đóng Activity sau khi lưu ghi chú
            }.addOnFailureListener {
                Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }
    }
}
