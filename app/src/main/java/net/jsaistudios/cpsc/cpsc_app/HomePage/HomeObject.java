package net.jsaistudios.cpsc.cpsc_app.HomePage;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;

public class HomeObject extends ItemObject {
    private String info, address;
    private int image;

    public HomeObject() {
        super();
    }
    public HomeObject(String n, String inf, int im) {
        super(n);
        info = inf;
        image = im;
    }

    public HomeObject(String n, String a) {
        super(n);
        setAddress(a);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
