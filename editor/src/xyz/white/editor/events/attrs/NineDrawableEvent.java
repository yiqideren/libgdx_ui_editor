package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/12 0012.
 */
public class NineDrawableEvent implements Event {
    public int[] nines;

    public NineDrawableEvent(int[] nines){
        this.nines = nines;
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
