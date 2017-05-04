package xyz.white.editor.events.listener;

import com.google.common.eventbus.Subscribe;

import xyz.white.editor.events.editor.ActorAddEvent;
import xyz.white.editor.events.editor.RefreshWindowEvent;
import xyz.white.editor.events.editor.SureActorEvent;
import xyz.white.editor.events.editor.AttrEvent;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public interface EditorEventListener {

    @Subscribe
    public void changePos(AttrEvent attrEvent);

    @Subscribe
    public void addActor(ActorAddEvent event);

    @Subscribe
    public void sureActor(SureActorEvent sureActorEvent);

    @Subscribe
    public void refreshWindow(RefreshWindowEvent event);
}

