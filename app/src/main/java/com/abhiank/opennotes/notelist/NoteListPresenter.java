package com.abhiank.opennotes.notelist;

/**
 * Created by abhimanyu on 06/09/17.
 */

public interface NoteListPresenter {

    void addNewNoteClicked();

    void onResume();

    void logout();

    void onNoteItemClicked(int position);

    void onNoteItemRemoved(int position);
}
