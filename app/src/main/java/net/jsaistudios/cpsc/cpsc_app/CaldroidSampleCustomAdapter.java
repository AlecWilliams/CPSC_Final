package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CellView;

import net.jsaistudios.cpsc.cpsc_app.Calendar.DailyEvent;

import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
    protected HashMap<String, DailyEvent> events = new HashMap<String, DailyEvent>();

    /**
     * Constructor
     *
     * @param context
     * @param month
     * @param year
     * @param caldroidData
     * @param extraData
     */
    public CaldroidSampleCustomAdapter(Context context, int month, int year, Map<String, Object> caldroidData, Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CellView cellView;
        DateTime dateTime = this.datetimeList.get(position);

        // For reuse
        if (convertView == null) {
            final int squareDateCellResource = squareTextViewCell ? com.caldroid.R.layout.square_date_cell : com.caldroid.R.layout.normal_date_cell;
            cellView = (CellView) localInflater.inflate(squareDateCellResource, parent, false);
        } else {
            cellView = (CellView) convertView;
        }
        if (events.containsKey(dateTime.getYear()+""+dateTime.getMonth()+""+dateTime.getDay())) {
            DailyEvent event = events.get(dateTime.getYear()+""+dateTime.getMonth()+""+dateTime.getDay());
            cellView.setBackgroundColor(resources
                    .getColor(R.color.colorPrimary));

        }

        customizeTextView(position, cellView);

        return cellView;
    }

    public void setEvents(HashMap<String, DailyEvent> events) {
        this.events = events;
    }
}
