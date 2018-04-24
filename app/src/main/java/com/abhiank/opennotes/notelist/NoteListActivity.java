package com.abhiank.opennotes.notelist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.abhiank.opennotes.BaseActivity;
import com.abhiank.opennotes.R;
import com.abhiank.opennotes.customview.VerticalSpaceItemDecoration;
import com.abhiank.opennotes.data.model.Note;
import com.abhiank.opennotes.noteedit.AddEditNoteActivity;
import com.abhiank.opennotes.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteListActivity extends BaseActivity implements NoteListContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton addNewNoteFab;
    NoteListAdapter noteListAdapter;
    List<Note> searchList;

    @Inject
    public NoteListContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.note_list_toolbar_title);

        recyclerView.setLayoutManager(new LinearLayoutManager(NoteListActivity.this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(NoteListActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int) Utils.convertDpToPixel(8, NoteListActivity.this)));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    addNewNoteFab.hide();
                else if (dy < 0)
                    addNewNoteFab.show();
            }
        });

        presenter.attachView(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        presenter.onStart();
    }
    /*Adding Search to the Action bar*/

    //start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main , menu);
        MenuItem item = menu.findItem(R.id.noteSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Note> notes = searchList;
                List<Note> filteredNoteList = filter(notes,newText.toLowerCase());
                noteListAdapter.getfilter(filteredNoteList);
                return true;
            }
        });

        return true;
    }

    //end

    //Filter from title and content
    private List<Note> filter(List<Note> notes, String query){
        List<Note> filteredNoteList = new ArrayList<>();
        for(Note model  : notes){
            String title = model.getTitle().toLowerCase();
            String content = model.getContent().toLowerCase();
            if(title.startsWith(query)){
                filteredNoteList.add(model);
            }
            else if(title.contains(query)){
                filteredNoteList.add(model);
            }
            else if(content.contains(query)){
                filteredNoteList.add(model);
            }
        }
        return filteredNoteList;
    }

    //end
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                presenter.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        startActivity(AddEditNoteActivity.getActivityIntent(NoteListActivity.this));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNoteListItems(List<Note> noteList) {

        searchList = noteList;
        noteListAdapter = new NoteListAdapter(noteList, new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String noteId) {
                presenter.onNoteItemClicked(noteId);
            }

            @Override
            public void onItemDeleteClick(String noteId) {
                showNoteDeletionDialog(noteId);
            }
        });
        recyclerView.setAdapter(noteListAdapter);
    }

    public void showNoteDeletionDialog(String noteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.delete_note_dialog_title);
        builder.setMessage(R.string.delete_note_dialog_message);
        builder.setPositiveButton(R.string.delete_note_pos_button, (dialogInterface, i) -> presenter.onNoteItemRemoveClicked(noteId));
        builder.setNegativeButton(R.string.delete_note_negative_button, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(NoteListActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNote(Note note) {
        startActivity(AddEditNoteActivity.getActivityIntent(NoteListActivity.this, note.getmId()));
    }

    @Override
    public void removeNoteFromList(String noteId) {
        ((NoteListAdapter) recyclerView.getAdapter()).removeNoteFromList(noteId);
        Toast.makeText(NoteListActivity.this, R.string.note_removed_success_message, Toast.LENGTH_SHORT).show();
    }

}
