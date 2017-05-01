package xyz.white.editor.events.listener;

import com.google.common.eventbus.Subscribe;
import xyz.white.editor.events.assets.LoadSceneEvent;

/**
 * Created by 10037 on 2017/5/1 0001.
 */
public interface AssetEventListener {

    @Subscribe
    void loadScene(LoadSceneEvent event);
}
