package com.abhiank.opennotes.notelist;

import android.content.Context;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.NotesRepository;

import java.util.List;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NoteListPresenterImpl implements NoteListPresenter {

    private NoteListView noteListView;
    private NotesRepository notesRepository;
    private List<Note> noteList;

    public NoteListPresenterImpl(NoteListView noteListView, Context context) {
        this.noteListView = noteListView;
        notesRepository = NotesRepository.getInstance(context);
    }

    @Override
    public void onResume() {
        notesRepository.getAllNotes(new NotesDataSource.LoadAllNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                noteList = notes;
                noteListView.setNoteListItems(noteList);
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
        noteListView.navigateToAddEditNoteScreen(noteList.get(position));
    }

    @Override
    public void onNoteItemRemoveClicked(int position) {
        notesRepository.deleteNote(noteList.get(position));
        noteList.remove(position);
        noteListView.noteRemoved(position);
    }

}
