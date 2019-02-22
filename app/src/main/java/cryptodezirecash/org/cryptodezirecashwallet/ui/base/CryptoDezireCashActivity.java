package cryptodezirecash.org.cryptodezirecashwallet.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import cryptodezirecash.org.cryptodezirecashwallet.CryptoDezireCashApplication;
import cryptodezirecash.org.cryptodezirecashwallet.R;
import global.CryptoDezireCashModule;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.dialogs.SimpleTextDialog;
import cryptodezirecash.org.cryptodezirecashwallet.utils.DialogsUtil;

import static cryptodezirecash.org.cryptodezirecashwallet.service.IntentsConstants.ACTION_STORED_BLOCKCHAIN_ERROR;
import static cryptodezirecash.org.cryptodezirecashwallet.service.IntentsConstants.ACTION_TRUSTED_PEER_CONNECTION_FAIL;

/**
 * Created by akshaynexus on 6/8/17.
 */

public class CryptoDezireCashActivity extends AppCompatActivity {

    protected CryptoDezireCashApplication cryptodezirecashApplication;
    protected CryptoDezireCashModule cryptodezirecashModule;

    protected LocalBroadcastManager localBroadcastManager;
    private static final IntentFilter intentFilter = new IntentFilter(ACTION_TRUSTED_PEER_CONNECTION_FAIL);
    private static final IntentFilter errorIntentFilter = new IntentFilter(ACTION_STORED_BLOCKCHAIN_ERROR);

    protected boolean isOnForeground = false;

    private BroadcastReceiver trustedPeerConnectionDownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_TRUSTED_PEER_CONNECTION_FAIL)) {
                SimpleTextDialog simpleTextDialog = DialogsUtil.buildSimpleErrorTextDialog(context,R.string.title_no_trusted_peer_connection,R.string.message_no_trusted_peer_connection);
                simpleTextDialog.show(getFragmentManager(),"fail_node_connection_dialog");
            }else if (action.equals(ACTION_STORED_BLOCKCHAIN_ERROR)){
                SimpleTextDialog simpleTextDialog = DialogsUtil.buildSimpleErrorTextDialog(context,R.string.title_blockstore_error,R.string.message_blockstore_error);
                simpleTextDialog.show(getFragmentManager(),"blockstore_error_dialog");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cryptodezirecashApplication = CryptoDezireCashApplication.getInstance();
        cryptodezirecashModule = cryptodezirecashApplication.getModule();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnForeground = true;
        localBroadcastManager.registerReceiver(trustedPeerConnectionDownReceiver,intentFilter);
        localBroadcastManager.registerReceiver(trustedPeerConnectionDownReceiver,errorIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOnForeground = false;
        localBroadcastManager.unregisterReceiver(trustedPeerConnectionDownReceiver);
    }
}
