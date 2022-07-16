package com.example.notes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Note implements Parcelable {

    private String noteName;
    private String noteDescription;
    private String noteDate;

    public Note(String noteName, String noteDescription, String noteDate) {
        this.noteName = noteName;
        this.noteDescription = noteDescription;
        this.noteDate = noteDate;
    }

    protected Note(Parcel in) {
        noteName = in.readString();
        noteDescription = in.readString();
        noteDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getNoteName());
        dest.writeString(getNoteDescription());
        dest.writeString(getNoteDate());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getNoteName() {
        return noteName;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public String getNoteDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        noteDate = (new StringBuilder()
                .append(day).append(".").append(month + 1).append(".")
                .append(year)).toString();
        return noteDate;
    }
}