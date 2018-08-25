package net.jsaistudios.cpsc.cpsc_app;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Alec on 7/4/2018.
 */

public class RecyclerModel {
    private ItemObject itemObject;
    private DatabaseReference databaseNodeReference=null;

    public DatabaseReference getDatabaseNodeReference() {
        return databaseNodeReference;
    }

    public void setDatabaseNodeReference(DatabaseReference databaseNodeReference) {
        this.databaseNodeReference = databaseNodeReference;
    }

    public ItemObject getItemObject() {
        return itemObject;
    }

    public void setItemObject(ItemObject itemObject) {
        this.itemObject = itemObject;
    }
}
