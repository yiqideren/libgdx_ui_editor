package xyz.white.editor.events.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;


/**
 * Created by 10037 on 2017/4/18 0018.
 */
public class ActorTextEvent extends SelectedActorEvent{
    public String msg,orginMsg;
    private Label label;
    public ActorTextEvent(String msg){
        this.msg = msg;
    }

    @Override
    public Event redo() {
        if (label == null && selectGroup.getLastSelectActor() instanceof Label){
            label = (Label) selectGroup.getLastSelectActor();
        }

        if (label ==null){
            isUndo = false;
            return this;
        }
        isUndo = true;
        orginMsg = label.getText().toString();
        label.setText(msg);
        return this;
    }

    @Override
    public Event undo() {
        label.setText(orginMsg);
        return this;
    }
}
