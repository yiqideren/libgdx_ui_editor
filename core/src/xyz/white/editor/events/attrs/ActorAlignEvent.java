package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/19 0019.
 */
public class ActorAlignEvent implements Event {
    public int align;

    public ActorAlignEvent(int align){
        this.align = align;
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
