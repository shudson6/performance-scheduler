package performancescheduler.core;

import java.util.Objects;

@SuppressWarnings("serial")
public class UndoablePerformanceEdit extends AbstractUndoableEdit {
    protected final PerformanceDataModel model;
    
    public UndoablePerformanceEdit(PerformanceDataModel model, String name) {
        super(name);
        Objects.requireNonNull(model);
        this.model = model;
    }
    
}
