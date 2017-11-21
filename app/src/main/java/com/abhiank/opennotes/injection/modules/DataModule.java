package com.abhiank.opennotes.injection.modules;

import android.content.Context;

import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.NotesRepository;
import com.abhiank.opennotes.data.source.local.NotesLocalDataSource;
import com.abhiank.opennotes.data.source.remote.NotesRemoteDataSource;
import com.abhiank.opennotes.injection.customannotations.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by abhimanyu on 17/11/17.
 */

@Module
public class DataModule {

    @Provides
    @Singleton
    NotesDataSource provideNotesDataSource(NotesLocalDataSource notesLocalDataSource,
                                           NotesRemoteDataSource notesRemoteDataSource) {
        return new NotesRepository(notesLocalDataSource, notesRemoteDataSource);
    }

    @Provides
    @Singleton
    NotesLocalDataSource provideNotesLocalDataSource(@ApplicationContext Context context) {
        return new NotesLocalDataSource(context);
    }

    @Provides
    @Singleton
    NotesRemoteDataSource provideNotesRemoteDataSource(NotesLocalDataSource notesLocalDataSource) {
        return new NotesRemoteDataSource(notesLocalDataSource);
    }

}
