package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class ActorPosChangeEvent extends SelectedActorEvent{
    public float x,y,oldX,oldY;
    public int align = Align.bottomLeft;
    private Actor actor;

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
        if (actor == null){
            actor = selectGroup.getLastSelectActor();
        }
        if (actor == null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        oldX = actor.getX();
        oldY = actor.getY();
        actor.setPosition(x,y,align);
        return this;
    }

    @Override
    public ActorPosChangeEvent undo() {
        actor.setPosition(oldX,oldY);
        return this;
    }
}
