package xyz.white.editor.events.tree;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import xyz.white.editor.events.Event;

/**
 * Created by chenxiang on 2017/4/19.
 */
public class TreeSelectedActroEvent implements Event {
    public Array<Actor> actors;

    public TreeSelectedActroEvent(Array actors){
        this.actors = actors;
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
