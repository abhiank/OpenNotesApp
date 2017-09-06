package com.abhiank.opennotes.data.source;

import android.support.annotation.NonNull;

import com.abhiank.opennotes.data.Note;

import java.util.List;

/**
 * Created by abhimanyu on 06/09/17.
 */

public interface NotesDataSource {

    interface LoadAllNotesCallback{

        void onNotesLoaded(List<Note> notes);

        void onDataNotAvailable();
    }

    interface GetNoteCallBack {

        void onNoteLoaded(Note note);

        void onDataNotAvailable();
    }

    void getAllNotes(@NonNull LoadAllNotesCallback callback);

    void getNote(@NonNull long noteId, @NonNull GetNoteCallBack callBack);

    void saveNote(@NonNull Note note);

    void deleteNote(@NonNull Note note);
}
