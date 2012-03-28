package au.com.xandar.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Maintains a List that notifies Listeners of changes.
 * <p/>
 * User: William
 * Date: 26/05/2010
 * Time: 7:13:41 PM
 */
public final class SimpleList<T> implements Iterable<T> {

    private final List<T> items = new ArrayList<T>();
    private final List<ListListener<T>> listeners = new ArrayList<ListListener<T>>();

    public void add(T item) {
        final int indexOfNewItem  = items.size();
        items.add(item);
        for (final ListListener<T> listener : listeners) {
            listener.onAdd(item, indexOfNewItem);
        }
    }

    public void remove(T item) {
        final int indexOfRemovedItem = items.indexOf(item);
        if (indexOfRemovedItem == -1) {
            return;
        }
        
        items.remove(indexOfRemovedItem);
        for (final ListListener<T> listener : listeners) {
            listener.onRemove(item, indexOfRemovedItem);
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public T get(int index) {
        return items.get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    public void addListListener(ListListener<T> listener) {
        listeners.add(listener);
    }

    public void removeImportFileDefinitionListener(ListListener<T> listener) {
        listeners.remove(listener);
    }
}