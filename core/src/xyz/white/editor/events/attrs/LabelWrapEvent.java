package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/18 0018.
 */
public class LabelWrapEvent implements Event {
    public boolean isWrap;

    public LabelWrapEvent(boolean isWrap){
        this.isWrap = isWrap;
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
