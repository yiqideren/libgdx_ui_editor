package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class ActorOriginEvent implements Event {
    public float originX ,originY;
    public int align=-1;

    public ActorOriginEvent(int align){
        this.align = align;
    }

    public ActorOriginEvent (float originX,float originY){
        this.originX = originX;
        this.originY = originY;

    }

    @Override
    public Event redo() {
        return null;
    }

    @Override
    public ActorOriginEvent undo() {
        return this;
    }
}
