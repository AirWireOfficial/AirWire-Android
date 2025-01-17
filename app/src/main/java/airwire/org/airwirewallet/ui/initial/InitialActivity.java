package airwire.org.airwirewallet.ui.initial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import airwire.org.airwirewallet.AirWireApplication;
import airwire.org.airwirewallet.ui.splash_activity.SplashActivity;
import airwire.org.airwirewallet.ui.wallet_activity.WalletActivity;
import airwire.org.airwirewallet.utils.AppConf;

/**
 * Created by akshaynexus on 8/19/17.
 */

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AirWireApplication airwireApplication = AirWireApplication.getInstance();
        AppConf appConf = airwireApplication.getAppConf();
        // show report dialog if something happen with the previous process
        Intent intent;
        if (!appConf.isAppInit() || appConf.isSplashSoundEnabled()){
            intent = new Intent(this, SplashActivity.class);
        }else {
            intent = new Intent(this, WalletActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
