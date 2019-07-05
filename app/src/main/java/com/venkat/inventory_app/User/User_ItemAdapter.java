package com.venkat.inventory_app.User;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.venkat.inventory_app.Common.Itemshow;
import com.venkat.inventory_app.R;

public class User_ItemAdapter extends FirestoreRecyclerAdapter<Itemshow, User_ItemAdapter.UserItemHolder> {
    private OnItemClickListner listner;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Dialog mydialog;

    public User_ItemAdapter(@NonNull FirestoreRecyclerOptions<Itemshow> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserItemHolder holder, int position, @NonNull Itemshow model) {
        holder.textViewItemname.setText(model.getItem_name());
        holder.textViewcount.setText(String.valueOf(model.getCount()));

    }

    @NonNull
    @Override
    public UserItemHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item_layout,viewGroup,false);
        final UserItemHolder userHolder = new UserItemHolder(v);



    /*    userHolder.rv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewGroup.getContext(),"Text Click"+String.valueOf(userHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
            }
        });*/

        return new UserItemHolder(v);
    }



    class UserItemHolder extends RecyclerView.ViewHolder{

        CardView rv_layout;
        TextView textViewItemname;
        TextView textViewcount;

        public UserItemHolder(@NonNull View itemView) {
            super(itemView);
            rv_layout=itemView.findViewById(R.id.rv_user_item_layout);
            textViewItemname=itemView.findViewById(R.id.item_nameu);
            textViewcount=itemView.findViewById(R.id.countu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listner!=null){
                        listner.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }
    }

    public interface OnItemClickListner{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);

    }
    public void setOnItemClickListener(OnItemClickListner listener){

        this.listner=listener;
    }

}
