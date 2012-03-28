package au.com.xandar.swing;

import au.com.xandar.collection.ListListener;
import au.com.xandar.collection.SimpleList;

import javax.swing.*;

/**
 * Maps a SimpleList to a swing ListModel.
 * <p/>
 * User: William
 * Date: 27/05/2010
 * Time: 1:24:34 PM
 */
public final class SimpleListModel<T> extends AbstractListModel {

    private final SimpleList<T> list;

    public SimpleListModel(SimpleList<T> list) {
        this.list = list;

        // Ensure that whenever the list changes the ListModel changes too.
        list.addListListener(new ListListener<T>() {
            @Override
            public void onAdd(T fileDefinition, int indexOfAddedItem) {
                SimpleListModel.this.fireIntervalAdded(SimpleListModel.this, indexOfAddedItem, indexOfAddedItem);
            }

            @Override
            public void onRemove(T fileDefinition, int indexOfRemovedItem) {
                SimpleListModel.this.fireIntervalRemoved(SimpleListModel.this, indexOfRemovedItem, indexOfRemovedItem);
            }
        });
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }
}
