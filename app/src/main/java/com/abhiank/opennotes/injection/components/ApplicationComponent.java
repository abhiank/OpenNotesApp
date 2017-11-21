package com.abhiank.opennotes.injection.components;

import android.app.Application;
import android.content.Context;

import com.abhiank.opennotes.data.source.NotesRepository;
import com.abhiank.opennotes.injection.customannotations.ApplicationContext;
import com.abhiank.opennotes.injection.modules.ActivityModule;
import com.abhiank.opennotes.injection.modules.ApplicationModule;
import com.abhiank.opennotes.injection.modules.DataModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by abhimanyu on 17/11/17.
 */


@Singleton
@Component(modules = {ApplicationModule.class, DataModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context getContext();

    Application getApplication();

    NotesRepository getNotesRepository();
}
