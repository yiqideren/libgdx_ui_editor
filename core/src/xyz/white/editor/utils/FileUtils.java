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
            ParseGenAttr(parentGroup,element);
            if (element.getChildCount()>0){
                for (int i = 0;i<element.getChildCount();i++){
                    ReadAttr(parentGroup,element.getChild(i),dragListener);
                }
            }
        }else if (element.getName().equals("Group")){
            Group group = new Group();
            ParseGenAttr(group,element);
            group.addListener(dragListener);
            parentGroup.addActor(group);
           if (element.getChildCount()>0){
               for (int i = 0;i<element.getChildCount();i++){
                   ReadAttr(group,element.getChild(i),dragListener);
               }
           }
        }else if (element.getName().equals("Label")){
            NativeLabel label = new NativeLabel(element.get("text",""), EditorManager.getInstance().getMainFont());
            parentGroup.addActor(label);
            label.addListener(dragListener);
            ParseGenAttr(label,element);
        }else if (element.getName().equals("Button")){
            VisImageButton button = new VisImageButton((VisUI.getSkin().get(VisImageButton.VisImageButtonStyle.class)));
            parentGroup.addActor(button);
            button.addListener(dragListener);
            ParseGenAttr(button,element);
        }
    }

    public static void ParseGenAttr(Actor actor, XmlReader.Element element){
        actor.setWidth(element.getFloat("width",100));
        actor.setHeight(element.getFloat("height",100));
        actor.setPosition(element.getFloat("x",0),element.getFloat("y",0));
        actor.setOrigin(element.getFloat("originX",0),element.getFloat("originY",0));
        if (element.getAttributes().containsKey("name")){
            actor.setName(element.get("name"));
        }
    }

    public static void WriteFile(Group group,FileHandle fileHandle) throws IOException {
        FileWriter writer = new FileWriter(fileHandle.file());
        XmlWriter xmlWriter= new XmlWriter(writer);
        writeAttr(xmlWriter,group);
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
        }else if (cla.equals(Label.class)){
            Label label = (Label) actor;
            xmlWriter.element("Label");
            writeGenAttr(xmlWriter,label);
            xmlWriter.attribute("text",label.getText());
            xmlWriter.pop();
        }else if (cla.equals(Button.class)){
            Button button = (Button) actor;
            xmlWriter.element("Button");
            writeGenAttr(xmlWriter,button);
            xmlWriter.pop();
        }
    }

    public static void writeGenAttr(XmlWriter writer,Actor actor) throws IOException {
        writer.attribute("width",actor.getWidth())
                .attribute("height",actor.getHeight())
                .attribute("x",actor.getX())
                .attribute("y",actor.getY())
                .attribute("originX",actor.getOriginX())
                .attribute("originY",actor.getOriginY());
        if (actor.getName()!=null&&!actor.getName().isEmpty()){
            writer.attribute("name",actor.getName());
        }
    }
}
