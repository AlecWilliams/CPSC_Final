package net.jsaistudios.cpsc.cpsc_app.Calendar;

import hirondelle.date4j.DateTime;

public class DailyEvent {

    public String title;

    public DateTime date;

    public DailyEvent(DateTime d, String t) {
        title=t;
        date = d;
    }
}