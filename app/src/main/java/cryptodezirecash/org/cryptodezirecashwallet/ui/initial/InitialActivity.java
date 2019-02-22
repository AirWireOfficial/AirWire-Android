package cryptodezirecash.org.cryptodezirecashwallet.ui.initial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cryptodezirecash.org.cryptodezirecashwallet.CryptoDezireCashApplication;
import cryptodezirecash.org.cryptodezirecashwallet.ui.splash_activity.SplashActivity;
import cryptodezirecash.org.cryptodezirecashwallet.ui.wallet_activity.WalletActivity;
import cryptodezirecash.org.cryptodezirecashwallet.utils.AppConf;

/**
 * Created by akshaynexus on 8/19/17.
 */

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CryptoDezireCashApplication cryptodezirecashApplication = CryptoDezireCashApplication.getInstance();
        AppConf appConf = cryptodezirecashApplication.getAppConf();
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
