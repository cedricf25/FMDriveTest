package net.rncmobile.fmdrivetest.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.databinding.GridviewMonitorInfosTelephonyBinding;
import net.rncmobile.fmdrivetest.models.MyTelephonyFactory;
import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.utils.AppConstants;

import cz.mroczis.netmonster.core.model.signal.SignalGsm;
import cz.mroczis.netmonster.core.model.signal.SignalLte;

/**
 * Created by cedric_f25 on 25.08.2017.
 */

public class GridCellInfoAdapter extends BaseAdapter {
    private static final String TAG = "GridCellInfoAdapter";

    private final Context context;
    private final String[] cellValues;
    private final IMyCell cell;

    private final SharedPreferences mPrefs;

    public GridCellInfoAdapter(Context context, String[] cellValues, IMyCell rnc) {
        this.context = context;
        this.cellValues = cellValues;
        this.cell = rnc;

        mPrefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public View getView(int position, View gridView, ViewGroup parent) {

        try {
            ViewHolder holder;
            if (gridView != null) holder = (ViewHolder) gridView.getTag();
            else {
                GridviewMonitorInfosTelephonyBinding gridviewMonitorInfosTelephonyBinding =
                        GridviewMonitorInfosTelephonyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                gridView = gridviewMonitorInfosTelephonyBinding.getRoot();
                holder = new ViewHolder(gridviewMonitorInfosTelephonyBinding);
                gridView.setTag(holder);
            }

            holder.binding.gridItemLabel.setText(cellValues[position]);

            String cellPosition = cellValues[position];

            if (cellPosition.equals("CI")) holder.binding.gridItemValue.setText(String.valueOf(cell.getLcid()));
            if (cellPosition.equals("CID")) holder.binding.gridItemValue.setText(String.valueOf(cell.getCid()));
            if (cellPosition.equals("eNb"))
                holder.binding.gridItemValue.setText(context.getString(R.string.concatRncCid, cell.getRnc(), cell.getCid()));
            if (cellPosition.equals("LAC") || cellPosition.equals("TAC")) holder.binding.gridItemValue.setText(String.valueOf(cell.getXac()));
            if (cellPosition.equals("RNC")) holder.binding.gridItemValue.setText(String.valueOf(cell.getRnc()));
            if (cellPosition.equals("BSIC")) holder.binding.gridItemValue.setText(String.valueOf(cell.getBsic()));
            if (cellPosition.equals("PSC") || cellPosition.equals("PCI")) holder.binding.gridItemValue.setText(String.valueOf(cell.getPxx()));

            if (cellPosition.equals("ARFCN") || cellPosition.equals("EARFCN")) {
                if (cell.getICell().getBand() != null && cell.getICell().getBand().getChannelNumber() <= 0) {
                    holder.binding.gridItemLabel.setVisibility(View.GONE);
                    holder.binding.gridItemValue.setVisibility(View.GONE);
                } else {
                    holder.binding.gridItemValue.setText(String.valueOf(cell.getICell().getBand().getChannelNumber()));
                    holder.binding.gridItemLabel.setVisibility(View.VISIBLE);
                    holder.binding.gridItemValue.setVisibility(View.VISIBLE);
                }
            }

            if (cellPosition.equals("RSSI") || cellPosition.equals("RSCP") || cellPosition.equals("RSRP"))
                holder.binding.gridItemValue.setText(context.getString(R.string.unit_dbm, cell.getMainSignal()));

            if (cellPosition.equals("TA")) {
                if ((cell.getTech() == 2 && ((SignalGsm) cell.getICell().getSignal()).getTimingAdvance() > 0) ||
                        ((cell.getTech() == 4 && ((SignalLte) cell.getICell().getSignal()).getTimingAdvance() > 0))) {
                    if(cell.getTech() == 2)
                        holder.binding.gridItemValue.setText(context.getString(R.string.unit_km, ((SignalGsm) cell.getICell().getSignal()).getTimingAdvance()
                            * (Integer.parseInt(mPrefs.getString("ta_conversion", "78")) / 1000.0)));
                    if(cell.getTech() == 4)
                        holder.binding.gridItemValue.setText(context.getString(R.string.unit_km, ((SignalLte) cell.getICell().getSignal()).getTimingAdvance()
                            * (Integer.parseInt(mPrefs.getString("ta_conversion", "78")) / 1000.0)));
                    holder.binding.gridItemLabel.setVisibility(View.VISIBLE);
                    holder.binding.gridItemValue.setVisibility(View.VISIBLE);
                } else {
                    holder.binding.gridItemLabel.setVisibility(View.GONE);
                    holder.binding.gridItemValue.setVisibility(View.GONE);
                }
            }

            if (cell.getTech() == 4) {
                if (cellPosition.equals("RSSI") && ((SignalLte) cell.getICell().getSignal()).getRssi() != null)
                    holder.binding.gridItemValue.setText(context.getString(R.string.unit_dbm, (double) ((SignalLte) cell.getICell().getSignal()).getRssi()));
                if (cellPosition.equals("RSRQ") && ((SignalLte) cell.getICell().getSignal()).getRsrq() != null)
                    holder.binding.gridItemValue.setText(context.getString(R.string.unit_dbm, ((SignalLte) cell.getICell().getSignal()).getRsrq()));
                if (cellPosition.equals("SNR")) {
                    if (cell.getSnr() <= 0) {
                        holder.binding.gridItemValue.setText("-");
                    } else {
                        holder.binding.gridItemValue.setText(context.getString(R.string.unit_db, ((SignalLte) cell.getICell().getSignal()).getSnr()));
                    }
                }
            }

            if (cellPosition.equals("BW")) {
                holder.binding.gridItemValue.setText(MyTelephonyFactory.getInstance().get(context).getBandWidth(cell.getBandwith()));
            }
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite : " + e.toString());
        }

        return gridView;
    }

    static final class ViewHolder {
        public GridviewMonitorInfosTelephonyBinding binding;

        ViewHolder(GridviewMonitorInfosTelephonyBinding binding) {
            this.binding = binding;
        }
    }

    @Override
    public int getCount() {
        return cellValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
