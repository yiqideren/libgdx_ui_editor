package xyz.white.editor.events.listener;

import com.badlogic.gdx.graphics.Color;
import com.google.common.eventbus.Subscribe;

import xyz.white.editor.events.attrs.*;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public interface ChangeActorAttrListener {

    @Subscribe
    void changeColor(Color color);

    @Subscribe
    void changePos(ActorPosChangeEvent actorPosChangeEvent);

    @Subscribe
    void changeSize(ActorSizeEvent actorSizeEvent);

    @Subscribe
    void changeOrigin(ActorOriginEvent actorOriginEvent);

    @Subscribe
    void changeTextAttr(ActorTextEvent actorTextEvent);

    @Subscribe
    void setLabelWrap(LabelWrapEvent labelWrapEvent);

    @Subscribe
    void setAlign(ActorAlignEvent alignEvent);

    @Subscribe
    void changeRotate(ActorRotationEvent event);
}
