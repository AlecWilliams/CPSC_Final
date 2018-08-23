package net.jsaistudios.cpsc.cpsc_app;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Alec on 7/4/2018.
 */

public class RecyclerModel {
    private ItemObject itemObject;
    private DataSnapshot databaseNodeReference;

    public DataSnapshot getDatabaseNodeReference() {
        return databaseNodeReference;
    }

    public void setDatabaseNodeReference(DataSnapshot databaseNodeReference) {
        this.databaseNodeReference = databaseNodeReference;
    }

    public ItemObject getItemObject() {
        return itemObject;
    }

    public void setItemObject(ItemObject itemObject) {
        this.itemObject = itemObject;
    }
}
