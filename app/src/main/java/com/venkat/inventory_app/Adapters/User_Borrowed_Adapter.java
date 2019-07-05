package com.venkat.inventory_app.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.venkat.inventory_app.Model.Borrowed_Model;
import com.venkat.inventory_app.R;

public class User_Borrowed_Adapter extends FirestoreRecyclerAdapter<Borrowed_Model, User_Borrowed_Adapter.BorrowedHolder> {


    private OnItemClickListner listner;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public User_Borrowed_Adapter(@NonNull FirestoreRecyclerOptions<Borrowed_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BorrowedHolder holder, int position, @NonNull Borrowed_Model model) {

        holder.borrowed_itemname.setText(model.getItem_name());
        holder.borrowed_count.setText(String.valueOf(model.getMycount()));
    }

    @NonNull
    @Override
    public BorrowedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_borrowed_layout,viewGroup,false);
        return new BorrowedHolder(v);

    }

    class BorrowedHolder extends RecyclerView.ViewHolder{
        TextView borrowed_itemname;
        TextView borrowed_docuid;
        TextView borrowed_count;

        public BorrowedHolder(@NonNull View itemView) {
            super(itemView);
            borrowed_itemname=itemView.findViewById(R.id.borrowed_item_name);
            borrowed_count=itemView.findViewById(R.id.borrowed_count);

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
