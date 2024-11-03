package com.example.notesmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var notesList: MutableList<Note>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("notes")
        notesList = mutableListOf()
        recyclerView = findViewById(R.id.rvNotes)
        noteAdapter = NoteAdapter(notesList) { note -> openNoteDetail(note) }
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAddNote).setOnClickListener {
            addNote()
        }

        getNotes()
    }

    private fun getNotes() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                notesList.clear()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    if (note != null) {
                        notesList.add(note)
                    }
                }
                noteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addNote() {
        // Chuyển đến một Activity mới để nhập ghi chú
        val intent = Intent(this, AddNoteActivity::class.java)
        startActivity(intent)
    }

    private fun openNoteDetail(note: Note) {
        // Chuyển đến một Activity để xem và chỉnh sửa ghi chú
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra("NOTE_ID", note.id) // Gửi ID ghi chú đến Activity chi tiết
        startActivity(intent)
    }

}
