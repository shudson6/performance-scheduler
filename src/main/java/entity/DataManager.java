package main.java.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.util.UUIDGenerator;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

public class DataManager<T> {
    private Map<UUID, Item> data = new HashMap<>();
    private UUIDGenerator uuidGen = new UUIDGenerator();
    private List<DataListener> listeners = new ArrayList<>();
    
    /**
     * Add an item to be managed by this instance. If the item does not represent the current
     * state of any item within the underlying data structure, it will be added as both the
     * original and current state of a new entry and will be assigned a new {@code UUID}.
     * 
     * @param item the item to add, not {@code null}
     * @return {@code true} if the data structure changes as a result of this operation
     */
    public boolean addItem(T item) {
        if (_add(item)) {
            fireDataChanged(DataEvent.createAddEvent(this, item));
            return true;
        }
        return false;
    }
    
    /**
     * Add multiple objects to this instance. Only those items that are not already being managed
     * (as determined by {@link #contains()}) are added.
     * 
     * @param items the items to add
     * @return {@code true} if the data structure changed as a result of the operation
     */
    public boolean addItems(Collection<T> items) {
        ArrayList<T> added = new ArrayList<>();
        items.forEach(item -> {
            if (_add(item)) {
                added.add(item);
            }
        });
        if (!added.isEmpty()) {
            fireDataChanged(DataEvent.createAddEvent(this, added.toArray()));
            return true;
        }
        return false;
    }
    
    /**
     * Update the state of an item. {@code oldItem} must refer
     * to the current state of an item managed by this instance. {@code newItem} will be set
     * as the current state of that item. {@code newItem} may be {@code null}, signifying that
     * the item has been removed from the active structure; expect it to be removed from or
     * marked inactive on disk; essentially, the item has been deleted.
     * <p>
     * Note that the state replaced by this method will no longer be a part of this structure, 
     * as intermediate states are not stored.
     * 
     * @param oldItem the current state of the item to be updated, not {@code null}.
     * @param newItem the new state of the item after update
     * @return {@code true} if the data structure changes as a result of this operation
     */
    public boolean updateItem(T oldItem, T newItem) {
        if (_update(oldItem, newItem)) {
            fireDataChanged(DataEvent.createUpdateEvent(this, oldItem, newItem));
            return true;
        }
        return false;
    }
    
    /**
     * Update the state of multiple items. The required {@code updateMap} maps the states to be
     * updated (as keys) to their respective new states (as values). Only those states which exist
     * in the underlying data structure are updated; those which do not exist are simply ignored.
     * 
     * @param updateMap mapping of current states to new states
     * @return {@code true} if the data structure changed as a result of the operation
     */
    public boolean updateItems(Map<T, T> updateMap) {
        if (updateMap == null) {
            return false;
        }
        // first, find items to be updated and map them to their uuids
        // this intermediate step protects against errors that occur when one item updates
        // to a state that matches the current state of another item
        Map<Object, Object> updates = new TreeMap<>();
        updateMap.entrySet().forEach(entry -> {
            if (_update(entry.getKey(), entry.getValue())) {
                updates.put(entry.getKey(), entry.getValue());
            }
        });
        if (!updates.isEmpty()) {
            fireDataChanged(DataEvent.createUpdateEvent(this, updates));
            return true;
        }
        return false;
    }
    
    /**
     * Check if an item matches one being managed by this class. This method uses
     * {@link Object#equals(Object)} and only considers the current state of managed objects.
     * <p>
     * Note that {@code null} is not a valid state and is used as an indicator that an item
     * will be removed from or marked inactive in storage. Thus, this method will return 
     * {@code false} if {@code item == null}.
     * 
     * @param item the item being searched for
     * @return {@code true} if {@code item} matches the current state of a managed object
     */
    public boolean contains(T item) {
        if (item != null) {
            for (Item i : data.values()) {
                if (i.current.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get the {@code UUID} of {@code item}.
     * <p>
     * Note that this method only looks at the current state of items. In most cases, the current
     * state of an item is the only valid state that objects should hold references to.
     * 
     * @param item the item in question
     * @return the {@code UUID} of that item. {@code null} if {@code item} is not part of this
     *      structure.
     */
    public UUID getUUID(T item) {
        if (item != null) {
            for (Entry<UUID, Item> entry : data.entrySet()) {
                if (entry.getValue().current.equals(item)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
    
    /**
     * Get the current state of the item associated with {@code id}.
     * 
     * @param id the UUID in question
     * @return the current state of the item. {@code null} if there is no match for {@code id}.
     */
    public T getData(UUID id) {
        if (id == null || data.get(id) == null) {
            return null;
        }
        return data.get(id).current;
    }
    
    /**
     * Get a set of current states of all items managed by this instance.
     * 
     * @return new {@code Set} containing current state of all items
     */
    public Set<T> getAll() {
        final Set<T> all = new TreeSet<>();
        data.values().forEach(v -> {
            all.add(v.current);
        });
        return all;
    }
    
    /**
     * Get the original state of the item referred to by {@code id} as it was first added
     * to the manager. Typically, this should match the state that is stored.
     * 
     * @param id the UUID in question
     * @return the item as first added to the structure. {@code null} if there is no match
     *      for {@code id}.
     */
    public T getOriginal(UUID id) {
        if (id == null || data.get(id) == null) {
            return null;
        }
        return data.get(id).original;
    }
    
    /**
     * Determines if the supplied UUID refers to an object being managed by this instance and
     * which has been deleted.
     * <p>
     * An item is considered deleted if it has been updated to {@code null}. In this case, it
     * is marked for deletion or deactivation from storage.
     * 
     * @param id the UUID in question
     * @return {@code true} if the above criteria are met
     */
    public boolean wasDeleted(UUID id) {
        if (id == null || data.get(id) == null) {
            return false;
        }
        return data.get(id).current == null;
    }
    
    /**
     * Adds a {@link DataListener} to the list to be notified when the internal data structure
     * changes.
     * <p>
     * This method is a no-op if {@code dl} is {@code null} or already in the list.
     * 
     * @param dl the {@code DataListener} to be added
     */
    public void addDataListener(DataListener dl) {
        if (dl != null && !listeners.contains(dl)) {
            listeners.add(dl);
        }
    }
    
    /**
     * Removes a {@link DataListener} from the list; it will no longer be notified of changes
     * to the internal data structure.
     * 
     * @param dl the {@code DataListener} to be removed
     */
    public void removeDataListener(DataListener dl) {
        if (dl != null) {
            listeners.remove(dl);
        }
    }
    
    /**
     * Notifies all listeners that the internal data structure changed.
     * 
     * @param event the {@link DataEvent} describing the change
     */
    public void fireDataChanged(DataEvent event) {
        listeners.forEach(dl -> {
            dl.dataChanged(event);
        });
    }
    
    private boolean _add(T item) {
        if (item != null && !contains(item)) {
            data.put(uuidGen.generateUUID(), new Item(item));
            return true;
        }
        return false;
    }
    
    private Item _get(T t) {
        for (Item i : data.values()) {
            if (i.current.equals(t)) {
                return i;
            }
        }
        // unreachable as of 11.29.2019; only called from _update() which calls contains()
        // first, so it will not ask for nonexistent objects
        return null;
    }
    
    private boolean _update(T old, T cur) {
        if (contains(old) && !contains(cur)) {
            _get(old).current = cur;
            return true;
        }
        return false;
    }
    
    /**
     * Used to tie the original and current states of an item within the UUID-mapped structure.
     */
    private class Item {
        final T original;
        T current;
        
        Item(T item) {
            if (item == null) {
                // unreachable as of 11.29.2019; calling methods perform null-check before
                // creating the item
                throw new IllegalArgumentException("DataManager: original item cannot be null.");
            }
            original = item;
            current = item;
        }
    }
}
