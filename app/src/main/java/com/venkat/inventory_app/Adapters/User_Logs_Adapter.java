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

public class User_Logs_Adapter extends FirestoreRecyclerAdapter<Logs_Model, User_Logs_Adapter.UserLogsHolder> {

    public User_Logs_Adapter(@NonNull FirestoreRecyclerOptions<Logs_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserLogsHolder holder, int position, @NonNull Logs_Model model) {

        holder.user_countlog.setText(String.valueOf(model.getCountitem()));
        holder.user_itemnamelog.setText(model.getItem_name());
        holder.user_status.setText(model.getStatus());
    }

    @NonNull
    @Override
    public UserLogsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_logs_layout,viewGroup,false);
        return new UserLogsHolder(v);
    }

    class UserLogsHolder extends RecyclerView.ViewHolder{

        TextView user_itemnamelog;
        TextView user_countlog;
        TextView user_status;
        public UserLogsHolder(@NonNull View itemView) {
            super(itemView);
            user_itemnamelog=itemView.findViewById(R.id.item_name_logs_user);
            user_countlog=itemView.findViewById(R.id.count_logs_user);
            user_status=itemView.findViewById(R.id.user_status_logs);

        }
    }
}
