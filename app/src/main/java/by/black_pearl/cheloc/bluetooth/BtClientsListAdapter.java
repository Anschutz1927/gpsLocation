package by.black_pearl.cheloc.bluetooth;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import by.black_pearl.cheloc.R;

/**
 * This is class adapter for listview with custom layout (bt_clients_list_layout) with two textviews.
 */
public class BtClientsListAdapter extends SimpleAdapter{
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private ArrayList<HashMap<String, String>> list;

    /**
     * BtClientsListAdapter provide you data to custom layout like bt_clients_list_layout
     * with two TextViews that have id like:
     * tv1_id = nameListLayoutTextView
     * tv2_id = addressListLayoutTextView.
     * To insert data to ListView vith Device Name at 1st TextView and Device Address at 2nd TextView.
     * @param context - get context from base activity.
     * @param list - list of data to insert to adapter.
     *             To create clearable list adapter, You can insert her new list like that:
     *             "new ArrayList<HashMap<String, String>>()"
     */
       public BtClientsListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
           super(context,
                   list,
                   R.layout.bt_clients_list_layout,
                   new String[] {COLUMN_NAME, COLUMN_ADDRESS},
                   new int[] {R.id.nameListLayoutTextView, R.id.addressListLayoutTextView});
           this.list = list;
       }

    /**
     * Call this method to past new device to BtClientsListAdapter.
     * @param name
     * @param address
     */
    public void add(String name, String address) {
        HashMap<String, String> map = new HashMap<>();
        map.put(COLUMN_NAME, name);
        map.put(COLUMN_ADDRESS, address);
        this.list.add(map);
        this.notifyDataSetChanged();
    }
}
