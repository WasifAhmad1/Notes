package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AddNote extends AppCompatActivity {

    private EditText title;
    private EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title= findViewById(R.id.input1);
        desc = findViewById(R.id.input2);

        if (getIntent().hasExtra("EditTitle")){
            String editTitle = getIntent().getStringExtra("EditTitle");
            String editDesc = getIntent().getStringExtra("EditDesc");
            title.setText(editTitle);
            desc.setText(editDesc);

        }
    }

    public void sendNote(View v) throws ParseException {
        //This whole method essentially sends the note back to the main activity. I know the ActivityResultLauncher is a better
        //suited technique here but by the time I had heard the professor discussing that method I had already coded most of this
        ArrayList<Note> noteList = new ArrayList<>();
        String getTitle = title.getText().toString();
        String getDesc = desc.getText().toString();

        Date date = new Date();
        //Utilizing an id token helped significantly. It was used to help determine whether a note had already been previously
        //created or if it was new.
        UUID id;
        if(getTitle.isEmpty()){
            Toast.makeText(this, "You need to enter text in the title", Toast.LENGTH_SHORT).show();
        }
        else {
            if (getIntent().hasExtra("ID")) {
                String id2 = getIntent().getStringExtra("ID");
                id = UUID.fromString(id2);
                Note note = new Note(getTitle, getDesc, id, date);
                noteList.add(note);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("EditNotes", note);
                intent.putExtra("ID2", id2);

                startActivity(intent);
            } else {
                id = UUID.randomUUID();
                Note note = new Note(getTitle, getDesc, id, date);
                noteList.add(note);

                Intent intent = new Intent(this, MainActivity.class);
                //need to attach data to the intent to get it to the main activity
        /*intent.putExtra("Title", getTitle);
        intent.putExtra("Desc", getDesc); */
                intent.putExtra("Notes", note);
                startActivity(intent);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addnotes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        View v = item.getActionView();
        if(id== R.id.save) {
            try {
                sendNote(v);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save data");
        builder.setMessage("Do you want to save the data?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            try {
                sendNote(null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("No", (dialog, id) -> finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}