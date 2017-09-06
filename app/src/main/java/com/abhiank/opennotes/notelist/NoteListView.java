package com.abhiank.opennotes.notelist;

import com.abhiank.opennotes.domain.Note;

import java.util.List;

/**
 * Created by abhimanyu on 06/09/17.
 */

public interface NoteListView {

    void showProgress();

    void hideProgress();

    void setNoteListItems(List<Note> noteList);

    void showMessage(String message);

    void navigateToAddEditNoteScreen(Note note);
}
