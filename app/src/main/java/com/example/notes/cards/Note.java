package com.example.notes.cards;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Note implements Parcelable {

    private String mNoteName;
    private String mNoteDescription;
    private String mNoteDate;

    public Note(String noteName, String noteDescription) {
        mNoteName = noteName;
        mNoteDescription = noteDescription;
    }

    protected Note(Parcel in) {
        mNoteName = in.readString();
        mNoteDescription = in.readString();
        mNoteDate = in.readString();
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
        return mNoteName;
    }

    public String getNoteDescription() {
        return mNoteDescription;
    }

    public String getNoteDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mNoteDate = day + "." + (month + 1) + "." + year;
        return mNoteDate;
    }

    public void setNoteName(String noteName) {
        mNoteName = noteName;
    }

    public void setNoteDescription(String noteDescription) {
        mNoteDescription = noteDescription;
    }
}