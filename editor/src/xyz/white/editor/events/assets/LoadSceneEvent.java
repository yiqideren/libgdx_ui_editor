package xyz.white.editor.events.assets;

import com.badlogic.gdx.files.FileHandle;
import xyz.white.editor.events.Event;

/**
 * Created by 10037 on 2017/5/1 0001.
 */
public class LoadSceneEvent implements Event {
    public FileHandle sceneFile;

    public LoadSceneEvent(FileHandle sceneFile){
        this.sceneFile = sceneFile;
    }

    @Override
    public Event redo() {
        return null;
    }

    @Override
    public Event undo() {
        return null;
    }
}
