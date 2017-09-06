package com.abhiank.opennotes.noteedit;

import com.abhiank.opennotes.domain.Note;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class AddEditNotePresenterImpl implements AddEditNotePresenter {

    private AddEditNoteView addEditNoteView;
    private Note note;

    public AddEditNotePresenterImpl(AddEditNoteView addEditNoteView, Note note) {
        this.addEditNoteView = addEditNoteView;
        this.note = note;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void saveNote() {
        addEditNoteView.showMessage("save note");
    }

    @Override
    public void attachPhoto() {
        addEditNoteView.showMessage("attaching photo");
    }
}
