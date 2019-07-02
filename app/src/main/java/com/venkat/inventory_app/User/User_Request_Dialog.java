package com.venkat.inventory_app.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.venkat.inventory_app.R;

public class User_Request_Dialog extends AppCompatDialogFragment {

    private EditText request_count;
    private TextView item_name_dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        MainUSER activity = (MainUSER) getActivity();//getting i
        String value = activity.getMyData();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user, null);

        item_name_dialog=view.findViewById(R.id.rv_item_name);
        item_name_dialog.setText(value);

        builder.setView(view)
                .setTitle("Component Request")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        request_count=view.findViewById(R.id.rv_count);

        return builder.create();
    }
}
