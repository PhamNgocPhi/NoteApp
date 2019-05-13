package com.example.rikkeisoft.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;
    private NoteOnclickListener noteOnclickListener;


    public NoteAdapter() {
        this.notes = new ArrayList<>();

    }

    public void setNoteOnclickListener(NoteOnclickListener noteOnclickListener) {
        this.noteOnclickListener = noteOnclickListener;

    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_note_activity, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvDate.setText(DateUtils.partDateToString(note.getCreateDate()));
        holder.rlColorItem.setBackgroundColor(note.getColor());
        holder.cvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteOnclickListener.onClickItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    //FIXME không cần public static
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ivAlarm)
        ImageView ivalarms;
        @BindView(R.id.rlColorItem)
        RelativeLayout rlColorItem;
        @BindView(R.id.cvNote)
        CardView cvNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            //FIXME Sử dụng hàm này để bind view
            ButterKnife.bind(this, itemView);
        }
    }

    public interface NoteOnclickListener {
        void onClickItem(int noteId);
    }
}
