package xyz.white.editor.events.attrs;

import com.badlogic.gdx.utils.Align;
import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class ActorPosChangeEvent implements Event {
    public float x,y;
    public int align = Align.bottomLeft;

    public ActorPosChangeEvent(float x,float y){
        this.x = x;
        this.y = y;
    }

    public ActorPosChangeEvent (float x,float y,int align){
        this.x = x;
        this.y = y;
        this.align = align;
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
