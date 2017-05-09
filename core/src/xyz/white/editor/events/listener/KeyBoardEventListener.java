package xyz.white.editor.events.listener;

import com.google.common.eventbus.Subscribe;
import xyz.white.editor.events.keyboard.KeyDelEvent;

/**
 * 键盘监听
 * Created by 10037 on 2017/4/23 0023.
 */
public interface KeyBoardEventListener {
    @Subscribe
    void key_del(KeyDelEvent event);

}
