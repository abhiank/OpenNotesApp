package com.abhiank.opennotes.data.model;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by abhimanyu on 06/09/17.
 */

@Parcel
public class Note extends RealmObject {

    @PrimaryKey
    String mId;
    String title;
    String content;

    public Note() {
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Note){
            Note note = (Note) obj;
            return note.getmId().equals(this.getmId());
        }
        else return false;
    }

    @NonNull
    public String getmId() {
        return mId;
    }

    public void setmId(@NonNull String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
