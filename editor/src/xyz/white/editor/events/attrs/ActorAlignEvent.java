package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/4/19 0019.
 */
public class ActorAlignEvent extends SelectedActorEvent {
    public int align;
    private Label label;
    private int orginAlign;

    public ActorAlignEvent(int align){
        this.align = align;
    }

    @Override
    public Event redo() {
        if (label==null&&selectGroup.getLastSelectActor() instanceof Label){
            label = (Label) selectGroup.getLastSelectActor();

        }
        if (label == null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        orginAlign = label.getLabelAlign();
        label.setAlignment(align);
        return this;
    }

    @Override
    public Event undo() {
        label.setAlignment(orginAlign);
        return this;
    }
}
