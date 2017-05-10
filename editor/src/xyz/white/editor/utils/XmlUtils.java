package xyz.white.editor.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader;
import com.kotcrab.vis.ui.widget.VisImageButton;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;

import java.util.HashMap;


/**
 * Created by 10037 on 2017/4/28 0028.
 */
public class XmlUtils {
    public static void ParseGenAttr(Actor actor, XmlReader.Element element){
        actor.setWidth(element.getFloat("width",100));
        actor.setHeight(element.getFloat("height",100));
        actor.setPosition(element.getFloat("x",0),element.getFloat("y",0));
        actor.setOrigin(element.getFloat("originX",0),element.getFloat("originY",0));
        if (element.getAttributes().containsKey("name")){
            actor.setName(element.get("name"));
        }
    }

    public static void ParseUqAttr(Actor actor, XmlReader.Element element){
        Class cls  = EditorManager.getInstance().getActorType(actor);
        if (cls.equals(Label.class)){
            ParseLabel((Label) actor,element);
        }else if (cls.equals(Image.class)){
            ParseImage((Image) actor,element);
        }else if (cls.equals(Button.class)){
            ParseButton((VisImageButton) actor,element);
        }else if (cls.equals(TextField.class)){
            ParseTextField((TextField) actor,element);
        }
    }

    public static void ParseLabel(Label label, XmlReader.Element element){
        boolean isWrap = element.getBoolean("isWrap",false);
        label.setWrap(isWrap);
        label.setText(element.get("text",""));
        Attr2Label(label,isWrap);
    }

    public static void ParseImage(Image image, XmlReader.Element element){
        String imagePath = element.get("image","");
        image.setDrawable(new TextureRegionDrawable(new TextureRegion(
                new Texture(Config.getImageFilePath(imagePath))
        )));

        Attr2Image(image,imagePath);
    }

    public static void ParseButton(VisImageButton button, XmlReader.Element element){
        String upPath = element.get("up");
        String downPath = element.get("down");
        String checkPath = element.get("check");
        Drawable up = new TextureRegionDrawable(new TextureRegion(new Texture(Config.getImageFilePath(upPath))));
        Drawable down = new TextureRegionDrawable(new TextureRegion(new Texture(Config.getImageFilePath(downPath))));
        Drawable checked = new TextureRegionDrawable(new TextureRegion(new Texture(Config.getImageFilePath(checkPath))));
        VisImageButton.VisImageButtonStyle buttonStyle = new VisImageButton.VisImageButtonStyle(
                up,down,checked,up,down,checked
        );
        button.setStyle(buttonStyle);
        Attr2Button(button,upPath,downPath,checkPath);
    }

    public static void ParseTextField(TextField textField, XmlReader.Element element){
        String text = element.get("text","");
        String messageText = element.get("messageText","");
        textField.setText(text);
        textField.setMessageText(messageText);
        Attr2TextField(textField);
    }


    public static void Attr2Image(Image image, String imagePath){
        HashMap hashMap = new HashMap();
        hashMap.put("image",imagePath);
        image.setUserObject(hashMap);
    }

    public static void Attr2Label(Label label,boolean isWrap){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("text",label.getText().toString());
        hashMap.put("isWrap",String.valueOf(isWrap));
        label.setUserObject(hashMap);
    }

    public static void Attr2Button(Button button,String up,String down,String check){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("up",up);
        hashMap.put("down",down);
        hashMap.put("check",check);
        button.setUserObject(hashMap);
    }

    public static void Attr2TextField(TextField textField){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("text",textField.getText());
        hashMap.put("messageText",textField.getMessageText());
        textField.setUserObject(hashMap);
    }

}
