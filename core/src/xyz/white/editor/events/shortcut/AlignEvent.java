package xyz.white.editor.events.shortcut;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class AlignEvent  implements Event{
    public int align;

    public AlignEvent(int align){
        this.align = align;
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
