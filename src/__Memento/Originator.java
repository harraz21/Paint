package __Memento;

import command.Command;

public class Originator {
    private Command State;

    public void setState(Command state) {
        this.State = state;
    }

    public Memento storeInMemento() {
        return new Memento(State);
    }

    public Command restoreFromMemento(Memento memento) {
        State = memento.getState();
        return State;
    }
}
