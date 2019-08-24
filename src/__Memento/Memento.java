package __Memento;

import __Model.Shape;
import command.Command;

import java.util.ArrayList;

public class Memento {
    private Command state;

    public Memento(Command command) {
        this.state = command;
    }

    public Command getState() {
        return state;
    }


}
