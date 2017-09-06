package com.abhiank.opennotes.noteedit;

import android.content.Context;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.NotesRepository;

import java.util.UUID;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class AddEditNotePresenterImpl implements AddEditNotePresenter {

    private AddEditNoteView addEditNoteView;
    private Note note;
    private NotesRepository notesRepository;

    public AddEditNotePresenterImpl(AddEditNoteView _addEditNoteView, String noteId, Context context) {
        this.addEditNoteView = _addEditNoteView;
        notesRepository = NotesRepository.getInstance(context);

        if (noteId != null) {
            notesRepository.getNote(noteId, new NotesDataSource.GetNoteCallBack() {
                @Override
                public void onNoteLoaded(Note note1) {
                    note = note1;
                    addEditNoteView.setData(note);
                }

                @Override
                public void onDataNotAvailable() {
                    addEditNoteView.showMessage("Data not found");
                }
            });
        }
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
    public void attachPhoto() {
        addEditNoteView.showMessage("attaching photo");
    }
}
