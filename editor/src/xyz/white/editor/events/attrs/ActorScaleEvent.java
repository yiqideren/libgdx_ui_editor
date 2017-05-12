package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/12 0012.
 */
public class ActorScaleEvent implements Event {
    public float scaleX;
    public float scaleY;
    public float orginScaleX;
    public float orginScaleY;
    public Actor actor;

    public ActorScaleEvent(float scaleX,float scaleY){
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setTarget(Actor actor){
        this.actor = actor;
        this.orginScaleX = actor.getScaleX();
        this.orginScaleY = actor.getScaleY();
    }

    @Override
    public Event redo() {
        if (actor!=null){
            actor.setScale(scaleX,scaleY);
        }
        return this;
    }

    @Override
    public Event undo() {
        if (actor!=null){
            actor.setScale(orginScaleX,orginScaleY);
        }
        return this;
    }
}
