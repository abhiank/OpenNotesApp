package com.abhiank.opennotes.noteedit;

import com.abhiank.opennotes.data.Note;

/**
 * Created by abhimanyu on 06/09/17.
 */

public interface AddEditNotePresenter {

    void onResume();

    void saveNote(String title, String content);

    void attachPhoto();

}
