package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/18 0018.
 */
public class ActorTextEvent implements Event {
    public String msg;
    public char addMsg;
    public ActorTextEvent(String msg,char addMsg){
        this.msg = msg;
        this.addMsg = addMsg;
    }

    @Override
    public Event redo() {
        return null;
    }

    @Override
    public Event undo() {
        return null;
    }
}
