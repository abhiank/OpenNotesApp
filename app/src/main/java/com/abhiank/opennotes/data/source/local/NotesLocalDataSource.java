package com.abhiank.opennotes.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.abhiank.opennotes.data.model.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.injection.customannotations.ApplicationContext;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NotesLocalDataSource implements NotesDataSource {

    private Realm realm;

    @Inject
    public NotesLocalDataSource(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void getAllNotes(@NonNull LoadAllNotesCallback callback) {
        List<Note> noteList = realm.copyFromRealm(realm.where(Note.class).findAll());

        if (noteList == null || noteList.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onNotesLoaded(noteList);
        }
    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallBack callBack) {
        Note note = realm.where(Note.class).equalTo("mId", noteId).findFirst();
        if (note != null) {
            callBack.onNoteLoaded(realm.copyFromRealm(note));
        } else {
            callBack.onDataNotAvailable();
        }
    }

    @Override
    public void saveNote(@NonNull Note note) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    @Override
    public void deleteNote(@NonNull String noteId) {
        realm.beginTransaction();
        Note note1 = realm.where(Note.class).equalTo("mId", noteId).findFirst();
        if (note1 != null) {
            note1.deleteFromRealm();
        }
        realm.commitTransaction();
    }

}
