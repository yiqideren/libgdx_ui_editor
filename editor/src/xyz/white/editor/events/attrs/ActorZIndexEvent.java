package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/5/13 0013.
 */
public class ActorZIndexEvent extends SelectedActorEvent{
    private int zIndex,oldZindex;
    private Actor actor;

    public ActorZIndexEvent(int zIndex){
        this.zIndex = zIndex;
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
        oldZindex = actor.getZIndex();
        actor.setZIndex(zIndex);
        return super.redo();
    }

    @Override
    public Event undo() {
        actor.setZIndex(oldZindex);
        return super.undo();
    }
}
