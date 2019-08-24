package __Memento;

public class CareTaker {
    private UndoRedo listOfStates;

    private Memento currentMemento;

    public CareTaker() {
        listOfStates = new UndoRedo();
    }

    public void addMemento(Memento memento) {
        listOfStates.addToHistory(memento);
    }

    public Memento undoMemento() {
        Memento memento = listOfStates.undoByStack();
        if (memento == null)
            return null;
        else {
            currentMemento = memento;
            return currentMemento;
        }
    }

    public Memento redoByMemento() {
        Memento memento = listOfStates.redoByStack();
        if (memento == null)
            return null;
        else {
            currentMemento = new Memento(memento.getState());
            return currentMemento;
        }
    }
    public void clearPreviousSteps()
    {
        listOfStates=new UndoRedo();
    }


}
