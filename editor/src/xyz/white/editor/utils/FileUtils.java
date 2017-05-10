package xyz.white.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import net.mwplay.nativefont.NativeLabel;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.SelectGroup;
import xyz.white.editor.windows.MainWindow;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 * Created by 10037 on 2017/5/1 0001.
 */
public class FileUtils {

    public static void createScene(Writer writer,float width,float height,String name) throws IOException {
        XmlWriter xmlWriter= new XmlWriter(writer);
        xmlWriter.element("Stage")
                    .attribute("width",width)
                    .attribute("height",height)
                    .attribute("name",name)
                    .pop();
        xmlWriter.flush();
    }

    public static void ReadFile(Group group, FileHandle fileHandle, EventListener dragListener) throws IOException {
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(fileHandle);
        ReadAttr(group,element,dragListener);
    }

    public static void ReadAttr(Group parentGroup, XmlReader.Element element,EventListener dragListener){
        if (element.getName().equals("Stage")){
            XmlUtils.ParseGenAttr(parentGroup,element);
            if (element.getChildCount()>0){
                for (int i = 0;i<element.getChildCount();i++){
                    ReadAttr(parentGroup,element.getChild(i),dragListener);
                }
            }
        }else if (element.getName().equals("Group")){
            Group group = new Group();
            XmlUtils.ParseGenAttr(group,element);
            group.addListener(dragListener);
            parentGroup.addActor(group);
           if (element.getChildCount()>0){
               for (int i = 0;i<element.getChildCount();i++){
                   ReadAttr(group,element.getChild(i),dragListener);
               }
           }
        }else {
            Actor actor = EditorManager.getInstance().getActorByName(element.getName());
            parentGroup.addActor(actor);
            actor.addListener(dragListener);
            XmlUtils.ParseGenAttr(actor,element);
            XmlUtils.ParseUqAttr(actor,element);
        }

    }


    public static void WriteFile(Group group,FileHandle fileHandle) throws IOException {
        FileWriter writer = new FileWriter(fileHandle.file());
        XmlWriter xmlWriter= new XmlWriter(writer);
        writeAttr(xmlWriter,group);
        xmlWriter.flush();
        writer.close();
    }

    public static void writeAttr(XmlWriter xmlWriter, Actor actor) throws IOException {
        Class cla = EditorManager.getInstance().getActorType(actor);
        if (actor instanceof MainWindow){
            xmlWriter.element("Stage");
            writeGenAttr(xmlWriter,actor);
            for (Actor child:((MainWindow) actor).getChildren()){
                if (child instanceof SelectGroup) continue;
                writeAttr(xmlWriter,child);
            }
            xmlWriter.pop();
        }else if (cla.equals(Group.class)){
            xmlWriter.element("Group");
            writeGenAttr(xmlWriter,actor);
            for (Actor child:((Group) actor).getChildren()){
                writeAttr(xmlWriter,child);
            }
            xmlWriter.pop();
        }else {
            xmlWriter.element(cla.getSimpleName());
            writeGenAttr(xmlWriter,actor);
            writeUqAttr(xmlWriter,actor);
            xmlWriter.pop();
        }
    }

    public static void writeGenAttr(XmlWriter writer,Actor actor) throws IOException {
        writer.attribute("width",actor.getWidth())
                .attribute("height",actor.getHeight())
                .attribute("x",actor.getX())
                .attribute("y",actor.getY())
                .attribute("originX",actor.getOriginX())
                .attribute("originY",actor.getOriginY())
                .attribute("visible",String.valueOf(actor.isVisible()));
        if (actor.getName()!=null&&!actor.getName().isEmpty()){
            writer.attribute("name",actor.getName());
        }
    }

    public static void writeUqAttr(XmlWriter xmlWriter,Actor actor) throws IOException {
        Object userobject = actor.getUserObject();
        if (userobject!=null&&userobject instanceof HashMap){
            HashMap<String,String> attrMap = (HashMap<String,String>)userobject;
            for (String key : attrMap.keySet()){
                xmlWriter.attribute(key,attrMap.get(key));
            }
        }
    }
}
