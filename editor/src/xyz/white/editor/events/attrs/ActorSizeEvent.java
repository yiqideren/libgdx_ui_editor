package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class ActorSizeEvent extends SelectedActorEvent{
    public float width,height;
    private float oldWidth,oldHeight;
    private Actor actor;

    public ActorSizeEvent (float width,float height){
        this.width = width;
        this.height = height;
    }

    @Override
    public Event redo() {
        if (actor == null){
            actor = selectGroup.getLastSelectActor();
        }

        if (actor ==null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        oldWidth = actor.getWidth();
        oldHeight = actor.getHeight();
        actor.setSize(width,height);
        return this;
    }

    @Override
    public ActorSizeEvent undo() {
        actor.setSize(oldWidth,oldHeight);
        return this;
    }
}
