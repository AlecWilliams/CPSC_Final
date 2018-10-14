package net.jsaistudios.cpsc.cpsc_app.Calendar;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import net.jsaistudios.cpsc.cpsc_app.CaldroidSampleCustomAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomFragment extends CaldroidFragment {
    protected HashMap<String, DailyEvent> events = new HashMap<String, DailyEvent>();



    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        CaldroidSampleCustomAdapter adapter = new CaldroidSampleCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
        DateTime samp = new DateTime(2018, 10, 10, 9, 36, 36, 36);
        events.put("20181010", new DailyEvent(samp, "new ev"));
        adapter.setEvents(events);
        return adapter;
    }
    public void refreshView() {
        if (month == -1 || year == -1) {
            return;
        }

        refreshMonthTitleTextView();
        for (CaldroidGridAdapter adapter : datePagerAdapters) {
            adapter.setCaldroidData(getCaldroidData());
            adapter.setExtraData(extraData);
            ((CaldroidSampleCustomAdapter)adapter).setEvents(events);
            // Refresh view
            adapter.notifyDataSetChanged();
        }
    }
}
