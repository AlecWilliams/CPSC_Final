package net.jsaistudios.cpsc.cpsc_app.EventsPage;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;

import java.util.Date;

/**
 * Created by ip on 8/18/18.
 */

public class EventsObject extends ItemObject {
    private String info, place;
    private Date date;
    private String id;

    public EventsObject() {
        super();
    }
    public EventsObject(String n, String inf, int im) {
        super(n);
        info = inf;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
