package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/8 0008.
 */
public class ImagePathEvent implements Event {
    public String imagePath;

    public ImagePathEvent(String imagePath){
        this.imagePath = imagePath;
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
