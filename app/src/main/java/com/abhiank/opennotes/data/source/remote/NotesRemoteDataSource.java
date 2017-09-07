package com.abhiank.opennotes.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NotesRemoteDataSource implements NotesDataSource {

    private static NotesRemoteDataSource INSTANCE = null;
    private DatabaseReference mDatabase;
    private FirebaseJobDispatcher firebaseJobDispatcher;

    private static final String NOTES_CHILD_REF = "notes";

    private NotesRemoteDataSource(Context context) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
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

        if (internet) {
            mDatabase.child(NOTES_CHILD_REF).child(note.getmId()).setValue(note);
        } else {
            firebaseJobDispatcher.mustSchedule(UpdateFirebaseJobService.getUpdateNoteJob(note,
                    firebaseJobDispatcher, UpdateFirebaseJobService.NOTE_SAVE_TAG));
        }
    }

    @Override
    public void deleteNote(@NonNull final Note note) {

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean connected = snapshot.getValue(Boolean.class);
                if (connected == null) connected = false;
                if (connected) {
                    System.out.println("connected");
                    mDatabase.child(NOTES_CHILD_REF).child(note.getmId()).removeValue();
                } else {
                    System.out.println("not connected");
                    firebaseJobDispatcher.mustSchedule(UpdateFirebaseJobService.getUpdateNoteJob(note,
                            firebaseJobDispatcher, UpdateFirebaseJobService.NOTE_DELETE_TAG));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }
}
