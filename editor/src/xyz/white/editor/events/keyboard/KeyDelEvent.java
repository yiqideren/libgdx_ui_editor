package xyz.white.editor.events.keyboard;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/23 0023.
 */
public class KeyDelEvent implements Event {

    public KeyDelEvent(){

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
