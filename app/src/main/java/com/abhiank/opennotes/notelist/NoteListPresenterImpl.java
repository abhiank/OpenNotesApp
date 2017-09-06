package com.abhiank.opennotes.notelist;

import com.abhiank.opennotes.domain.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NoteListPresenterImpl implements NoteListPresenter {

    private NoteListView noteListView;

    public NoteListPresenterImpl(NoteListView noteListView) {
        this.noteListView = noteListView;
    }

    @Override
    public void onResume() {
        List<Note> notes = new ArrayList<>();
        for(int i=0; i<10; i++){
            notes.add(new Note("blah", "blah blah"));
        }
        noteListView.setNoteListItems(notes);
    }

    @Override
    public void logout() {
        noteListView.showMessage("Logging out");
    }

    @Override
    public void onNoteItemClicked(int position) {
        noteListView.showMessage("Note " + position + " clicked");
        noteListView.navigateToAddEditNoteScreen(new Note("a", "b"));
    }

    @Override
    public void onNoteItemRemoved(int position) {
        noteListView.showMessage("Note " + position + " removed");
    }
}
