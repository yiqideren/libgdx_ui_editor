package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/18 0018.
 */
public class ActorUnqAttrChangeEvent implements Event {
    public Class classType;
    public ActorUnqAttrChangeEvent(Class classType){
        this.classType = classType;
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
