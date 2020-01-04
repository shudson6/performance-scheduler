package performancescheduler.core.event;

@FunctionalInterface
public interface ScheduleDataListener<T> {
    public void scheduleDataChanged(ScheduleEvent<T> event);
}
