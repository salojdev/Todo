package com.saloj.android.wes_task_project.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saloj.android.wes_task_project.R;
import com.saloj.android.wes_task_project.helper.NotesListener;

import java.util.ArrayList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {

     Context ctx;
    ArrayList<NotesModel> listModel;
     int notesid;
    String notesStr;
MainActivity activity;
     NotesListener notesListener;

    public NotesListAdapter(Context ctx, ArrayList<NotesModel> listModel,MainActivity activity) {
        this.activity = activity;
        this.ctx = ctx;
        this.listModel = listModel;
       // this.notesid = notesid;
       // this.notesStr = notesStr;
        this.notesListener = notesListener;
    }


    @Override
    public NotesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from( parent.getContext()).inflate( R.layout.todo_list_item_layout,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.ViewHolder holder, int position) {
NotesModel notesModel = listModel.get(position);

        holder.tv_time.setText(notesModel.getTime_stamp());
        holder.tv_title.setText(notesModel.getTitle());
        holder.tv_notes.setText(notesModel.getDescription());

        holder.edit.setOnClickListener(v -> {

            activity.getNotesEdit(notesModel.getNotes_id(),notesModel.getTitle(),notesModel.getDescription());

        });  holder.delete.setOnClickListener(v -> {

            activity.getDeleted(notesModel.getNotes_id(),notesModel.getTitle());

        });


    }

    @Override
    public int getItemCount() {
        Log.d("Adapter", "getItemCount: "+listModel.size());
        return listModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView delete,edit;
        TextView tv_notes,tv_time,tv_title;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            edit = itemView.findViewById( R.id.img_edit);
            delete = itemView.findViewById( R.id.img_delete);
            tv_time= itemView.findViewById( R.id.tv_date);
            tv_title = itemView.findViewById( R.id.tv_title);
            tv_notes = itemView.findViewById( R.id.tv_notes);

        }

    }

}
