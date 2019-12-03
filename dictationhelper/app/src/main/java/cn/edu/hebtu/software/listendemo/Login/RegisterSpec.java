package cn.edu.hebtu.software.listendemo.Login;

import android.support.v4.app.Fragment;
import android.widget.ProgressBar;


import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class RegisterSpec {
    private Fragment fragment;
    private ProgressBar progressBar;

    public void changeProgress(int step) {
        switch (step) {
            case Constant.REGISTER_STEP_ONE:
                progressBar.setMax(100);
                progressBar.setProgress(50);
                break;
            case Constant.REGISTER_STEP_TWO:
                progressBar.setMax(100);
                progressBar.setProgress(100);
                break;
        }
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
