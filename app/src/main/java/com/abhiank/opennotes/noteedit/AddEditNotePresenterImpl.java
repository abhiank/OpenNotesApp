package com.abhiank.opennotes.noteedit;

import android.content.Context;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.NotesRepository;

import java.util.UUID;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class AddEditNotePresenterImpl implements AddEditNotePresenter {

    private AddEditNoteView addEditNoteView;
    private Note note;
    private NotesRepository notesRepository;

    public AddEditNotePresenterImpl(AddEditNoteView addEditNoteView, Note note, Context context) {
        this.addEditNoteView = addEditNoteView;
        this.note = note;
        notesRepository = NotesRepository.getInstance(context);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void saveNote(String title, String content) {
        addEditNoteView.showMessage("save note");
        if (note == null) {
            note = new Note();
            note.setTitle(title);
            note.setContent(content);
            note.setmId(UUID.randomUUID().toString());
            notesRepository.saveNote(note);
        } else {
            notesRepository.saveNote(note);
        }
    }

    @Override
    public void attachPhoto() {
        addEditNoteView.showMessage("attaching photo");
    }
}
