package net.jsaistudios.cpsc.cpsc_app;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Alec on 7/4/2018.
 */

public class RecyclerModel {
    private ItemObject itemObject;
    private DataSnapshot perkDatabaseNode;

    public DataSnapshot getPerkDatabaseNode() {
        return perkDatabaseNode;
    }

    public void setPerkDatabaseNode(DataSnapshot perkDatabaseNode) {
        this.perkDatabaseNode = perkDatabaseNode;
    }

    public ItemObject getItemObject() {
        return itemObject;
    }

    public void setItemObject(ItemObject itemObject) {
        this.itemObject = itemObject;
    }
}
