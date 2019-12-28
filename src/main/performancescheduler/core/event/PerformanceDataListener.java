package performancescheduler.core.event;

@FunctionalInterface
public interface PerformanceDataListener {
    public void performanceDataChanged(PerformanceEvent event);
}
