package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/4/23 0023.
 */
public class ActorRotationEvent extends SelectedActorEvent {
    public float rotation,orginRotation;
    private Actor actor;

    public ActorRotationEvent(float rotation){
        this.rotation = rotation;
    }

    @Override
    public Event redo() {
        if (actor == null){
            actor = selectGroup.getLastSelectActor();
            orginRotation = actor.getRotation();
        }
        if (actor == null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        actor.setRotation(rotation);
        return this;
    }

    @Override
    public Event undo() {
        actor.setRotation(orginRotation);
        return this;
    }
}
