package performancescheduler.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;

@SuppressWarnings("serial")
public class UndoManager extends javax.swing.undo.UndoManager {
    private enum Action {UNDO, REDO};
    private UndoAction undoAction = new UndoAction(Action.UNDO);
    private UndoAction redoAction = new UndoAction(Action.REDO);
    
    public UndoManager() {
        update();
    }
    
    public UndoAction getUndoAction() {
        return undoAction;
    }
    
    public UndoAction getRedoAction() {
        return redoAction;
    }
    
    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        super.undoableEditHappened(e);
        update();
    }

    private void update() {
        undoAction.setEnabled(canUndo());
        undoAction.putValue(AbstractAction.NAME, getUndoPresentationName());
        redoAction.setEnabled(canRedo());
        redoAction.putValue(AbstractAction.NAME, getRedoPresentationName());
    }
    
    public class UndoAction extends AbstractAction {
        private final Action action;
        
        public UndoAction(Action a) {
            action = a;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (action.equals(Action.UNDO)) {
                undo();
            } else {
                redo();
            }
            update();
        }
    }
}
