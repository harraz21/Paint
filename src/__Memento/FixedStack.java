package __Memento;

import __Model.Shape;
import command.Command;

import java.util.ArrayList;

public class  FixedStack  {
   private Command[] data;
   private int top=0;
   private int maxSize;
    public  FixedStack(int maxSize)
    {
        this.maxSize=maxSize;
        data=new Command[maxSize];
    }
    public boolean isFull()
    {
        if(top==maxSize)
            return true;
        else return false;
    }
    public boolean isEmpty()
    {
        if (top==0)
            return true;
        else return false;
    }
    public void push(Command Command)
    {
        if(isFull())
        {
            for (int i=0;i<(data.length-1);i++)
            {
                data[i]=data[i+1];
            }
            data[top-1]=Command;
        }else data[top++]= Command;
    }
    public Command pop()
    {
        if (!isEmpty()) {
            top--;
            return data[top];
        }else return null;
    }
    public Command peek()
    {
        if (isEmpty())
            return null;
        else
        {
            Command x=pop();
            push(x);
            return x;
        }
    }
    public void printStack()
    {
        int Top=top-1;
        FixedStack fixedStack=new FixedStack(maxSize);
        while (!isEmpty())
        {
            Command x=pop();
                System.out.println(x.toString());
            fixedStack.push(x);
        }
        while (!fixedStack.isEmpty())
        {
            push(fixedStack.pop());
        }
    }
    public int size()
    {
        return top;
    }
}
