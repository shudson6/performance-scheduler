package performancescheduler.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import performancescheduler.data.Performance;

@SuppressWarnings("serial")
public class UndoableMovePerformances extends UndoablePerformanceEdit {
    private Map<Performance, Performance> updates;
    
    public UndoableMovePerformances(PerformanceDataModel model, Map<Performance, Performance> updates) {
        super(model, (updates != null && updates.size() > 1) ? "Move Performances" : "Move Performance");
        Objects.requireNonNull(updates);
        this.updates = updates;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        Map<Performance, Performance> undos = new HashMap<>(updates.size() * 2);
        updates.forEach((k, v) -> undos.put(v, k));
        model.update(undos);
    }
    
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        model.update(updates);
    }
}
