package performancescheduler.core;

import java.util.Collection;
import java.util.Objects;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import performancescheduler.data.Performance;

@SuppressWarnings("serial")
public class UndoableAddPerformances extends UndoablePerformanceEdit {
    private Collection<Performance> data;
    
    public UndoableAddPerformances(PerformanceDataModel model, Collection<Performance> added) {
        super(model, (added != null && added.size() > 1) ? "Add Performances" : "Add Performance");
        Objects.requireNonNull(added);
        data = added;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        model.remove(data);
    }
    
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        model.add(data);
    }
}
