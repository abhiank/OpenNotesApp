package com.abhiank.opennotes.notelist;

import android.content.Context;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.NotesRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NoteListPresenterImpl implements NoteListPresenter {

    private NoteListView noteListView;
    private NotesRepository notesRepository;

    public NoteListPresenterImpl(NoteListView noteListView, Context context) {
        this.noteListView = noteListView;
        notesRepository = NotesRepository.getInstance(context);
    }

    @Override
    public void onResume() {
//        List<Note> notes = new ArrayList<>();
//        for(int i=0; i<10; i++){
//            notes.add(new Note("blah", "blah blah"));
//        }

        notesRepository.getAllNotes(new NotesDataSource.LoadAllNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                noteListView.setNoteListItems(notes);
            }

            @Override
            public void onDataNotAvailable() {
                noteListView.showMessage("Data not found");
            }
        });

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
