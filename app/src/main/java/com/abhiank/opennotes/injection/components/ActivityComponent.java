package com.abhiank.opennotes.injection.components;

import com.abhiank.opennotes.injection.customannotations.ActivityScope;
import com.abhiank.opennotes.injection.modules.ActivityModule;
import com.abhiank.opennotes.injection.modules.PresenterModule;
import com.abhiank.opennotes.noteedit.AddEditNoteActivity;
import com.abhiank.opennotes.notelist.NoteListActivity;

import dagger.Component;

/**
 * Created by abhimanyu on 17/11/17.
 */

@ActivityScope
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, PresenterModule.class})
public interface ActivityComponent {

    void inject(AddEditNoteActivity addEditNoteActivity);

    void inject(NoteListActivity noteListActivity);
}
