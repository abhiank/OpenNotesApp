package com.abhiank.opennotes.noteedit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abhiank.opennotes.R;
import com.abhiank.opennotes.domain.Note;

import org.parceler.Parcel;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditNoteActivity extends AppCompatActivity implements AddEditNoteView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scroll_view_linear_layout)
    LinearLayout noteLinearLayout;

    private AddEditNotePresenterImpl presenter;

    private static final String NOTE_INTENT_EXTRA_TAG = "note";

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, AddEditNoteActivity.class);
    }

    public static Intent getActivityIntent(Context context, Note note) {
        Intent intent = new Intent(context, AddEditNoteActivity.class);
        intent.putExtra(NOTE_INTENT_EXTRA_TAG, Parcels.wrap(note));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ButterKnife.bind(AddEditNoteActivity.this);

        Note note = null;
        if (getIntent().getExtras() != null) {
            note = Parcels.unwrap(getIntent().getExtras().getParcelable(NOTE_INTENT_EXTRA_TAG));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title;
        if (note == null) {
            title = "Create Note";
        } else {
            title = "Edit Note";
        }
        getSupportActionBar().setTitle(title);

        presenter = new AddEditNotePresenterImpl(AddEditNoteActivity.this, note);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attach:
                presenter.attachPhoto();

                LinearLayout.LayoutParams imParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ImageView imSex = new ImageView(AddEditNoteActivity.this);
                imSex.setImageResource(R.drawable.abcd);
                imSex.setScaleType(ImageView.ScaleType.FIT_XY);
                EditText editText = new EditText(AddEditNoteActivity.this);
                editText.setBackgroundColor(Color.BLUE);
                noteLinearLayout.addView(imSex,imParams);
                noteLinearLayout.addView(editText,imParams);

                return true;

            case R.id.save:
                presenter.saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(AddEditNoteActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
