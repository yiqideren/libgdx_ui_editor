package xyz.white.editor.events.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class SureActorEvent implements Event {
    public Actor actor;
    public SureActorEvent (Actor actor){
        this.actor = actor;
    }

    @Override
    public Event redo() {
        return null;
    }

    @Override
    public SureActorEvent undo() {
        if (actor != null){
            actor.remove();
        }
        return this;
    }
}
