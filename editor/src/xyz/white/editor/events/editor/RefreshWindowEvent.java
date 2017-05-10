package xyz.white.editor.events.editor;

import com.badlogic.gdx.scenes.scene2d.Group;
import xyz.white.editor.events.Event;


/**
 * Created by 10037 on 2017/5/4 0004.
 */
public class RefreshWindowEvent implements Event {

    public Group mainWindow;

    public RefreshWindowEvent(Group mainWindow){
        this.mainWindow = mainWindow;
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
