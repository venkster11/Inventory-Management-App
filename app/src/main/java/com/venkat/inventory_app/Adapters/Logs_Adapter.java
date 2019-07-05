package com.venkat.inventory_app.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.venkat.inventory_app.Model.Logs_Model;
import com.venkat.inventory_app.R;

public class Logs_Adapter extends FirestoreRecyclerAdapter<Logs_Model, Logs_Adapter.LogsHolder> {


    public Logs_Adapter(@NonNull FirestoreRecyclerOptions<Logs_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LogsHolder holder, int position, @NonNull Logs_Model model) {

        holder.countlog.setText(String.valueOf(model.getCountitem()));
        holder.itemnamelog.setText(model.getItem_name());
        holder.usernamelog.setText(model.getUsername());
        holder.status.setText(model.getStatus());
    }

    @NonNull
    @Override
    public LogsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_logs_layout,viewGroup,false);
        return new LogsHolder(v);
    }

    class LogsHolder extends RecyclerView.ViewHolder{
        TextView itemnamelog;
        TextView usernamelog;
        TextView countlog;
        TextView status;

        public LogsHolder(@NonNull View itemView) {
            super(itemView);
            itemnamelog=itemView.findViewById(R.id.item_name_log);
            usernamelog=itemView.findViewById(R.id.user_name_log);
            countlog=itemView.findViewById(R.id.count_log);
            status=itemView.findViewById(R.id.status_log);

        }
    }
}
