package xyz.white.editor.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;

/**
 * Created by 10037 on 2017/4/28 0028.
 */
public class XmlUtils {
    public static void WriteFile(FileHandle fileHandle){

    }

    public static void ReadFile(Group group,FileHandle fileHandle) throws IOException {
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(fileHandle);

    }
}
