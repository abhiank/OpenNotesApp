package com.abhiank.opennotes.noteedit;

import com.abhiank.opennotes.data.Note;

/**
 * Created by abhimanyu on 06/09/17.
 */

public interface AddEditNoteView {

    void showMessage(String message);

    void setData(Note note);

    void noteSaved();

}
