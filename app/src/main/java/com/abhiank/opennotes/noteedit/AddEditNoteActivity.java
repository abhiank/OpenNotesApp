package com.abhiank.opennotes.noteedit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abhiank.opennotes.R;
import com.abhiank.opennotes.domain.Note;
import com.abhiank.opennotes.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditNoteActivity extends AppCompatActivity implements AddEditNoteView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scroll_view_linear_layout)
    LinearLayout noteLinearLayout;
    @BindView(R.id.note_edit_text)
    EditText editText;

    private static final int IMAGE_PICKER_INTENT_REQUEST_CODE = 0;

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
                attachImage();
                return true;

            case R.id.save:
                presenter.saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void attachImage() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivityForResult(Utils.getImagePickerIntent(), IMAGE_PICKER_INTENT_REQUEST_CODE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(AddEditNoteActivity.this, "Storage permission was denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(AddEditNoteActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == IMAGE_PICKER_INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (imageReturnedIntent == null) {
                //Display an error
                return;
            }

            Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

            SpannableString ss = new SpannableString("abc\n");
            Drawable d = new BitmapDrawable(getResources(), yourSelectedImage);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            editText.append(ss);
        }
    }
}
