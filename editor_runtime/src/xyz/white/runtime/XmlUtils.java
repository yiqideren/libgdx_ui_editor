package xyz.white.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader;
import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;
import net.mwplay.nativefont.NativeLabel;
import net.mwplay.nativefont.NativeTextField;

import java.io.IOException;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class XmlUtils {

    public static void readFile(Group group, FileHandle fileHandle) throws IOException {
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(fileHandle);
        readAttr(group,element);
    }

    private static void readAttr(Group parentGroup, XmlReader.Element element){
        if (element.getName().equals("Stage")){
            XmlUtils.parseGenAttr(parentGroup,element);
            if (element.getChildCount()>0){
                for (int i = 0;i<element.getChildCount();i++){
                    readAttr(parentGroup,element.getChild(i));
                }
            }
        }else if (element.getName().equals("Group")){
            Group group = new Group();
            XmlUtils.parseGenAttr(group,element);
            parentGroup.addActor(group);
            if (element.getChildCount()>0){
                for (int i = 0;i<element.getChildCount();i++){
                    readAttr(group,element.getChild(i));
                }
            }
        }else {
            Actor actor = getActorByName(element.getName());
            parentGroup.addActor(actor);
            XmlUtils.parseGenAttr(actor,element);
            XmlUtils.parseUqAttr(actor,element);
        }

    }

    private static Actor getActorByName(String name){
        switch (name){
            case "Group":
                return new Group();
            case "Label":
                return new NativeLabel("",new NativeFont(new NativeFontPaint(14)));
            case "CheckBox":
//                return new CheckBox("");
            case "Image":
                return new Image();
            case "Button":
                return new Button();
            case "TextField":
            default:
                return new Actor();
        }
    }

    private static void parseGenAttr(Actor actor, XmlReader.Element element){
        actor.setWidth(element.getFloat("width",100));
        actor.setHeight(element.getFloat("height",100));
        actor.setPosition(element.getFloat("x",0),element.getFloat("y",0));
        actor.setOrigin(element.getFloat("originX",0),element.getFloat("originY",0));
        if (element.getAttributes().containsKey("name")){
            actor.setName(element.get("name"));
        }
    }
    private static Class getActorType(Actor actor){
        if (actor instanceof Label){
            return Label.class;
        }else if (actor instanceof CheckBox){
            return CheckBox.class;
        }else if (actor instanceof Image){
            return Image.class;
        }else if (actor instanceof TextField){
            return TextField.class;
        }else if (actor instanceof Button){
            return Button.class;
        }else if (actor instanceof Group){
            return Group.class;
        }else {
            return Actor.class;
        }
    }

    private static void parseUqAttr(Actor actor, XmlReader.Element element){
        Class cls  = getActorType(actor);
        if (cls.equals(Label.class)){
            parseLabel((Label) actor,element);
        }else if (cls.equals(Image.class)){
            parseImage((Image) actor,element);
        }else if (cls.equals(Button.class)){
            parseButton((Button) actor,element);
        }else if (cls.equals(TextField.class)){
            parseTextField((TextField) actor,element);
        }
    }

    private static void parseLabel(Label label, XmlReader.Element element){
        boolean isWrap = element.getBoolean("isWrap",false);
        label.setWrap(isWrap);
        label.setText(element.get("text",""));
    }

    private static void parseImage(Image image, XmlReader.Element element){
        String imagePath = element.get("image","");
        boolean isNine   = element.getBoolean("isNine",false);
        int[] nine = {1,1,1,1};
        Texture texture = new Texture(Gdx.files.internal(imagePath));
        Drawable drawable;
        if (isNine){
            nine[0] = element.getInt("left",1);
            nine[1] = element.getInt("right",1);
            nine[2] = element.getInt("top",1);
            nine[3] = element.getInt("bottom",1);
            drawable = new NinePatchDrawable(new NinePatch(texture,nine[0],nine[1],nine[2],nine[3]));
        }else {
            drawable = new TextureRegionDrawable(new TextureRegion(texture));
        }

        image.setDrawable(drawable);
    }

    private static void parseButton(Button button, XmlReader.Element element){
        String upPath = element.get("up");
        String downPath = element.get("down");
        String checkPath = element.get("check");
        Drawable up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(upPath))));
        Drawable down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(downPath))));
        Drawable checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(checkPath))));
        Button.ButtonStyle  buttonStyle = new Button.ButtonStyle(up,down,checked);
        button.setStyle(buttonStyle);
    }

    private static void parseTextField(TextField textField, XmlReader.Element element){
        String text = element.get("text","");
        String messageText = element.get("messageText","");
        textField.setText(text);
        textField.setMessageText(messageText);
    }


}
