package net.jsaistudios.cpsc.cpsc_app.PerkPage;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;

/**
 * Created by ip on 8/18/18.
 */

public class PerkObject extends ItemObject {
    private String info;
    private int image;

    public PerkObject() {
        super();
    }
    public PerkObject(String n, String inf, int im) {
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
