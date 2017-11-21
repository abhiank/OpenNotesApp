package com.abhiank.opennotes.noteedit;

import android.content.Context;

import com.abhiank.opennotes.data.model.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.NotesRepository;

import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class AddEditNotePresenter implements AddEditNoteContract.Presenter {

    private AddEditNoteContract.View addEditNoteView;
    private Note note;
    private NotesRepository notesRepository;

    @Inject
    public AddEditNotePresenter(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void saveNote(String title, String content) {
        String message = "Note Updated";
        if (note == null) {
            note = new Note();
            note.setmId(UUID.randomUUID().toString());
            message = "Note Saved";
        }
        note.setTitle(title);
        note.setContent(content);
        notesRepository.saveNote(note);
        addEditNoteView.showMessage(message);
        addEditNoteView.noteSaved();
    }

    @Override
    public void getNote(String noteId) {
        if (noteId != null) {
            notesRepository.getNote(noteId, new NotesDataSource.GetNoteCallBack() {
                @Override
                public void onNoteLoaded(Note note1) {
                    note = note1;
                    AddEditNotePresenter.this.addEditNoteView.setData(note);
                }

                @Override
                public void onDataNotAvailable() {
                    AddEditNotePresenter.this.addEditNoteView.showMessage("Data not found");
                }
            });
        }
    }

    @Override
    public void attachPhoto() {
        addEditNoteView.showMessage("attaching photo");
    }

    @Override
    public void attachView(AddEditNoteContract.View view) {
        if (view != null) {
            addEditNoteView = view;
        }
    }
}
