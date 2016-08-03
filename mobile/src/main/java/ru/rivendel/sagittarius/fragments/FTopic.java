package ru.rivendel.sagittarius.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.dialogs.CListDialog;

/**
 * Created by user on 03.08.16.
 */

public class FTopic extends CFragment {

    public FTopic() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectTopic();
            }
        });

        return view;

    }

    public void selectTopic() {

        CListDialog dialog = new CListDialog();
        dialog.show(getFragmentManager(),"TestList");

    }

}
