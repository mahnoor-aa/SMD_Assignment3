package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleBinAdapter extends RecyclerView.Adapter<RecycleBinAdapter.ViewHolder2>{

    DeleteContact parentActivity;

    public interface DeleteContact
    {
        public void onDeletePassword(int index);
    }

    ArrayList<Info> passwords;
    Context context;

    public RecycleBinAdapter(Context c, ArrayList<Info> list)
    {
        passwords = list;
        context = c;
        parentActivity = (DeleteContact) c;
    }

    @NonNull
    @Override
    public RecycleBinAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_password, parent, false);
        return new ViewHolder2(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecycleBinAdapter.ViewHolder2 holder, int position) {
        holder.tvUsername.setText(passwords.get(position).getUsername());
        holder.tvPassword.setText(passwords.get(position).getPassword());
        holder.tvURL.setText(passwords.get(position).getUrl());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(holder.itemView.getContext());
                alertDialog.setMessage("Do you really want to delete?");
                alertDialog.setTitle("Delete Notification");

                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InfoDB database = new InfoDB(context);
                        database.open();
                        database.deletePasswordRecycle(passwords.get(holder.getAdapterPosition()).getId());
                        database.close();

                        parentActivity.onDeletePassword(holder.getAdapterPosition());
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();


                return false;
            }
        });

        holder.ivRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(holder.itemView.getContext());
                alertDialog.setMessage("Do you really want to restore");
                alertDialog.setTitle("Restore Notification");

                alertDialog.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InfoDB database = new InfoDB(context);
                        database.open();
                        database.RestorePassword(passwords.get(holder.getAdapterPosition()).getId());
                        database.close();

                        parentActivity.onDeletePassword(holder.getAdapterPosition());
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder
    {
        TextView tvUsername,tvPassword,tvURL;
        ImageView ivRestore;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvdisplayUsername);
            tvPassword = itemView.findViewById(R.id.tvdisplayPassword);
            tvURL = itemView.findViewById(R.id.tvdisplayURL);
            ivRestore = itemView.findViewById(R.id.ivEdit);
            ivRestore.setImageResource(R.drawable.ic_restore);
        }
    }
}
