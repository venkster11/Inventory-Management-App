package com.venkat.inventory_app.Adapters;

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
import com.venkat.inventory_app.Model.Itemshow;
import com.venkat.inventory_app.R;

public class ItemAdapter extends FirestoreRecyclerAdapter<Itemshow, ItemAdapter.ItemHolder> {
    private OnItemClickListner listner;
    private int countavail;
    public ItemAdapter(@NonNull FirestoreRecyclerOptions<Itemshow> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Itemshow model) {


        Number countavail1 = model.getCount();
        countavail=countavail1.intValue();
        if(countavail!=0){
            holder.textViewItemname.setText(model.getItem_name());
            holder.textViewusername.setText(model.getUser_name());
            holder.textViewcount.setText(String.valueOf(model.getCount()));
        }
        else {
            holder.rootview.setLayoutParams(holder.params);
        }


    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
            return new ItemHolder(v);

}

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        public CardView.LayoutParams params;
        public CardView rootview;
        TextView textViewItemname;
        TextView textViewusername;
        TextView textViewcount;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            params = new CardView.LayoutParams(0,0);
            rootview = itemView.findViewById(R.id.rootviewc);

            textViewItemname=itemView.findViewById(R.id.item_name);
            textViewusername=itemView.findViewById(R.id.user_name);
            textViewcount=itemView.findViewById(R.id.count);

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
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListner listener){
        this.listner=listener;
    }
}
