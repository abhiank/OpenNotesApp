package com.abhiank.opennotes.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.local.NotesLocalDataSource;

import java.util.List;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NotesRepository implements NotesDataSource {

    private static NotesRepository INSTANCE = null;

    private NotesLocalDataSource notesLocalDataSource;

    private NotesRepository(Context context) {
        notesLocalDataSource = NotesLocalDataSource.getInstance(context);
    }

    public static NotesRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NotesRepository(context);
        }
        return INSTANCE;
    }


    @Override
    public void getAllNotes(@NonNull final LoadAllNotesCallback callback) {
        notesLocalDataSource.getAllNotes(new LoadAllNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                callback.onNotesLoaded(notes);
            }

            @Override
            public void onDataNotAvailable() {
                //todo get from firebase
            }
        });
    }

    @Override
    public void getNote(@NonNull long noteId, @NonNull final GetNoteCallBack callBack) {
        notesLocalDataSource.getNote(noteId, new GetNoteCallBack() {
            @Override
            public void onNoteLoaded(Note note) {
                callBack.onNoteLoaded(note);
            }

            @Override
            public void onDataNotAvailable() {
                //// TODO: 06/09/17 something
            }
        });
    }

    @Override
    public void saveNote(@NonNull Note note) {
        notesLocalDataSource.saveNote(note);
    }

    @Override
    public void deleteNote(@NonNull Note note) {
        notesLocalDataSource.deleteNote(note);
    }
}
