package performancescheduler.core;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import performancescheduler.data.Feature;

@SuppressWarnings("serial")
public class UndoableAddFeature extends UndoableFeatureEdit {
    private Feature added;
    
    public UndoableAddFeature(FeatureDataModel model, Feature f) {
        super(model, "Add Feature: " + f.getTitle());
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
}
