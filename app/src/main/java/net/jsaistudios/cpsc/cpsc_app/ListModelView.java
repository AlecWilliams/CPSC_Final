package net.jsaistudios.cpsc.cpsc_app;

/**
 * Created by Alec on 8/16/2018.
 */

public class ListModelView {
    private ListeningList<RecyclerModel> dataModelList = new ListeningList<>();

    public ListeningList<RecyclerModel> getDataModelList() {
        return dataModelList;
    }

    public void setDataModelList(ListeningList<RecyclerModel> dataModelList) {
        this.dataModelList = dataModelList;
    }
}
