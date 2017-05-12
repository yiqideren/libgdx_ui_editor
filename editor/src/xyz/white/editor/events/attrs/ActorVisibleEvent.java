package xyz.white.editor.events.attrs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class ActorVisibleEvent extends SelectedActorEvent {
    public boolean isVisible;
    private Actor target;

    public ActorVisibleEvent(boolean isVisible){
        this.isVisible = isVisible;
    }


    @Override
    public Event redo() {
        if (target == null){
            target = selectGroup.getLastSelectActor();
        }
        if (target == null) {
            isUndo  = false;
            return this;
        }
        isUndo = true;
        this.target.setVisible(isVisible);
        return this;
    }

    @Override
    public Event undo() {
        Gdx.app.log("app","ActorVisible" + String.valueOf(isVisible));
        this.target.setVisible(!isVisible);
        return this;
    }
}
