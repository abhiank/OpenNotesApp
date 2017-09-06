package com.abhiank.opennotes.notelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abhiank.opennotes.R;
import com.abhiank.opennotes.data.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abhimanyu on 06/09/17.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private OnItemClickListener itemClickListener;

    public NoteListAdapter(List<Note> noteList, OnItemClickListener onItemClickListener) {
        this.noteList = noteList;
        this.itemClickListener = onItemClickListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item_view, parent, false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.noteContentTextView.setText(note.getContent());
        holder.noteTitleTextView.setText(note.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });

        holder.deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_title_tv__note_list_item)
        TextView noteTitleTextView;
        @BindView(R.id.note_preview_tv__note_list_item)
        TextView noteContentTextView;
        @BindView(R.id.delete_note_button__note_list_item)
        ImageButton deleteNoteButton;

        NoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {

        void onItemClick(int position);

        void onItemDeleteClick(int position);
    }

}
