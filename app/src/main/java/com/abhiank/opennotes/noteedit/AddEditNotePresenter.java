package com.abhiank.opennotes.noteedit;

/**
 * Created by abhimanyu on 06/09/17.
 */

public interface AddEditNotePresenter {

    void onResume();

    void saveNote(String title, String content);

    void attachPhoto();

}
