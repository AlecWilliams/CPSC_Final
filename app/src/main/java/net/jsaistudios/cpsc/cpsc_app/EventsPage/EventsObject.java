package net.jsaistudios.cpsc.cpsc_app.EventsPage;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;

/**
 * Created by ip on 8/18/18.
 */

public class EventsObject extends ItemObject {
    private String info;
    private int image;

    public EventsObject() {
        super();
    }
    public EventsObject(String n, String inf, int im) {
        super(n);
        info = inf;
        image = im;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
