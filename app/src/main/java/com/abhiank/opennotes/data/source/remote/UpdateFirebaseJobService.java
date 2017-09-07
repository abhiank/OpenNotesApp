package com.abhiank.opennotes.data.source.remote;

import android.content.Context;
import android.os.Bundle;

import com.abhiank.opennotes.data.Note;
import com.abhiank.opennotes.data.source.NotesDataSource;
import com.abhiank.opennotes.data.source.local.NotesLocalDataSource;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;

import org.parceler.Parcels;

/**
 * Created by abhimanyu on 07/09/17.
 */

public class UpdateFirebaseJobService extends JobService {

    public static final String NOTE_ID_EXTRA_TAG = "note";
    public static final String NOTE_SAVE_TAG = "save";
    public static final String NOTE_DELETE_TAG = "remove";
    public static final String FIREBASE_DISPATHER_JOB_TAG = "job_tag";

    public static Job getUpdateNoteJob(Note note, FirebaseJobDispatcher firebaseJobDispatcher, String taskToDo) {

        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString(NOTE_ID_EXTRA_TAG, note.getmId());
        if (taskToDo.equals(NOTE_SAVE_TAG))
            myExtrasBundle.putBoolean(NOTE_SAVE_TAG, true);
        else if (taskToDo.equals(NOTE_DELETE_TAG))
            myExtrasBundle.putBoolean(NOTE_DELETE_TAG, true);

        Job myJob = firebaseJobDispatcher.newJobBuilder()
                .setService(UpdateFirebaseJobService.class)
                .setTag(FIREBASE_DISPATHER_JOB_TAG)
                .setRecurring(false)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(
                        Constraint.ON_ANY_NETWORK
                )
                .setExtras(myExtrasBundle)
                .build();

        return myJob;
    }

    @Override
    public boolean onStartJob(JobParameters job) {

        final Bundle extras = job.getExtras();
        if (extras != null) {

            Context context = UpdateFirebaseJobService.this;
            final NotesRemoteDataSource notesRemoteDataSource = NotesRemoteDataSource.getInstance(context);
            NotesLocalDataSource notesLocalDataSource = NotesLocalDataSource.getInstance(context);

            String noteId = extras.getString(NOTE_ID_EXTRA_TAG);
            notesLocalDataSource.getNote(noteId, new NotesDataSource.GetNoteCallBack() {
                @Override
                public void onNoteLoaded(Note note) {
                    if (extras.getBoolean(NOTE_SAVE_TAG)) {
                        notesRemoteDataSource.saveNote(note);
                    } else if (extras.getBoolean(NOTE_DELETE_TAG)) {
                        notesRemoteDataSource.deleteNote(note);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                }
            });
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
