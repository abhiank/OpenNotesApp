package com.abhiank.opennotes.notelist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abhiank.opennotes.R;
import com.abhiank.opennotes.domain.Note;
import com.abhiank.opennotes.noteedit.AddEditNoteActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NoteListView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton addNewNoteFab;

    private NoteListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.note_list_toolbar_title);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    addNewNoteFab.hide();
                else if (dy < 0)
                    addNewNoteFab.show();
            }
        });


        presenter = new NoteListPresenterImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
    public void onFabClicked(){
        startActivity(AddEditNoteActivity.getActivityIntent(MainActivity.this));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setNoteListItems(List<Note> noteList) {

        NoteListAdapter noteListAdapter = new NoteListAdapter(noteList, new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                presenter.onNoteItemClicked(position);
            }

            @Override
            public void onItemDeleteClick(int position) {
                presenter.onNoteItemRemoved(position);
            }
        });
        recyclerView.setAdapter(noteListAdapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToAddEditNoteScreen(Note note) {
        startActivity(AddEditNoteActivity.getActivityIntent(MainActivity.this, note));
    }

}
