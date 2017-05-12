package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/4/18 0018.
 */
public class LabelWrapEvent extends SelectedActorEvent {
    public boolean isWrap;
    private Label label;

    public LabelWrapEvent(boolean isWrap){
        this.isWrap = isWrap;
    }

    @Override
    public Event redo() {
        if (label == null && selectGroup.getLastSelectActor() instanceof Label){
            label = (Label) selectGroup.getLastSelectActor();
        }
        if (label == null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        label.setWrap(isWrap);
        return this;
    }

    @Override
    public Event undo() {
        label.setWrap(!isWrap);
        return this;
    }
}
