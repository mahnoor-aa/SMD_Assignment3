package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    DeleteContact parentActivity;

    public interface DeleteContact
    {
        public void onDeleteContact(int index);
        public void onUpdateContact(int index, String []values);
    }

    ArrayList<Info> passwords;
    Context context;

    public InfoAdapter(Context c, ArrayList<Info> list)
    {
        passwords = list;
        context = c;
        parentActivity = (DeleteContact) c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_password, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                        database.deleteContact(passwords.get(holder.getAdapterPosition()).getId());
                        database.close();

                        parentActivity.onDeleteContact(holder.getAdapterPosition());
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

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog editDialog = new AlertDialog.Builder(context).create();
                editDialog.setTitle("Edit Contact");
                View view = LayoutInflater.from(context).inflate(R.layout.edit_layout, null,false);
                editDialog.setView(view);
                editDialog.show();

                EditText etUsername, etPassword,etURL;
                Button btnUpdate, btnCancel;
                etUsername = view.findViewById(R.id.eteditUsername);

                etPassword = view.findViewById(R.id.eteditPassword);
                etURL = view.findViewById(R.id.eteditUrl);
                btnUpdate = view.findViewById(R.id.btneditUpdate);
                btnCancel = view.findViewById(R.id.btneditCancel);

                etUsername.setText(passwords.get(holder.getAdapterPosition()).getUsername());
                etPassword.setText(passwords.get(holder.getAdapterPosition()).getPassword());
                etURL.setText(passwords.get(holder.getAdapterPosition()).getUrl());
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InfoDB database = new InfoDB(context);
                        database.open();
                        database.updateContact(passwords.get(holder.getAdapterPosition()).getId(),
                                etUsername.getText().toString().trim(),
                                etPassword.getText().toString().trim(),
                                etURL.getText().toString().trim());
                        database.close();
                        String []updateContact = new String[]{etUsername.getText().toString().trim(),
                                etPassword.getText().toString().trim(),etURL.getText().toString().trim()};
                        parentActivity.onUpdateContact(holder.getAdapterPosition(), updateContact);
                        editDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvUsername,tvPassword,tvURL;
        ImageView ivEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvdisplayUsername);
            tvPassword = itemView.findViewById(R.id.tvdisplayPassword);
            tvURL = itemView.findViewById(R.id.tvdisplayURL);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
}
