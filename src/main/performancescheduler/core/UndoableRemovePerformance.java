package performancescheduler.core;

import java.util.Collection;
import java.util.Objects;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import performancescheduler.data.Performance;

@SuppressWarnings("serial")
public class UndoableRemovePerformance extends UndoablePerformanceEdit {
    private Collection<Performance> data;
    
    public UndoableRemovePerformance(PerformanceDataModel model, Collection<Performance> toRm) {
        super(model, (toRm != null && toRm.size() > 1) ? "Remove Performances" : "Remove Performance");
        Objects.requireNonNull(toRm);
        data = toRm;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        model.add(data);
    }
    
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        model.remove(data);
    }
}
