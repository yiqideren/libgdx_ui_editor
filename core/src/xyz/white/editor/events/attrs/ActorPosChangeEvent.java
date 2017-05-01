package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class ActorPosChangeEvent implements Event {
    public float x,y;

    public ActorPosChangeEvent (float x,float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public Event redo() {
        return null;
    }

    @Override
    public ActorPosChangeEvent undo() {
        return this;
    }
}
