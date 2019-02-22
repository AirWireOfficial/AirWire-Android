package cryptodezirecash.org.cryptodezirecashwallet.ui.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import cryptodezirecash.org.cryptodezirecashwallet.CryptoDezireCashApplication;
import global.CryptoDezireCashModule;

/**
 * Created by akshaynexus on 6/29/17.
 */

public class BaseFragment extends Fragment {

    protected CryptoDezireCashApplication cryptodezirecashApplication;
    protected CryptoDezireCashModule cryptodezirecashModule;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cryptodezirecashApplication = CryptoDezireCashApplication.getInstance();
        cryptodezirecashModule = cryptodezirecashApplication.getModule();
    }

    protected boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getActivity(),permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
