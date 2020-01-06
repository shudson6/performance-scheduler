package performancescheduler.core.event;

import java.util.List;

public interface ScheduleEvent<T> {

    int ADD = 0x0001;
    int REMOVE = 0x0002;
    int UPDATE = 0x0003; // add && remove
    int REPLACE = 0x0003; // because I can't make up my mind what to call it

    int getAction();

    List<T> getAdded();

    List<T> getRemoved();
}