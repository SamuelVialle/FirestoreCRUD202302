package com.samuelvialle.firestorecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    /** Initialisations des composants graphiques */
    private EditText etNoteTitle, etNoteDescription;
    private Button btnSaveNote, btnShowAllNotes;

    private void initUI(){
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnShowAllNotes = findViewById(R.id.btnShowAllNotes);
    }

    /** Initialisation de Firebase Firestore */
    private FirebaseFirestore db;
    private void initFirebaseTools(){
        db = FirebaseFirestore.getInstance();
    }

    /** Les méthodes persos */
    // 3 VARS Globales pour les datas du formulaire
    private String uId, uTitle, uDesc;

    private void saveButton(){
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etNoteTitle.getText().toString();
                String desc = etNoteDescription.getText().toString();
                String id = UUID.randomUUID().toString();
                saveDataToFirestore(id, title, desc);
            }
        });
    }

    private void saveDataToFirestore(String id, String title, String desc) {
        Map<String, Object> note = new HashMap<>();
        note.put("id", id);
        note.put("title", title);
        note.put("desc", desc);

// Add a new document with a generated ID
        db.collection("Notes").document(id).set(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Note Saved !", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed" + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /** Début des méthodes de l'ACV */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initFirebaseTools();
        saveButton();
    }
}