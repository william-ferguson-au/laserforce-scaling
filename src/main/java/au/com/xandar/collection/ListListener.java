package au.com.xandar.collection;

/**
 * Identifies an Object interested in events that occur to an ImportFileDefinitionList
 * <p/>
 * User: William
 * Date: 26/05/2010
 * Time: 7:42:43 PM
 */
public interface ListListener<T> {
    public void onAdd(T fileDefinition, int indexOfAddedItem);
    public void onRemove(T fileDefinition, int indexOfRemovedItem);
}