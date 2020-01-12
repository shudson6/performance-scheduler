package performancescheduler.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;

public abstract class DataManager<T> {
	protected final ScheduleDataModel<T> model;
	private List<UndoableEditListener> undoListenerList = new ArrayList<>();
	
	public DataManager(ScheduleDataModel<T> model) {
		Objects.requireNonNull(model);
		this.model = model;
	}
	
	public ScheduleDataModel<T> getModel() {
		return model;
	}
	
	public void addUndoableEditListener(UndoableEditListener listener) {
	    if (!undoListenerList.contains(listener)) {
	        undoListenerList.add(listener);
	    }
	}
	
	public void removeUndoableEditListener(UndoableEditListener listener) {
	    undoListenerList.remove(listener);
	}
	
	public void fireUndoableEdit(UndoableEdit edit) {
	    undoListenerList.forEach(l -> l.undoableEditHappened(new UndoableEditEvent(this, edit)));
	}
}
