package performancescheduler.core;

import java.util.Collection;
import java.util.Objects;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import performancescheduler.data.Performance;

@SuppressWarnings("serial")
public class UndoableAddPerformances extends AbstractUndoableEdit {
    private String presentationName = "Add Performance";
    private Collection<Performance> data;
    private PerformanceDataModel model;
    
    public UndoableAddPerformances(PerformanceDataModel model, Collection<Performance> added) {
        Objects.requireNonNull(model);
        this.model = model;
        if (added != null && added.size() != 1) {
            presentationName += "s";
        }
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
    
    @Override
    public String getPresentationName() {
        return presentationName;
    }
}
