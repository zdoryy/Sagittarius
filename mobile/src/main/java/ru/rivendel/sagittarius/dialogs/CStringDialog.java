package ru.rivendel.sagittarius.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.rivendel.sagittarius.R;

/**
 * Created by user on 08.08.16.
 */
public class CStringDialog extends DialogFragment {

    private String title;
    private String value;
    private OnEnterListener listener;

    public interface OnEnterListener {
        void onEnter(String str);
    }

    public CStringDialog() {
        super();
    }

    public void setParam(String name, String init, OnEnterListener lst) {
        title = name;
        value = init;
        listener = lst;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view  = inflater.inflate(R.layout.dialog_string, null);

        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        editText.setText(value);

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                         listener.onEnter(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //CStringDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }
}
