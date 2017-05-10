package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class ActorNameEvent implements Event {
    public String name;
    private Actor actor;
    private String orginName;

    public ActorNameEvent(String name){
        this.name = name;
    }

    public void setTarget(Actor actor){
        this.actor =actor;
        this.orginName = actor.getName();
    }

    @Override
    public Event redo() {
        this.actor.setName(name);
        return this;
    }

    @Override
    public Event undo() {
        this.actor.setName(orginName);
        return this;
    }
}
