package net.jsaistudios.cpsc.cpsc_app;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alec on 8/16/2018.
 */

public class ListModelView {
    private ListeningList<RecyclerModel> dataModelList = new ListeningList<>();
    public Observer2<ListeningList<RecyclerModel>> listObserver;

    public ListeningList<RecyclerModel> getDataModelList() {
        return dataModelList;
    }

    public void setDataModelList(ListeningList<RecyclerModel> dataModelList) {
        this.dataModelList = dataModelList;
        if(listObserver!=null) {
            listObserver.update(dataModelList);
        }
    }
    public ListeningList<RecyclerModel> getSortedDataModeLList(Comparator<RecyclerModel> comparator) {
        Collections.sort(dataModelList, comparator);
        return dataModelList;
    }
}
