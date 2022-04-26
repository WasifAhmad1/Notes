package com.example.notes;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class Note implements Serializable, Comparable<Note> {

    private final String title;
    //private Date date;
    private final String desc;
    private final UUID id;
    private Date date;

    public Note(String title, String desc, UUID id, Date date) {
        this.title = title;
        //this.date = date;
        this.desc = desc;
        this.id = id;
        this.date = date;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return(formatter.format(date));

    }

    public String getTitle() {
        return title;
    }

    /*public String getTimeStamp() {
        long time = date.getTime();
        String s = String.valueOf(date);
        return s;
    } */

    public String getDesc() {
        return desc;
    }

    public String getID() {
        String id2= id.toString();
        return id2;
    }

    public String toString() {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("desc").value(getDesc());
            jsonWriter.name("id").value(getID());
            jsonWriter.name("date").value(getDate());
            //jsonWriter.name("date").value(getTimeStamp());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    @Override
    public int compareTo(Note t1) {
        return 0;
    }
  static class NoteComparator implements Comparator<Note> {

      @Override
      public int compare(Note note, Note t1) {
          return t1.getDate().compareTo(note.getDate());
      }
  }
}
