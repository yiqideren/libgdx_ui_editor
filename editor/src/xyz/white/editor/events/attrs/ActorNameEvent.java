package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class ActorNameEvent extends SelectedActorEvent {
    public String name;
    private Actor actor;
    private String orginName;

    public ActorNameEvent(String name){
        this.name = name;
    }


    @Override
    public Event redo() {
        if (actor==null){
            actor = selectGroup.getLastSelectActor();
            orginName = actor.getName();
        }
        if (actor == null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        this.actor.setName(name);
        return this;
    }

    @Override
    public Event undo() {
        this.actor.setName(orginName);
        return this;
    }
}
