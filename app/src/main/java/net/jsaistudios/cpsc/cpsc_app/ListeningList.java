package net.jsaistudios.cpsc.cpsc_app;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ip on 8/18/18.
 */

public class ListeningList<E> extends ArrayList<E> {
    public interface ListObserver {
        void notifyItemChanged(int i);
        void notifyDataSetChanged();
    }
    private ListObserver listObserver;
    public boolean add(E e) {
        super.add(e);
        if(listObserver!=null) {
            listObserver.notifyDataSetChanged();
        }
        return true;
    }
    public void add(int index, E element) {
        super.add(index, element);
        if(listObserver!=null) {
            listObserver.notifyItemChanged(index);
        }
    }

    public boolean addAll(Collection<? extends E> c) {
        super.addAll(c);
        if(listObserver!=null) {
            listObserver.notifyDataSetChanged();
        }
        return true;
    }


    public ListObserver getListObserver() {
        return listObserver;
    }

    public void setListObserver(ListObserver listObserver) {
        this.listObserver = listObserver;
    }
}
