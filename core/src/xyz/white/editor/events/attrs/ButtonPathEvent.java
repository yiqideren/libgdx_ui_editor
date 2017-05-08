package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/8 0008.
 */
public class ButtonPathEvent implements Event {
    public String up;
    public String down;
    public String check;

    public ButtonPathEvent(String up,String down,String check){
        this.up = up;
        this.down = down;
        this.check = check;
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
