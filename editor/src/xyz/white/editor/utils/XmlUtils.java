package xyz.white.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
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
    public static void parseGenAttr(Actor actor, XmlReader.Element element){
        actor.setWidth(element.getFloat("width",100));
        actor.setHeight(element.getFloat("height",100));
        actor.setPosition(element.getFloat("x",0),element.getFloat("y",0));
        actor.setOrigin(element.getFloat("originX",0),element.getFloat("originY",0));
        if (element.getAttributes().containsKey("name")){
            actor.setName(element.get("name"));
        }
    }

    public static void parseUqAttr(Actor actor, XmlReader.Element element){
        Class cls  = EditorManager.getInstance().getActorType(actor);
        if (cls.equals(Label.class)){
            parseLabel((Label) actor,element);
        }else if (cls.equals(Image.class)){
            parseImage((Image) actor,element);
        }else if (cls.equals(Button.class)){
            parseButton((VisImageButton) actor,element);
        }else if (cls.equals(TextField.class)){
            parseTextField((TextField) actor,element);
        }
    }

    public static void parseLabel(Label label, XmlReader.Element element){
        boolean isWrap = element.getBoolean("isWrap",false);
        label.setWrap(isWrap);
        label.setText(element.get("text",""));
        attr2Label(label,isWrap);
    }

    public static void parseImage(Image image, XmlReader.Element element){
        String imagePath = element.get("image","");
        boolean isNine   = element.getBoolean("isNine",false);
        int[] nine = {1,1,1,1};
        Texture texture = new Texture(Config.getImageFilePath(imagePath));
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

        attr2Image(image,imagePath,isNine,nine);
    }

    public static void parseButton(VisImageButton button, XmlReader.Element element){
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
        attr2Button(button, new String[]{upPath, downPath, checkPath});
    }

    public static void parseTextField(TextField textField, XmlReader.Element element){
        String text = element.get("text","");
        String messageText = element.get("messageText","");
        textField.setText(text);
        textField.setMessageText(messageText);
        attr2TextField(textField);
    }


    public static void attr2Image(Image image, String imagePath,boolean isNine,int[] nines){
        HashMap hashMap = getActorAttr(image);
        if (imagePath!=null)hashMap.put("image",imagePath);
        hashMap.put("isNine",isNine);
        if (isNine ){
            hashMap.put("left",nines[0]);
            hashMap.put("right",nines[1]);
            hashMap.put("top",nines[2]);
            hashMap.put("bottom",nines[3]);
        }
        image.setUserObject(hashMap);
    }

    public static void attr2Label(Label label, boolean isWrap){
        HashMap<String,String> hashMap = getActorAttr(label);
        hashMap.put("text",label.getText().toString());
        hashMap.put("isWrap",String.valueOf(isWrap));
        label.setUserObject(hashMap);
    }

    public static void attr2Button(Button button, String[] strings){
        HashMap<String,String> hashMap = getActorAttr(button);
        hashMap.put("up",strings[0]);
        hashMap.put("down",strings[1]);
        hashMap.put("check",strings[2]);
        button.setUserObject(hashMap);
    }

    public static void attr2TextField(TextField textField){
        HashMap<String,String> hashMap = getActorAttr(textField);
        hashMap.put("text",textField.getText());
        hashMap.put("messageText",textField.getMessageText());
        textField.setUserObject(hashMap);
    }

    private static HashMap getActorAttr(Actor actor){
        if (actor.getUserObject() !=null && actor.getUserObject() instanceof HashMap){
            return (HashMap) actor.getUserObject();
        }else {
           return new HashMap<>();
        }
    }
}
