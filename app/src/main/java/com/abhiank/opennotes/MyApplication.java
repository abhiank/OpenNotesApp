package com.abhiank.opennotes;

import android.app.Application;
import android.content.Context;

import com.abhiank.opennotes.injection.components.ApplicationComponent;
import com.abhiank.opennotes.injection.components.DaggerApplicationComponent;
import com.abhiank.opennotes.injection.modules.ApplicationModule;

/**
 * Created by abhimanyu on 17/11/17.
 */

public class MyApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static MyApplication get(Context context){
        return ((MyApplication)context.getApplicationContext());
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
