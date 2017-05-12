package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/5/12 0012.
 */
public class ActorScaleEvent extends SelectedActorEvent {
    public float scaleX;
    public float scaleY;
    public float orginScaleX;
    public float orginScaleY;
    public Actor actor;

    public ActorScaleEvent(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }


    @Override
    public Event redo() {
        if (actor == null) {
            actor = selectGroup.getLastSelectActor();
        }
        if (actor == null) {
            isUndo = false;
            return this;
        }
        isUndo = true;
        orginScaleX = actor.getScaleX();
        orginScaleY = actor.getScaleY();
        actor.setScale(scaleX, scaleY);
        return this;
    }

    @Override
    public Event undo() {
        if (actor != null) {
            actor.setScale(orginScaleX, orginScaleY);
        }
        return this;
    }
}
