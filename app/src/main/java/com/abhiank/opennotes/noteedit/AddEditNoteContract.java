package com.abhiank.opennotes.noteedit;

import com.abhiank.opennotes.data.model.Note;
import com.abhiank.opennotes.notelist.NoteListContract;

/**
 * Created by abhimanyu on 17/11/17.
 */

public interface AddEditNoteContract {

    interface Presenter {

        void onResume();

        void saveNote(String title, String content);

        void getNote(String noteId);

        void attachPhoto();

        void attachView(AddEditNoteContract.View view);
    }

    interface View {

        void showMessage(String message);

        void setData(Note note);

        void noteSaved();
    }

}
