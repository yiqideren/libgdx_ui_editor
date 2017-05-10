package xyz.white.editor.events.tree;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/23 0023.
 */
public class TreeActorMoveEvent implements Event{
    public Actor actor;
    public Group group;

    public TreeActorMoveEvent(Actor actor, Group group){
        this.actor = actor;
        this.group = group;
    }

    @Override
    public Event redo() {
        return null;
    }

    @Override
    public Event undo() {
        return null;
    }
}
