package com.abhiank.opennotes.notelist;

import com.abhiank.opennotes.data.model.Note;

import java.util.List;

/**
 * Created by abhimanyu on 10/11/17.
 */

public interface NoteListContract {

    interface NoteListPresenter {

//    void addNewNoteClicked();

        void onStart();

        void logout();

        void onNoteItemClicked(String noteId);

        void onNoteItemRemoveClicked(String noteId);
    }

    interface NoteListView {

        void showProgress();

        void hideProgress();

        void showNoteListItems(List<Note> noteList);

        void showErrorMessage(String message);

        void showNote(Note note);

        void removeNoteFromList(String noteId);
    }
}
