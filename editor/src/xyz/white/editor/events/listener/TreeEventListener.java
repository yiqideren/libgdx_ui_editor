package xyz.white.editor.events.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.Subscribe;
import xyz.white.editor.events.tree.TreeActorMoveEvent;
import xyz.white.editor.events.tree.TreeCancelEvent;
import xyz.white.editor.events.tree.TreeSelectedActroEvent;

/**
 * Created by chenxiang on 2017/4/19.
 */
public interface TreeEventListener {
    @Subscribe
    void selecedActor(TreeSelectedActroEvent event) ;

    @Subscribe
    void cancelActor(TreeCancelEvent event);

    @Subscribe
    void moveActor(TreeActorMoveEvent event);
}
