package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class ActorOriginEvent extends SelectedActorEvent{
    public float originX ,originY;
    private float oldOrginX,oldOrginY;
    private Actor actor;
    public int align=-1;

    public ActorOriginEvent(int align){
        this.align = align;
    }

    public ActorOriginEvent (float originX,float originY){
        this.originX = originX;
        this.originY = originY;
    }

    @Override
    public Event redo() {
        if (actor == null){
            actor = selectGroup.getLastSelectActor();
        }
        if (actor == null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        oldOrginX = actor.getOriginX();
        oldOrginY = actor.getOriginY();
        if (align == -1){
            actor.setOrigin(originX,originY);
        }else {
            actor.setOrigin(align);
        }
        return this;
    }

    @Override
    public ActorOriginEvent undo() {
        actor.setOrigin(oldOrginX,oldOrginY);
        return this;
    }
}
