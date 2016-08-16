package ru.rivendel.sagittarius.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;

/**
 * Created by elanse on 12.08.16.
 */
public class CTimerIntervalDialog extends DialogFragment implements View.OnClickListener {
    //View mainView;
    CPicker3Dialog dialog3;
    CPickerDialog dialog;
    TextView txtHour;
    TextView txtMinutes;
    TextView txtSeconds;
    TextView txtAdvance;
    TextView txtWaking;
    TextView txtRithm;
    EditText txtTitle;
    CheckBox cbAdvance;
    CheckBox cbWaking;
    CheckBox cbRithm;
    TextView currentControl=null;


    public static final String retTITLE_INTERVAL = "title";
    public static final String retADVANCE = "advanced";
    public static final String retWAKING = "waking";
    public static final String retTIME = "time";

    public CTimerIntervalDialog() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_interval, container, false);
        View.OnClickListener onClickListener3=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3For((TextView)view);
            }
        };
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFor((TextView)view);
            }
        };

        assignControls(view,onClickListener,onClickListener3);

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    private void assignControls(View view,View.OnClickListener onClickListener,
                                View.OnClickListener onClickListener3)
    {
        dialog3 = new CPicker3Dialog();
        dialog3.setTargetFragment(this, Environment.TI_REQUEST_3_NUMBER);

        dialog = new CPickerDialog();
        dialog.setTargetFragment(this, Environment.TI_REQUEST_NUMBER);

        view.findViewById(R.id.btnYES).setOnClickListener(this);
        view.findViewById(R.id.btnNO).setOnClickListener(this);

        txtTitle = (EditText)view.findViewById(R.id.ti_txtIntervalName);

        txtHour = (TextView)view.findViewById(R.id.ti_txtHours);
        txtHour.setOnClickListener(onClickListener3);

        txtMinutes = (TextView)view.findViewById(R.id.ti_txtMinutes);
        txtMinutes.setOnClickListener(onClickListener3);

        txtSeconds = (TextView)view.findViewById(R.id.ti_txtSeconds);
        txtSeconds.setOnClickListener(onClickListener3);

        txtAdvance = (TextView)view.findViewById(R.id.ti_txtAdvance);
        txtAdvance.setOnClickListener(onClickListener);

        txtWaking = (TextView)view.findViewById(R.id.ti_txtWake);
        txtWaking.setOnClickListener(onClickListener);

        txtRithm = (TextView)view.findViewById(R.id.ti_txtRithm);
        txtRithm.setOnClickListener(onClickListener);

        cbAdvance = (CheckBox)view.findViewById(R.id.ti_cbAdvance);
        cbWaking = (CheckBox)view.findViewById(R.id.ti_cbWaked);
        cbRithm = (CheckBox)view.findViewById(R.id.ti_cbRithm);

        cbAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedChanging(cbAdvance,txtAdvance);
            }
        });
        cbRithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedChanging(cbRithm,txtRithm);
            }
        });
        cbWaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedChanging(cbWaking,txtWaking);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode== Environment.TI_REQUEST_3_NUMBER) {
                Integer number1 = data.getIntExtra(dialog3.NUMBER1, -1);
                Integer number2 = data.getIntExtra(dialog3.NUMBER2, -1);
                Integer number3 = data.getIntExtra(dialog3.NUMBER3, -1);
                txtHour.setText(number1.toString());
                txtMinutes.setText(number2.toString());
                txtSeconds.setText(number3.toString());
            }
            else if (requestCode== Environment.TI_REQUEST_NUMBER)
            {
                if (currentControl!=null) {
                    Integer number = data.getIntExtra(dialog.NUMBER,-1);
                    currentControl.setText(number.toString());
                    if (currentControl==txtRithm)
                    {
                        setNumberCheckboxStatus(txtRithm,cbRithm);
                        resetNumerCheckBoxStatus(txtWaking,cbWaking);
                    }
                    else if (currentControl==txtWaking)
                    {
                        setNumberCheckboxStatus(txtWaking,cbWaking);
                        resetNumerCheckBoxStatus(txtRithm,cbRithm);
                    }
                    else if (currentControl==txtAdvance)
                    {
                        setNumberCheckboxStatus(txtAdvance,cbAdvance);
                    }
                }
            }
        }
    }

    private void checkedChanging(CheckBox checkBox, TextView txtView)
    {
        boolean toChecked = checkBox.isChecked();
        checkBox.setChecked(false);
        if (toChecked==false) txtView.setText("0");
        else dialogFor(txtView);
    }

    private void setNumberCheckboxStatus(TextView textView, CheckBox cbElement)
    {
        Integer number = Integer.valueOf(textView.getText().toString());
        if (number>0) cbElement.setChecked(true);
        else cbElement.setChecked(false);
    }

    private void resetNumerCheckBoxStatus(TextView textView, CheckBox cbElement)
    {
        textView.setText("0");
        cbElement.setChecked(false);
    }

    private void dialog3For(TextView textField)
    {
        dialog3.setNumbers(Integer.valueOf(txtHour.getText().toString()),
                Integer.valueOf(txtMinutes.getText().toString()),
                Integer.valueOf(txtSeconds.getText().toString()));
        dialog3.show(getFragmentManager(),"Picker3Dialog");
    }
    private void dialogFor(TextView textField)
    {
        currentControl = textField;
        dialog.setNumber(Integer.valueOf(textField.getText().toString()));
        dialog.show(getFragmentManager(),"PickerDialog");
    }

    private int convertHHMMSStoSec(String hh,String mm, String ss)
    {
        return Integer.valueOf(hh)*60*60+Integer.valueOf(mm)*60+Integer.valueOf(ss);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnYES) {
            Intent intent = new Intent();
            intent.putExtra(retTITLE_INTERVAL, txtTitle.getText().toString());
            intent.putExtra(retADVANCE,Integer.valueOf(txtAdvance.getText().toString()));
            intent.putExtra(retWAKING,Integer.valueOf(txtWaking.getText().toString()));
            intent.putExtra(retTIME,convertHHMMSStoSec(txtHour.getText().toString(),
                    txtMinutes.getText().toString(),txtSeconds.getText().toString()));
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }
        dismiss();
    }
}
