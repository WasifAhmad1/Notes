package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    //final means that you cannot change the reference the list points to
    private static final String TAG = "MainActivity";
    private final ArrayList<Note> noteList = new ArrayList<>();
    private ArrayList<Note> noteCopy = new ArrayList<>();
    private RecyclerView recyclerView;
    //private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setOnLongClickListener(this);

        noteAdapter noteAdapter = new noteAdapter(noteList, this);

        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*AlertDialog builder = new AlertDialog.Builder(this).setTitle("Title")
                .setMessage("Example Message").setPositiveButton("ok", null)
                .setNegativeButton("no", null).show(); */
    //This will load up the notes. It checks for any duplicates in the list that are generated when switching the orientation
        //layout of the main view. It also sorts the list in descending order with the most recent note first
        try {
            noteList.addAll(loadNotes());
            if(noteList.size()>1) {
                Collections.sort(noteList, new Note.NoteComparator());
            }
            if(noteList.size()>1) {
                Set<Note> set = new LinkedHashSet<Note>();
                set.addAll(noteList);
                noteList.clear();
                noteList.addAll(set);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }



//The following code handles the intent message it gets from the AddNote activity based on whether it is a new note or
        //existing note.
        if(getIntent().hasExtra("Notes")) {

            //ArrayList<Note> l = (ArrayList<Note>) getIntent().getSerializableExtra("Notes");
            Note n = (Note)getIntent().getSerializableExtra("Notes");
           /* for (int i = 0; i < l.size(); i++) {
                String title = l.get(i).getTitle();
                //String dateInString = "7-Nov-2021";
                String desc = l.get(i).getDesc();
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                Date date2 = format.parse(dateInString);
                String id = l.get(i).getID();
                String time = l.get(i).getDate();

                UUID uuid = UUID.fromString(id);
                System.out.println ("Original id is " + id);
                //Note note = new Note(title, desc, uuid, date);
                noteList.add(l.get(i));
                //saveProduct();
                //noteCopy.add(l.get(i));
            }  */
            noteList.add(n);
            if(noteList.size()>1) {
                Collections.sort(noteList, new Note.NoteComparator());
            }
        }

            else if(getIntent().hasExtra("EditNotes")) {
                Toast.makeText(this, "Text edited", Toast.LENGTH_SHORT).show();
                //ArrayList<Note> l = (ArrayList<Note>) getIntent().getSerializableExtra("EditNotes");
                Note b = (Note)getIntent().getSerializableExtra("EditNotes");
                int m = 0;
                   /* for (int i = 0; i < l.size(); i++) {
                        String title = l.get(i).getTitle();
                //String dateInString = "7-Nov-2021";
                        String desc = l.get(i).getDesc();
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                Date date2 = format.parse(dateInString);
                        String id = l.get(i).getID();
                        String id2 = getIntent().getStringExtra("ID2");
                        UUID uuid = UUID.fromString(id);
                        //Note note = new Note(title, desc, uuid, date);

                        System.out.println("Edited id is " + ref.getID()); */
                Note ref = b;
                        /*try {
                            noteList.addAll(loadNotes());
                            System.out.println("The size is at this point " + noteList.size());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } */


                        for (Note note:noteList){

                            if(note.getID().equals(ref.getID())) {
                                noteCopy.add(note);
                                System.out.println("in here");
                                //noteList.remove(note);
                                //saveProduct();
                                //noteList.add(ref);
                            }
                        }
                        //System.out.println("With ref" + ref.getTitle());
                        noteList.removeAll(noteCopy);
                        //saveProduct();
                        noteList.add(ref);
            if(noteList.size()>1) {
                Collections.sort(noteList, new Note.NoteComparator());
            }
                        System.out.println("The size at this point is " + noteList.size());
                        saveProduct();



            }
            setTitle("Multi Notes (" +noteList.size() + ")");




        }




    public static Comparator<Note> NoteComparator = new Comparator<Note>()
    {
        @Override
        public int compare(Note t1, Note t2) {
            String s1 = t1.getDate();
            String s2 = t2.getDate();
            if (s1 == s2) {return 0;}
            if (s1 == null) {return -1;}
            if (s2 == null) {return 1;}
            return s1.compareTo(s2);
        }

    };

    @Override
    protected void onPause() {
        //saves the list to the json file

        Toast.makeText(this, "on pause", Toast.LENGTH_SHORT).show();
        System.out.println("On pause the size is " + noteList.size());
        //for(Note note: noteCopy)
            //noteList.add(note);
        saveProduct();
        super.onPause();
    }

    private void saveProduct() {
        Log.d(TAG, "saveProduct: Saving JSON File");

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput("Notes5.json", Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(noteList);
            System.out.println(" In save product" + printWriter.toString());
            printWriter.close();
            fos.close();


            Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    protected void onResume() {
        //The resume method does not really call any methods or functions. I previously had it calling the loadNotes
        //function but found this function was better suited to be called by onCreate
        System.out.println("On resume the size is " + noteList.size());
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        /*try {
            //noteList.clear();
            //noteList.addAll(loadNotes());
        } catch (IOException e) {
            e.printStackTrace();
        } */


        super.onResume();
    }

    private ArrayList<Note> loadNotes() throws IOException {
        ArrayList<Note> noteList2 = new ArrayList<>();

        try{

            InputStream is = getApplicationContext().openFileInput("Notes5.json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());

            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String desc = jsonObject.getString("desc");
                String id = jsonObject.getString("id");
                String date =jsonObject.getString("date");
                UUID uuid = UUID.fromString(id);
                /*String date = jsonObject.getString("date");
                Date date2 = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                String dateInString = date;
                date2 = format.parse(dateInString); */
                Date date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
                Note note = new Note(title/*, date2*/, desc, uuid, date2);
                //noteList.add(note);
                noteList2.add(note);

            }


        } catch (FileNotFoundException | JSONException | ParseException e) {
            e.printStackTrace();
        }
        return noteList2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Generates the menu layout for this activity
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        //method for onClick to trigger editing the notes
        int pos = recyclerView.getChildLayoutPosition(view);
        Note n = noteList.get(pos);
        Intent intent = new Intent(this, AddNote.class);
        String title = n.getTitle();
        String desc = n.getDesc();
        String id = n.getID();
        intent.putExtra("EditTitle", title);
        intent.putExtra("EditDesc", desc);
        intent.putExtra("ID", id);


        startActivity(intent);
        //Toast.makeText(this, "on click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View view) {
        //this function will generate the dialog box as to whether the note will be deleted ot not

        int pos = recyclerView.getChildLayoutPosition(view);
        Note n = noteList.get(pos);
        //AlertDialog dialog = new AlertDialog.Builder(this);
        /*AlertDialog builder = new AlertDialog.Builder(this).setTitle("Delete")
                .setMessage("Do you want to delete the data").setPositiveButton("ok", null)
                .setNegativeButton("no", null).show(); */
        Builder builder2 = new Builder(this);
        builder2.setTitle("Delete data");
        builder2.setMessage("Do you want to delete the data?");


        builder2.setPositiveButton("Yes", (dialog, id) -> {
                noteList.remove(n);
                saveProduct();
                //I ended up having to place the following lines of code because I ran into an issue where I was
            //deleting an object but it would not reflect on the main screen unless it exited out and re-started the app
            //adding the code helped remedy this issue

                noteAdapter noteAdapter = new noteAdapter(noteList, this);
                recyclerView.setAdapter(noteAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                setTitle("Multi Notes (" +noteList.size() + ")");
        });

        //builder2.setNegativeButton("No", (dialog, id) -> finish());
        builder2.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder2.show();

        //System.out.println("The description is " + n.getDesc());
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Toast.makeText(this, "onLongClick", Toast.LENGTH_SHORT).show();

        return true; //should we not proceed with the onClick? Is this action complete?
        // If we were to return true it will execute the action and not call any other listeners. If false it may call other listeners
    }



    /*public void openNextActivity(View v) {
        //this refers to the button we put in the toolbar. It will open up a new activity to add to a note
        //we have to create ab object in the process of opening a new activity. This is called an intent which is for what
        //we want to do
        Intent intent = new Intent(this, AddNote.class);
        startActivity(intent);
    } */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //This will generate some sort of activity based on the menu option clicked

        int id = item.getItemId();
        if(id== R.id.add) {
            Intent intent = new Intent(this, AddNote.class);
            startActivity(intent);
            return true;
        }

        if(id== R.id.info) {
            Intent intent = new Intent(this, AboutPage.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //required for a onBackPressed function but this is the main activity and pressing back will exit the app and place
        //it on pause so I did not really need to add anything.
    }


    }
