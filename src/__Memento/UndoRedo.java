package __Memento;

public class UndoRedo {
    private FixedStack undo;
    private FixedStack redo;
    private Memento currentState;

    public UndoRedo() {
        this.redo = new FixedStack(20);
        this.undo = new FixedStack(20);
    }

    public void addToHistory(Memento memento) {
        if (memento != null) {
            undo.push(memento.getState());
            currentState = memento;
            redo = new FixedStack(20);
        }
    }

    public Memento undoByStack() {
        if (undo.size() == 0)
            return null;
        redo.push(currentState.getState());
        currentState = new Memento(undo.pop());
        return currentState;
    }

    public Memento redoByStack() {
        if (redo.size() == 0)
            return null;
        undo.push(currentState.getState());
        currentState = new Memento(redo.pop());
        return currentState;
    }
}
