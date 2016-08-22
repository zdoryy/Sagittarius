package ru.rivendel.sagittarius.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 08.08.16.
 */

public class CStringDialog extends DialogFragment {

    private String title;
    private String init;
    private int mode;
    private OnEnterListener mListener;

    public interface OnEnterListener {
        void onEnter(String str, int mode);
    }

    public CStringDialog() {
        super();
    }

    public static CStringDialog newInstance(String title, String init, int mode, int lst) {
        CStringDialog fragment = new CStringDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("init",init);
        args.putInt("listener", lst);
        args.putInt("mode",mode);
        fragment.setArguments(args);
        return fragment;
    }

    public static CStringDialog newInstance(String title, int mode, int lst) {
        CStringDialog fragment = new CStringDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putInt("listener", lst);
        args.putInt("mode",mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle param = getArguments();
        title = param.getString("title","");
        init = param.getString("init","");
        mode = param.getInt("mode");
        int owner = param.getInt("listener");
        mListener = (OnEnterListener) getFragmentManager().findFragmentById(owner);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view  = inflater.inflate(R.layout.dialog_string, null);

        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        editText.setText(init);

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                         mListener.onEnter(editText.getText().toString(),mode);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                     }
                });
        return builder.create();

    }
}
