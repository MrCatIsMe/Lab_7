package com.example.notesmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var database: DatabaseReference
    private lateinit var noteId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonDelete = findViewById(R.id.buttonDelete)

        database = FirebaseDatabase.getInstance().getReference("notes")
        noteId = intent.getStringExtra("NOTE_ID") ?: return // Nhận ID ghi chú từ Intent

        // Tải ghi chú từ Firebase
        loadNoteDetails()

        buttonUpdate.setOnClickListener {
            updateNote()
        }

        buttonDelete.setOnClickListener {
            deleteNote()
        }
    }

    private fun loadNoteDetails() {
        database.child(noteId).get().addOnSuccessListener { snapshot ->
            val note = snapshot.getValue(Note::class.java)
            if (note != null) {
                editTextTitle.setText(note.title)
                editTextContent.setText(note.content)
            } else {
                Toast.makeText(this, "Ghi chú không tồn tại", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Lỗi khi tải ghi chú: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNote() {
        val title = editTextTitle.text.toString()
        val content = editTextContent.text.toString()

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val updatedNote = Note(noteId, title, content)
            database.child(noteId).setValue(updatedNote).addOnCompleteListener {
                Toast.makeText(this, "Ghi chú đã được cập nhật", Toast.LENGTH_SHORT).show()
                finish() // Đóng Activity sau khi cập nhật
            }.addOnFailureListener {
                Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteNote() {
        database.child(noteId).removeValue().addOnCompleteListener {
            Toast.makeText(this, "Ghi chú đã được xóa", Toast.LENGTH_SHORT).show()
            finish() // Đóng Activity sau khi xóa
        }.addOnFailureListener {
            Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
