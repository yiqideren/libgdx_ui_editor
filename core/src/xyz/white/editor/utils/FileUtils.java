package xyz.white.editor.utils;

import com.badlogic.gdx.utils.XmlWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by 10037 on 2017/5/1 0001.
 */
public class FileUtils {

    public static void createScene(Writer writer,float width,float height,String name) throws IOException {
        XmlWriter xmlWriter= new XmlWriter(writer);
        xmlWriter.element("stage")
                    .attribute("width",width)
                    .attribute("height",height)
                    .attribute("name",name)
                    .pop();
        xmlWriter.flush();
    }
}
