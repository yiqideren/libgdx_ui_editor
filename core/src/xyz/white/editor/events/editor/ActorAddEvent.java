package xyz.white.editor.events.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/19 0019.
 */
public class ActorAddEvent implements Event {
    public Actor actor;

    public ActorAddEvent(Actor actor){
        this.actor = actor;
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
