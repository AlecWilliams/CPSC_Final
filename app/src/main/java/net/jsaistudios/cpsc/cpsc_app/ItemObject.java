package net.jsaistudios.cpsc.cpsc_app;

/**
 * Created by ip on 8/18/18.
 */

public class ItemObject {
    private String name="";
    public ItemObject() {

    }
    public ItemObject(String n) {
        name = n;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
