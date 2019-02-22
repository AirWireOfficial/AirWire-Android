package airwire.org.airwirewallet.ui.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import airwire.org.airwirewallet.AirWireApplication;
import global.AirWireModule;

/**
 * Created by akshaynexus on 6/29/17.
 */

public class BaseFragment extends Fragment {

    protected AirWireApplication airwireApplication;
    protected AirWireModule airwireModule;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        airwireApplication = AirWireApplication.getInstance();
        airwireModule = airwireApplication.getModule();
    }

    protected boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getActivity(),permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
