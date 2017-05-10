package xyz.white.editor.events.listener;

import com.google.common.eventbus.Subscribe;

import xyz.white.editor.events.attrs.ActorNameEvent;
import xyz.white.editor.events.editor.ActorAddEvent;
import xyz.white.editor.events.editor.RefreshWindowEvent;
import xyz.white.editor.events.editor.SureActorEvent;
import xyz.white.editor.events.editor.AttrEvent;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public interface EditorEventListener {

    @Subscribe
    void changePos(AttrEvent attrEvent);

    @Subscribe
    void addActor(ActorAddEvent event);

    @Subscribe
    void sureActor(SureActorEvent sureActorEvent);

    @Subscribe
    void refreshWindow(RefreshWindowEvent event);

    @Subscribe
    void setActorName(ActorNameEvent event);
}

