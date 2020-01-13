package performancescheduler.core;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import performancescheduler.data.Feature;

@SuppressWarnings("serial")
public class UndoableRemoveFeature extends UndoableFeatureEdit {
    private final Feature removed;
    
    public UndoableRemoveFeature(FeatureDataModel model, Feature f) {
        super(model, "Remove Feature: " + f.getTitle());
        removed = f;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        model.add(removed);
    }
    
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        model.remove(removed);
    }
}
