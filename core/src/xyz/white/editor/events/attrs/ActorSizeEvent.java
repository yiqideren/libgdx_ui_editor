package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class ActorSizeEvent implements Event {
    public float width,height;

    public ActorSizeEvent (float width,float height){
        this.width = width;
        this.height = height;
    }

    @Override
    public Event redo() {
        return null;
    }

    @Override
    public ActorSizeEvent undo() {
        return this;
    }
}
