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
import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 12.08.16.
 */
public class CTaskDialog extends DialogFragment {

    private CTask task;
    private OnSaveListener listener;

        public interface OnSaveListener {
            void onSave();
        }

        public CTaskDialog() {
            super();
        }

        public void setParam(CTask _task, OnSaveListener lst) {
            task = _task;
            listener = lst;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view  = inflater.inflate(R.layout.dialog_task, null);

            final EditText titleText = (EditText) view.findViewById(R.id.title_text);
            titleText.setText(task.title);

            builder.setView(view)
                    .setTitle("Задание")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            task.title = titleText.getText().toString();
                            task.saveMe();
                            listener.onSave();
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
