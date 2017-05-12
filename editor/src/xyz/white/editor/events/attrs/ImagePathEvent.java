package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/8 0008.
 */
public class ImagePathEvent implements Event {
    public String imagePath;
    public boolean isNine;
    public int[] nines;

    public ImagePathEvent(String imagePath,boolean isNine,int[] nines){
        this.imagePath = imagePath;
        this.isNine = isNine;
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
