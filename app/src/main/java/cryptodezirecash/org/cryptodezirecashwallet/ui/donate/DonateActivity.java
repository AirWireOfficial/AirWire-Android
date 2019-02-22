package cryptodezirecash.org.cryptodezirecashwallet.ui.donate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.cryptodezirecashj.core.Coin;
import org.cryptodezirecashj.core.InsufficientMoneyException;
import org.cryptodezirecashj.core.MasterNodeSystem;
import org.cryptodezirecashj.core.MasternodeInfo;
import org.cryptodezirecashj.core.MasternodeManager;
import org.cryptodezirecashj.core.Transaction;

import cryptodezirecash.org.cryptodezirecashwallet.R;
import cryptodezirecash.org.cryptodezirecashwallet.module.CryptoDezireCashContext;
import cryptodezirecash.org.cryptodezirecashwallet.service.CryptoDezireCashWalletService;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.BaseDrawerActivity;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.dialogs.SimpleTextDialog;
import cryptodezirecash.org.cryptodezirecashwallet.utils.DialogsUtil;
import cryptodezirecash.org.cryptodezirecashwallet.utils.NavigationUtils;

import static cryptodezirecash.org.cryptodezirecashwallet.service.IntentsConstants.ACTION_BROADCAST_TRANSACTION;
import static cryptodezirecash.org.cryptodezirecashwallet.service.IntentsConstants.DATA_TRANSACTION_HASH;

/**
 * Created by akshaynexus on 7/24/17.
 */

public class DonateActivity extends BaseDrawerActivity {

    private View root;
    private EditText edit_amount;
    private Button btn_donate;
    private SimpleTextDialog errorDialog;

    @Override
    protected void onCreateView(Bundle savedInstanceState, ViewGroup container) {
        root = getLayoutInflater().inflate(R.layout.donations_fragment,container);
        edit_amount = (EditText) root.findViewById(R.id.edit_amount);
        btn_donate = (Button) root.findViewById(R.id.btn_donate);
        //setTheme(android.R.style.DeviceDefault_ButtonBar);
        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    send();
                }catch (Exception e){
                    e.printStackTrace();
                    showErrorDialog(e.getMessage());
                }
            }
        });
    }


    private void send() {
        try {
            // create the tx
            String addressStr = CryptoDezireCashContext.DONATE_ADDRESS;
            if (!cryptodezirecashModule.chechAddress(addressStr))
                throw new IllegalArgumentException("Address not valid");
            String amountStr = edit_amount.getText().toString();
            if (amountStr.length() < 1) throw new IllegalArgumentException("Amount not valid");
            if (amountStr.length()==1 && amountStr.equals(".")) throw new IllegalArgumentException("Amount not valid");
            if (amountStr.charAt(0)=='.'){
                amountStr = "0"+amountStr;
            }
            Coin amount = Coin.parseCoin(amountStr);
            if (amount.isZero()) throw new IllegalArgumentException("Amount zero, please correct it");
            if (amount.isLessThan(Transaction.MIN_NONDUST_OUTPUT)) throw new IllegalArgumentException("Amount must be greater than the minimum amount accepted from miners, "+Transaction.MIN_NONDUST_OUTPUT.toFriendlyString());
            if (amount.isGreaterThan(Coin.valueOf(cryptodezirecashModule.getAvailableBalance())))
                throw new IllegalArgumentException("Insuficient balance");
            String memo = "Donation!";
            // build a tx with the default fee
    
            Transaction transaction = cryptodezirecashModule.buildSendTx(addressStr, amount, memo,cryptodezirecashModule.getReceiveAddress());
            // send it
            cryptodezirecashModule.commitTx(transaction);
            Intent intent = new Intent(DonateActivity.this, CryptoDezireCashWalletService.class);
            intent.setAction(ACTION_BROADCAST_TRANSACTION);
            intent.putExtra(DATA_TRANSACTION_HASH,transaction.getHash().getBytes());
            startService(intent);

            Toast.makeText(this,R.string.donation_thanks,Toast.LENGTH_LONG).show();
            onBackPressed();

        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Insuficient balance");
        }
    }

    private void showErrorDialog(String message) {
        if (errorDialog==null){
            errorDialog = DialogsUtil.buildSimpleErrorTextDialog(this,getResources().getString(R.string.invalid_inputs),message);
        }else {
            errorDialog.setBody(message);
        }
        errorDialog.show(getFragmentManager(),getResources().getString(R.string.send_error_dialog_tag));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationUtils.goBackToHome(this);
    }
}
