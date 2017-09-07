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
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abhiank.opennotes.R;
import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditNoteActivity extends AppCompatActivity implements AddEditNoteView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scroll_view_linear_layout)
    LinearLayout noteLinearLayout;
    @BindView(R.id.note_edit_text)
    EditText contentEditText;
    @BindView(R.id.title_edittext)
    EditText titleEditText;

    private static final String TAG = AddEditNoteActivity.class.getSimpleName();
    private static final int IMAGE_PICKER_INTENT_REQUEST_CODE = 0;

    private AddEditNotePresenterImpl presenter;

    private static final String NOTE_ID_INTENT_EXTRA_TAG = "note_id";

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, AddEditNoteActivity.class);
    }

    public static Intent getActivityIntent(Context context, String noteId) {
        Intent intent = new Intent(context, AddEditNoteActivity.class);
        intent.putExtra(NOTE_ID_INTENT_EXTRA_TAG, noteId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ButterKnife.bind(AddEditNoteActivity.this);

        String noteId = null;
        if (getIntent().getExtras() != null)
            noteId = getIntent().getExtras().getString(NOTE_ID_INTENT_EXTRA_TAG);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.create_note_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new AddEditNotePresenterImpl(AddEditNoteActivity.this, noteId, getApplicationContext());

        contentEditText.addTextChangedListener(new ImageSpanDeleteHandler(contentEditText));
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
                validateData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void validateData() {

        if (TextUtils.isEmpty(titleEditText.getText())) {
            titleEditText.setError(getString(R.string.title_empty_error_message));
            return;
        }
        if (TextUtils.isEmpty(contentEditText.getText())) {
            contentEditText.setError(getString(R.string.content_field_empty_error_message));
            return;
        }
        presenter.saveNote(titleEditText.getText().toString(), contentEditText.getText().toString());
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
                        Toast.makeText(AddEditNoteActivity.this, R.string.storage_permission_denied_message, Toast.LENGTH_SHORT).show();
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
    public void setData(Note note) {

        if (note != null) {
            getSupportActionBar().setTitle(R.string.edit_note_title);
            titleEditText.setText(note.getTitle());

            String content = note.getContent();
            SpannableString ss = new SpannableString(content);

            Matcher m = Pattern.compile("!\\[[^\\]]+\\]\\([^!]+\\)\\!").matcher(content);
            while (m.find()) {
                Log.i("regex", m.group());
                String s = m.group();
                String filePath = s.substring(s.indexOf("(") + 1, s.length() - 2);
                Log.i("filepath", filePath);

                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                Drawable d = new BitmapDrawable(getResources(), yourSelectedImage);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            }
            contentEditText.setText(ss);
        }
    }

    @Override
    public void noteSaved() {
        onBackPressed();
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
            String fileName = new File(filePath).getName();

            //https://stackoverflow.com/a/4887325/3090120
            //https://stackoverflow.com/questions/3176033/spannablestring-with-image-example
            String s = "![" + fileName + "](" + filePath + ")!\n";
            SpannableString ss = new SpannableString(s);
            Drawable d = new BitmapDrawable(getResources(), yourSelectedImage);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, s.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            contentEditText.append(ss);

            Log.i(TAG, contentEditText.getText().toString());
        }
    }

    //https://stackoverflow.com/a/19649371/3090120
    private static class ImageSpanDeleteHandler implements TextWatcher {

        private final EditText mEditor;
        private final ArrayList<ImageSpan> mEmoticonsToRemove = new ArrayList<ImageSpan>();

        public ImageSpanDeleteHandler(EditText editor) {
            // Attach the handler to listen for text changes.
            mEditor = editor;
        }

        public void insert(String emoticon, int resource) {
            // Create the ImageSpan
            Drawable drawable = mEditor.getResources().getDrawable(resource);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);

            // Get the selected text.
            int start = mEditor.getSelectionStart();
            int end = mEditor.getSelectionEnd();
            Editable message = mEditor.getEditableText();

            // Insert the emoticon.
            message.replace(start, end, emoticon);
            message.setSpan(span, start, start + emoticon.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        @Override
        public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            // Check if some text will be removed.
            if (count > 0) {
                int end = start + count;
                Editable message = mEditor.getEditableText();
                ImageSpan[] list = message.getSpans(start, end, ImageSpan.class);

                for (ImageSpan span : list) {
                    // Get only the emoticons that are inside of the changed
                    // region.
                    int spanStart = message.getSpanStart(span);
                    int spanEnd = message.getSpanEnd(span);
                    if ((spanStart < end) && (spanEnd > start)) {
                        // Add to remove list
                        mEmoticonsToRemove.add(span);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable text) {
            Editable message = mEditor.getEditableText();

            // Commit the emoticons to be removed.
            for (ImageSpan span : mEmoticonsToRemove) {
                int start = message.getSpanStart(span);
                int end = message.getSpanEnd(span);

                // Remove the span
                message.removeSpan(span);

                // Remove the remaining emoticon text.
                if (start != end) {
                    message.delete(start, end);
                }
            }
            mEmoticonsToRemove.clear();
        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
        }

    }
}
