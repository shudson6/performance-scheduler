package performancescheduler.core;

import java.util.Objects;

@SuppressWarnings("serial")
public class UndoableFeatureEdit extends AbstractUndoableEdit {
    protected final FeatureDataModel model;

    public UndoableFeatureEdit(FeatureDataModel model, String name) {
        super(name);
        Objects.requireNonNull(model);
        this.model = model;
    }
}
