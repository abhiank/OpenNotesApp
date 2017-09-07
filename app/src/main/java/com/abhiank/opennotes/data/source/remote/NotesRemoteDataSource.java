package com.abhiank.opennotes.data.source.remote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.Log;
import android.webkit.URLUtil;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.local.NotesLocalDataSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NotesRemoteDataSource implements NotesDataSource {

    private static NotesRemoteDataSource INSTANCE = null;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private NotesLocalDataSource notesLocalDataSource;

    private static final String NOTES_CHILD_REF = "notes";

    private NotesRemoteDataSource(Context context) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        notesLocalDataSource = NotesLocalDataSource.getInstance(context);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
    }

    public static NotesRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NotesRemoteDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getAllNotes(@NonNull final LoadAllNotesCallback callback) {
        mDatabase.child(NOTES_CHILD_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                List<Note> noteList = new ArrayList<Note>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    noteList.add(snapshot.getValue(Note.class));
                }

                callback.onNotesLoaded(noteList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull final GetNoteCallBack callBack) {
        mDatabase.child(NOTES_CHILD_REF).child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                callBack.onNoteLoaded(note);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveNote(@NonNull final Note note) {

        String content = note.getContent();
        Matcher m = Pattern.compile("!\\[[^\\]]+\\]\\([^!]+\\)\\!").matcher(content);
        while (m.find()) {
            Log.i("regex", m.group());
            String s = m.group();
            final String filePath = s.substring(s.indexOf("(") + 1, s.length() - 2);
            Log.i("filepath", s);

            if (!URLUtil.isNetworkUrl(filePath)) {
                //upload to firebase
                Uri file = Uri.fromFile(new File(filePath));
                StorageReference riversRef = storage.getReference().child("images/" + UUID.randomUUID().toString());
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        mDatabase.child(NOTES_CHILD_REF).child(note.getmId()).setValue(note);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        note.setContent(note.getContent().replace(filePath, downloadUrl.toString()));
                        mDatabase.child(NOTES_CHILD_REF).child(note.getmId()).setValue(note);
                        notesLocalDataSource.saveNote(note);
                    }
                });
            }
        }

        mDatabase.child(NOTES_CHILD_REF).child(note.getmId()).setValue(note);
    }

    @Override
    public void deleteNote(@NonNull Note note) {
        mDatabase.child(NOTES_CHILD_REF).child(note.getmId()).removeValue();

        String content = note.getContent();
        Matcher m = Pattern.compile("!\\[[^\\]]+\\]\\([^!]+\\)\\!").matcher(content);
        while (m.find()) {
            Log.i("regex", m.group());
            String s = m.group();
            final String filePath = s.substring(s.indexOf("(") + 1, s.length() - 2);
            Log.i("filepath", filePath);

            if (URLUtil.isNetworkUrl(filePath)) {
                StorageReference fileRef = storage.getReferenceFromUrl(filePath);
                fileRef.delete();
            }
        }
    }
}
