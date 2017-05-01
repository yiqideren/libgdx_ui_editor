package xyz.white.editor.events.listener;

import com.google.common.eventbus.Subscribe;
import xyz.white.editor.events.application.LoadProjectEvent;

/**
 * Created by 10037 on 2017/4/23 0023.
 */
public interface ApplicationEventListener {

    @Subscribe
    void LoadProject(LoadProjectEvent event);
}
