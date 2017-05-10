package xyz.white.editor.events.listener;

import com.google.common.eventbus.Subscribe;
import xyz.white.editor.events.shortcut.AlignEvent;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public interface ShorcutEventListener {
    @Subscribe
    void align(AlignEvent alignEvent);
}
