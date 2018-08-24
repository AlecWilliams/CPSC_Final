package net.jsaistudios.cpsc.cpsc_app;

/**
 * Created by ip on 8/18/18.
 */

public class BoardObject extends ItemObject {
    private String info;
    private int image;
    private String bio;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public BoardObject() {
        super();
    }
    public BoardObject(String n, String inf, int im) {
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
