package com.abhiank.opennotes.injection.modules;

import com.abhiank.opennotes.data.source.NotesRepository;
import com.abhiank.opennotes.noteedit.AddEditNoteContract;
import com.abhiank.opennotes.noteedit.AddEditNotePresenter;
import com.abhiank.opennotes.notelist.NoteListContract;
import com.abhiank.opennotes.notelist.NoteListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by abhimanyu on 21/11/17.
 */

@Module
public class PresenterModule {

    @Provides
    NoteListContract.Presenter providesNoteListPresenter(NotesRepository noteRepository){
        return new NoteListPresenter(noteRepository);
    }

    @Provides
    AddEditNoteContract.Presenter provideAddEditNotePresenter(NotesRepository notesRepository){
        return new AddEditNotePresenter(notesRepository);
    }
}
