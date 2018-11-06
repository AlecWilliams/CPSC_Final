package net.jsaistudios.cpsc.cpsc_app.EventsPage;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;

/**
 * Created by ip on 8/18/18.
 */

public class EventsObject extends ItemObject {
    private String info, date, place;
    private String id;
    private boolean old=false;

    public EventsObject() {
        super();
    }
    public EventsObject(String n, String inf, int im) {
        super(n);
        info = inf;
    }

    public boolean isOld() {
        return old;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
