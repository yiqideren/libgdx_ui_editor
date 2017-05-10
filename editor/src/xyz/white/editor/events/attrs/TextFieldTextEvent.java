package xyz.white.editor.events.attrs;

import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class TextFieldTextEvent implements Event {
    public String text;
    public String message;

    public TextFieldTextEvent(String text,String message){
        this.text = text;
        this.message = message;
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
