package performancescheduler.core;

import java.util.Objects;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import performancescheduler.data.Feature;

@SuppressWarnings("serial")
public class UndoableAddFeature extends AbstractUndoableEdit {
    private String presentationName = "Add Feature";
    private FeatureDataModel model;
    private Feature added;
    
    public UndoableAddFeature(FeatureDataModel model, Feature f) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(f);
        this.model = model;
        added = f;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        model.remove(added);
    }
    
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        model.add(added);
    }
    
    @Override
    public String getPresentationName() {
        return presentationName;
    }
}
