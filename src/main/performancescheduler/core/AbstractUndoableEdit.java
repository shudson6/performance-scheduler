package performancescheduler.core;

@SuppressWarnings("serial")
public abstract class AbstractUndoableEdit extends javax.swing.undo.AbstractUndoableEdit {
    private final String presentationName;
    
    public AbstractUndoableEdit(String name) {
        presentationName = (name != null ? name : null);
    }
    
    @Override
    public String getPresentationName() {
        return presentationName;
    }
}
