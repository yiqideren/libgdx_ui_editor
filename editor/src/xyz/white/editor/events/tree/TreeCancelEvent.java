package xyz.white.editor.events.tree;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/4/22 0022.
 */
public class TreeCancelEvent implements Event {

    public TreeCancelEvent(){

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
