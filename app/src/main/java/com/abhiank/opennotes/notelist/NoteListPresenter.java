package com.abhiank.opennotes.notelist;

import android.content.Context;

import com.abhiank.opennotes.data.model.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.NotesRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NoteListPresenter implements NoteListContract.Presenter {

    private NotesDataSource notesRepository;
    private NoteListContract.View noteListView;

    @Inject
    public NoteListPresenter(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Override
    public void onStart() {
        notesRepository.getAllNotes(new NotesDataSource.LoadAllNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                noteListView.showNoteListItems(notes);
            }

            @Override
            public void onDataNotAvailable() {
                noteListView.showErrorMessage("Data not found");
            }
        });
    }

    @Override
    public void logout() {
        noteListView.showErrorMessage("Logging out");
    }

    @Override
    public void onNoteItemClicked(String id) {
        notesRepository.getNote(id, new NotesDataSource.GetNoteCallBack() {
            @Override
            public void onNoteLoaded(Note note) {
                noteListView.showNote(note);
            }

            @Override
            public void onDataNotAvailable() {
                noteListView.showErrorMessage("Note not found");
            }
        });
    }

    @Override
    public void onNoteItemRemoveClicked(String noteId) {
        notesRepository.deleteNote(noteId);
        noteListView.removeNoteFromList(noteId);
    }

    @Override
    public void attachView(NoteListContract.View view) {
        this.noteListView = view;
    }

}
