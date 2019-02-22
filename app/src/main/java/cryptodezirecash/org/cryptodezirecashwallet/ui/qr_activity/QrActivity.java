package cryptodezirecash.org.cryptodezirecashwallet.ui.qr_activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import cryptodezirecash.org.cryptodezirecashwallet.R;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.BaseActivity;
import cryptodezirecash.org.cryptodezirecashwallet.utils.NavigationUtils;

/**
 * Created by akshaynexus on 6/8/17.
 */

public class QrActivity extends BaseActivity {

    private View root;

    @Override
    protected void onCreateView(Bundle savedInstanceState, ViewGroup container) {
        super.onCreateView(savedInstanceState, container);;
        root = getLayoutInflater().inflate(R.layout.qr_activity,container,true);
        setTitle(R.string.my_address);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationUtils.goBackToHome(this);
    }

}
