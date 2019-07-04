package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.CheckupModel;
import com.bigblackboy.doctorappointment.pojos.springpojos.HealthInfo;
import com.bigblackboy.doctorappointment.pojos.springpojos.Procedure;
import com.bigblackboy.doctorappointment.view.fragment.CheckupFragment;

import org.joda.time.LocalDate;

import java.util.List;

public class CheckupFragmentPresenter implements MVPBaseInterface.Presenter {

    private MVPBaseInterface.View view;
    private CheckupModel model;

    public CheckupFragmentPresenter() {
        model = new CheckupModel();
    }

    @Override
    public void attachView(MVPBaseInterface.View view) {
        this.view = view;
    }

    @Override
    public void viewIsReady() {
        view.showProgressBar();
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void getHealthInfo(final int age) {
        model.getHealthInfo(age, new CheckupModel.OnFinishedListener() {
            @Override
            public void onFinished(HealthInfo info) {
                if (model.getCheckupYear(age) == new LocalDate().getYear()) {
                    ((CheckupFragment) view).setTvHint("В этом году вы можете пройти бесплатную диспансеризацию.");
                } else ((CheckupFragment) view).setTvHint(String.format("Вы сможете пройти диспансеризацию в %d году.", model.getCheckupYear(age)));

                List<Procedure> procedures = info.getProcedures();
                ((CheckupFragment)view).showProcedures(procedures);
                view.hideProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                view.hideProgressBar();
                view.showToast(t.getMessage());
            }
        });
    }

}
