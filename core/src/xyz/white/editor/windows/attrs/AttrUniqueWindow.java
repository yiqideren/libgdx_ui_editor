package xyz.white.editor.windows.attrs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;

import net.mwplay.nativefont.NativeTextField;

import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.attrs.ActorAlignEvent;
import xyz.white.editor.events.attrs.ActorTextEvent;
import xyz.white.editor.events.attrs.LabelWrapEvent;

import java.util.HashMap;

import static xyz.white.editor.Config.alignsIns;
import static xyz.white.editor.Config.alignstrs;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class AttrUniqueWindow extends VisWindow {
    private VisTable uniqueTable = null;
    private HashMap<String,Integer> aligns;
    public AttrUniqueWindow() {
        super("Unique Attr",false);
        aligns = new HashMap<>();
        for (int i = 0; i < alignstrs.length; i++) {
            aligns.put(alignstrs[i],alignsIns[i]);
        }

        setSize(Config.width*0.2f,Config.height*0.4f);
    }

    //初始化特有属性
    public void refreshAttr(Actor actor)
    {   if (uniqueTable == null) {
            uniqueTable = new VisTable(true);
            uniqueTable.left().padLeft(10f).top().padTop(20);
            add(uniqueTable).expand().fill();
        }
        uniqueTable.clearChildren();
        if (actor instanceof Label){
            initLabelAttr((Label) actor,uniqueTable);
        }else if (actor instanceof Image){


        }

    }

//    初始化Label属性表
    private void initLabelAttr(Label label,VisTable uniqueTable){
        uniqueTable.add(new VisLabel("Text")).left();
        NativeTextField labelTextField = new NativeTextField(
                label.getText().toString(), EditorManager.getInstance().getInputTextStyle());
        labelTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                EditorManager.getInstance().getEventBus().post(new ActorTextEvent(textField.getText(),c));
            }
        });
        final VisCheckBox checkBox_wrap = new VisCheckBox("isWrap");
        checkBox_wrap.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                EditorManager.getInstance().getEventBus().post(new LabelWrapEvent(checkBox_wrap.isChecked()));
            }
        });

        final VisSelectBox<String> selectBox = new VisSelectBox<String>();
        selectBox.setItems(alignstrs);
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String alignStr = selectBox.getSelected();
                if (aligns.containsKey(alignStr)) {
                    EditorManager.getInstance().getEventBus().post(new ActorAlignEvent(aligns.get(alignStr)));
                }
            }
        });
        uniqueTable.add(labelTextField).width(getWidth()*0.3f).left();
        uniqueTable.add(checkBox_wrap);
        uniqueTable.row();
        uniqueTable.add(new VisLabel("Align:"));
        uniqueTable.add(selectBox).width(getWidth()*0.3f).left();
        uniqueTable.row();
    }


}
