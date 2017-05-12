package xyz.white.editor.events.attrs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/5/13 0013.
 */
public class ActorColorEvent extends SelectedActorEvent {
    private Color color,oldColor;
    private Actor actor;

    public ActorColorEvent(Color color){
        this.color = color;
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
        oldColor = actor.getColor();
        actor.setColor(color);
        return super.redo();
    }

    @Override
    public Event undo() {
        actor.setColor(oldColor);
        return super.undo();
    }
}
