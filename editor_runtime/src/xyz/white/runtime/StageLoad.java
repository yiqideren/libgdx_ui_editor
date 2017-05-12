package xyz.white.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.io.IOException;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class StageLoad {
    public static void loadScene(Group parentGroup,String path){
        if (path==null || parentGroup == null) return;
        FileHandle sceneFile = Gdx.files.internal(path);
        if (sceneFile.exists()){
            try {
                XmlUtils.readFile(parentGroup,sceneFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
