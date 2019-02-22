package cryptodezirecash.org.cryptodezirecashwallet.ui.settings_rates;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cryptodezirecash.org.cryptodezirecashwallet.R;
import global.CryptoDezireCashRate;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.BaseRecyclerFragment;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.tools.adapter.BaseRecyclerAdapter;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.tools.adapter.BaseRecyclerViewHolder;
import cryptodezirecash.org.cryptodezirecashwallet.ui.base.tools.adapter.ListItemListeners;

/**
 * Created by akshaynexus on 7/2/17.
 */

public class RatesFragment extends BaseRecyclerFragment<CryptoDezireCashRate> implements ListItemListeners<CryptoDezireCashRate> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setEmptyText("No rate available");
        setEmptyTextColor(Color.parseColor("#cccccc"));
        return view;
    }

    @Override
    protected List<CryptoDezireCashRate> onLoading() {
        return cryptodezirecashModule.listRates();
    }

    @Override
    protected BaseRecyclerAdapter<CryptoDezireCashRate, ? extends CryptoDezireCashRateHolder> initAdapter() {
        BaseRecyclerAdapter<CryptoDezireCashRate, CryptoDezireCashRateHolder> adapter = new BaseRecyclerAdapter<CryptoDezireCashRate, CryptoDezireCashRateHolder>(getActivity()) {
            @Override
            protected CryptoDezireCashRateHolder createHolder(View itemView, int type) {
                return new CryptoDezireCashRateHolder(itemView,type);
            }

            @Override
            protected int getCardViewResource(int type) {
                return R.layout.rate_row;
            }

            @Override
            protected void bindHolder(CryptoDezireCashRateHolder holder, CryptoDezireCashRate data, int position) {
                holder.txt_name.setText(data.getCode());
                if (list.get(0).getCode().equals(data.getCode()))
                    holder.view_line.setVisibility(View.GONE);
            }
        };
        adapter.setListEventListener(this);
        return adapter;
    }

    @Override
    public void onItemClickListener(CryptoDezireCashRate data, int position) {
        cryptodezirecashApplication.getAppConf().setSelectedRateCoin(data.getCode());
        Toast.makeText(getActivity(),R.string.rate_selected,Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onLongItemClickListener(CryptoDezireCashRate data, int position) {

    }

    private  class CryptoDezireCashRateHolder extends BaseRecyclerViewHolder{

        private TextView txt_name;
        private View view_line;

        protected CryptoDezireCashRateHolder(View itemView, int holderType) {
            super(itemView, holderType);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }
}
