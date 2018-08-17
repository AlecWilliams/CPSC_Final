package net.jsaistudios.cpsc.cpsc_app;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Alec on 7/4/2018.
 */

public class RecyclerPerkModel {
    private String name;
    private String info;
    private int image;
    private DataSnapshot perkDatabaseNode;


    public DataSnapshot getPerkDatabaseNode() {
        return perkDatabaseNode;
    }

    public void setPerkDatabaseNode(DataSnapshot perkDatabaseNode) {
        this.perkDatabaseNode = perkDatabaseNode;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
