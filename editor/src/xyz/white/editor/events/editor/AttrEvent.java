package xyz.white.editor.events.editor;


import com.badlogic.gdx.scenes.scene2d.Actor;

import xyz.white.editor.events.Event;

public class AttrEvent implements Event {
    public Actor actor;
    public AttrEvent (Actor actor){
        this.actor = actor;
    }


    @Override
    public Event redo() {
        return null;
    }

    @Override
    public AttrEvent undo() {
        return this;
    }
}
