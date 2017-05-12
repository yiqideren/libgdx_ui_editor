package xyz.white.editor.events.listener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.Subscribe;

import xyz.white.editor.events.Event;
import xyz.white.editor.events.SelectedActorEvent;
import xyz.white.editor.events.attrs.*;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public interface ChangeActorAttrListener {




    @Subscribe
    void setImagePath(ImagePathEvent event);
    @Subscribe
    void setNineDrawable(NineDrawableEvent event);

    @Subscribe
    void setButtonPath(ButtonPathEvent event);


    @Subscribe
    void selecedActorEvent(SelectedActorEvent event);
}
