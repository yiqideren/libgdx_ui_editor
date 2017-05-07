package xyz.white.editor;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.google.common.eventbus.EventBus;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import com.kotcrab.vis.ui.widget.file.FileChooser;
import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class EditorManager {
    private TextField.TextFieldStyle inputTextFieldStyle;
    private NativeFont mainFont,inputFont;
    private EventBus eventBus;
    private static EditorManager editorManager ;
    public AssetManager assetManager;

    public EditorManager(){
        FileChooser.setDefaultPrefsName("white-ui-editor");
        FileChooser.setSaveLastDirectory(true);

        assetManager = new AssetManager();
        assetManager.load("badlogic.jpg", Texture.class);
        assetManager.finishLoading();
    }

    public static EditorManager getInstance(){
        if (editorManager==null) editorManager = new EditorManager();

        return editorManager;
    }

    public EventBus getEventBus(){
        if (eventBus==null) {
            eventBus = new EventBus();
        }
        return eventBus;
    }

    public NativeFont getMainFont(){
        if (mainFont == null) {
             mainFont = new NativeFont();
            mainFont.setSize(18);
        }
        return mainFont;
    }

    public NativeFont getTextInputFont(){
        if (inputFont == null) {
            inputFont = new NativeFont();
            inputFont.setSize(16);
        }
        return inputFont;
    }

    public TextField.TextFieldStyle getInputTextStyle(){
        if (inputTextFieldStyle == null){
            VisTextField.VisTextFieldStyle visTextFieldStyle = VisUI.getSkin().get(VisTextField.VisTextFieldStyle.class);

            inputTextFieldStyle = new TextField.TextFieldStyle(getTextInputFont(),
                    Color.WHITE,
                    visTextFieldStyle.cursor,
                    visTextFieldStyle.selection,
                    visTextFieldStyle.background
            );
        }
        return inputTextFieldStyle;
    }

    public Class getActorType(Actor actor){
        if (actor instanceof Label){
            return Label.class;
        }else if (actor instanceof VisCheckBox){
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

    public Actor getActorByName(String name){
        switch (name){
            case "Group":
                return new Group();
            case "Label":
                return new NativeLabel("",getMainFont());
            case "CheckBox":
                return new VisCheckBox("");
            case "Image":
                return new Image(assetManager.get("badlogic.jpg",Texture.class));
            case "Button":
                return new VisImageButton(VisUI.getSkin().get(VisImageButton.VisImageButtonStyle.class));
            case "TextField":
                return new VisTextField("");
            default:
                return new Actor();
        }
    }
}
