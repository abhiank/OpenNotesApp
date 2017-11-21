package com.abhiank.opennotes;

import android.support.v7.app.AppCompatActivity;

import com.abhiank.opennotes.injection.components.ActivityComponent;
import com.abhiank.opennotes.injection.components.DaggerActivityComponent;
import com.abhiank.opennotes.injection.modules.ActivityModule;
import com.abhiank.opennotes.injection.modules.PresenterModule;

/**
 * Created by abhimanyu on 21/11/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityComponent getActivityComponent(){
        return DaggerActivityComponent
                .builder()
                .applicationComponent(MyApplication.get(this).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .presenterModule(new PresenterModule())
                .build();
    }

}
