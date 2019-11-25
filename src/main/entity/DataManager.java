package main.entity;

import java.util.Deque;
import java.util.Map;
import java.util.UUID;

public class DataManager<T> {
    /* in storage, every feature and performance item is mapped to a uuid
     * since those classes are immutable, they are stored in a deque (stack)
     * which enables tracking their changes and the uuid lets us know which
     * item must be updated in storage--the item at the bottom of the stack
     * should match the version in storage before the update, and the item
     * at the top of the stack is what storage should update to.
     */
    private Map<UUID, Deque<T>> data;
}
