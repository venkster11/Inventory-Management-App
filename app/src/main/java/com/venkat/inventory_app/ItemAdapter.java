package com.venkat.inventory_app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ItemAdapter extends FirestoreRecyclerAdapter<Itemshow, ItemAdapter.ItemHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ItemAdapter(@NonNull FirestoreRecyclerOptions<Itemshow> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Itemshow model) {

        holder.textViewItemname.setText(model.getItem_name());
        holder.textViewusername.setText(model.getUser_name());
        holder.textViewcount.setText(String.valueOf(model.getCount()));
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new ItemHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        TextView textViewItemname;
        TextView textViewusername;
        TextView textViewcount;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemname=itemView.findViewById(R.id.item_name);
            textViewusername=itemView.findViewById(R.id.user_name);
            textViewcount=itemView.findViewById(R.id.count);
        }
    }
}
