package com.bigblackboy.doctorappointment.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener mListener;
    private int year, month, day;

    public static DatePickerFragment newInstance(int year, int month, int day) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        if (getArguments() != null) {
            if (getArguments().containsKey("year") && getArguments().containsKey("month") && getArguments().containsKey("day")) {
                year = getArguments().getInt("year");
                month = getArguments().getInt("month");
                day = getArguments().getInt("day");
            }
        } else {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), mListener, year, month, day);
    }

    public void setDatePickerFragmentListener(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
    }
}
