package com.bigblackboy.doctorappointment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.fragment.AppointmentFragment;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.fragment.SpecialityFragment;

import java.util.HashMap;
import java.util.Map;

public class MakeAppointmentActivity extends AppCompatActivity implements OnDataPass {

    Fragment districtFragment;
    FragmentTransaction fTrans;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        fm = getSupportFragmentManager();
        districtFragment = new DistrictFragment();
        fm.beginTransaction().add(R.id.linLayoutMakeAppointment, districtFragment).commit();
    }

    @Override
    public void onDataPass(int fragmentId, HashMap hashMap) {
        Bundle bundle;
        switch (fragmentId) {
            case 1:
                fTrans = fm.beginTransaction();
                HospitalFragment hospitalFragment = new HospitalFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                hospitalFragment.setArguments(bundle);
                fTrans.replace(R.id.linLayoutMakeAppointment, hospitalFragment).addToBackStack("fragment_district");
                fTrans.commit();
                break;
            case 2:
                fTrans = fm.beginTransaction();
                SpecialityFragment specFragment = new SpecialityFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                specFragment.setArguments(bundle);
                fTrans.replace(R.id.linLayoutMakeAppointment, specFragment).addToBackStack("fragment_hospital");
                fTrans.commit();
                break;
            case 3:
                fTrans = fm.beginTransaction();
                DoctorFragment doctorFragment = new DoctorFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                doctorFragment.setArguments(bundle);
                fTrans.replace(R.id.linLayoutMakeAppointment, doctorFragment).addToBackStack("spec_fragment");
                fTrans.commit();
                break;
            case 4:
                fTrans = fm.beginTransaction();
                AppointmentFragment appointmentFragment = new AppointmentFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                appointmentFragment.setArguments(bundle);
                fTrans.replace(R.id.linLayoutMakeAppointment, appointmentFragment).addToBackStack("fragment_doctor");
                fTrans.commit();
                break;
            case 5:
                /*fTrans = fm.beginTransaction();
                AppointmentFragment appointmentFragment = new AppointmentFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                appointmentFragment.setArguments(bundle);
                fTrans.replace(R.id.linLayoutDistrict, appointmentFragment).addToBackStack("fragment_doctor");
                fTrans.commit();*/
                break;

        }

    }
}
