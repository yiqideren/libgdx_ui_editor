package xyz.white.editor.events;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public interface Event {
    public Event redo();
    public Event undo();
}
