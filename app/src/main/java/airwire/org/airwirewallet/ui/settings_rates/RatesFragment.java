package airwire.org.airwirewallet.ui.settings_rates;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import airwire.org.airwirewallet.R;
import global.AirWireRate;
import airwire.org.airwirewallet.ui.base.BaseRecyclerFragment;
import airwire.org.airwirewallet.ui.base.tools.adapter.BaseRecyclerAdapter;
import airwire.org.airwirewallet.ui.base.tools.adapter.BaseRecyclerViewHolder;
import airwire.org.airwirewallet.ui.base.tools.adapter.ListItemListeners;

/**
 * Created by akshaynexus on 7/2/17.
 */

public class RatesFragment extends BaseRecyclerFragment<AirWireRate> implements ListItemListeners<AirWireRate> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setEmptyText("No rate available");
        setEmptyTextColor(Color.parseColor("#cccccc"));
        return view;
    }

    @Override
    protected List<AirWireRate> onLoading() {
        return airwireModule.listRates();
    }

    @Override
    protected BaseRecyclerAdapter<AirWireRate, ? extends AirWireRateHolder> initAdapter() {
        BaseRecyclerAdapter<AirWireRate, AirWireRateHolder> adapter = new BaseRecyclerAdapter<AirWireRate, AirWireRateHolder>(getActivity()) {
            @Override
            protected AirWireRateHolder createHolder(View itemView, int type) {
                return new AirWireRateHolder(itemView,type);
            }

            @Override
            protected int getCardViewResource(int type) {
                return R.layout.rate_row;
            }

            @Override
            protected void bindHolder(AirWireRateHolder holder, AirWireRate data, int position) {
                holder.txt_name.setText(data.getCode());
                if (list.get(0).getCode().equals(data.getCode()))
                    holder.view_line.setVisibility(View.GONE);
            }
        };
        adapter.setListEventListener(this);
        return adapter;
    }

    @Override
    public void onItemClickListener(AirWireRate data, int position) {
        airwireApplication.getAppConf().setSelectedRateCoin(data.getCode());
        Toast.makeText(getActivity(),R.string.rate_selected,Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onLongItemClickListener(AirWireRate data, int position) {

    }

    private  class AirWireRateHolder extends BaseRecyclerViewHolder{

        private TextView txt_name;
        private View view_line;

        protected AirWireRateHolder(View itemView, int holderType) {
            super(itemView, holderType);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }
}
