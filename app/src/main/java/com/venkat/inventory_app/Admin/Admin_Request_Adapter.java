package com.venkat.inventory_app.Admin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.User.Request_Model;

import java.awt.font.TextAttribute;

public class Admin_Request_Adapter extends FirestoreRecyclerAdapter<Request_Model,Admin_Request_Adapter.RequestHolder> {

    private OnItemClickListner listner;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public Admin_Request_Adapter(@NonNull FirestoreRecyclerOptions<Request_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestHolder holder, int position, @NonNull Request_Model model) {
        holder.ItemName.setText(model.getNameitem());
        holder.UserName.setText(model.getUsername());
        holder.AvailableCount.setText(String.valueOf(model.getCountavail()));
        holder.RequestedCount.setText(String.valueOf(model.getReqcount()));
        //holder.DatenTime.setText((CharSequence) model.getTimestamp());

    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_request,viewGroup,false);
        return new RequestHolder(v);
    }

    class RequestHolder extends RecyclerView.ViewHolder{

        TextView ItemName;
        TextView UserName;
        TextView AvailableCount;
        TextView RequestedCount;
       // TextView DatenTime;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            ItemName=itemView.findViewById(R.id.item_name_rq);
            UserName=itemView.findViewById(R.id.user_name_rq);
            AvailableCount=itemView.findViewById(R.id.countavailable);
            RequestedCount=itemView.findViewById(R.id.countrequested);
            //DatenTime=itemView.findViewById(R.id.date_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listner!=null){
                        listner.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }

    }

    public interface OnItemClickListner{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setOnItemClickListener(OnItemClickListner listener){
        this.listner=listener;
    }
}
