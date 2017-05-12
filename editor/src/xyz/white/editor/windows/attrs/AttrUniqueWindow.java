package xyz.white.editor.windows.attrs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.*;

import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import net.mwplay.nativefont.NativeTextField;

import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.attrs.*;
import xyz.white.editor.factory.AttrFactory;
import xyz.white.editor.utils.XmlUtils;

import java.util.HashMap;

import static xyz.white.editor.Config.alignsIns;
import static xyz.white.editor.Config.alignstrs;

/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class AttrUniqueWindow extends VisWindow {
    private VisTable uniqueTable = null;
    private HashMap<String, Integer> aligns;

    public AttrUniqueWindow() {
        super("Unique Attr", false);
        aligns = new HashMap<>();
        for (int i = 0; i < alignstrs.length; i++) {
            aligns.put(alignstrs[i], alignsIns[i]);
        }

        setSize(Config.width * 0.2f, Config.height * 0.4f);
    }

    //初始化特有属性
    public void refreshAttr(Actor actor) {
        if (uniqueTable == null) {
            uniqueTable = new VisTable(true);
            uniqueTable.left().padLeft(10f).top().padTop(20);
            VisScrollPane scrollPane = new VisScrollPane(uniqueTable);
            scrollPane.setScrollingDisabled(true,false);
            add(scrollPane).expand().fill();
        }
        uniqueTable.clearChildren();
        if (actor instanceof Label) {
            initLabelAttr((Label) actor, uniqueTable);
        } else if (actor instanceof Image) {
            initImageAttr((Image) actor, uniqueTable);
        }  else if (actor instanceof TextField){
            initTextFieldAttr((TextField) actor,uniqueTable);
        }else if (actor instanceof CheckBox){
            initCheckBoxAttr((CheckBox) actor,uniqueTable);
        }else if (actor instanceof Button) {
            initButtonAttr((Button) actor, uniqueTable);
        }

    }

    //    初始化Label属性表
    private void initLabelAttr(final Label label, VisTable uniqueTable) {
        uniqueTable.add(new VisLabel("Text")).left();
        NativeTextField labelTextField = new NativeTextField(
                label.getText().toString(), EditorManager.getInstance().getInputTextStyle());

        final VisCheckBox checkBox_wrap = new VisCheckBox("isWrap");
        labelTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                EditorManager.getInstance().getEventBus().post(new ActorTextEvent(textField.getText()));
                XmlUtils.attr2Label(label, checkBox_wrap.isChecked());
            }
        });
        checkBox_wrap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EditorManager.getInstance().getEventBus().post(new LabelWrapEvent(checkBox_wrap.isChecked()));
                XmlUtils.attr2Label(label, checkBox_wrap.isChecked());
            }
        });

        final VisSelectBox<String> selectBox = new VisSelectBox<String>();
        selectBox.setItems(Config.alignstrs);
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String alignStr = selectBox.getSelected();
                if (Config.aligns.containsKey(alignStr)) {
                    EditorManager.getInstance().getEventBus().post(new ActorAlignEvent(Config.aligns.get(alignStr)));
                }
            }
        });
        uniqueTable.add(labelTextField).width(getWidth() * 0.3f).height(25).left();
        uniqueTable.add(checkBox_wrap);
        uniqueTable.row();
        uniqueTable.add(new VisLabel("Align:"));
        uniqueTable.add(selectBox).width(getWidth() * 0.3f).left();
        uniqueTable.row();
    }


    private void initImageAttr(final Image image, VisTable uniqueTable) {
        String imagePath = "                   ";
        boolean isNine = false;
        int[] nines = {1,1,1,1};
        if (image.getUserObject() != null && image.getUserObject() instanceof HashMap) {
            HashMap hashMap = (HashMap) image.getUserObject();
            if (hashMap.containsKey("image")) {
                imagePath = (String) hashMap.get("image");
            }
            if (hashMap.containsKey("isNine")){
                isNine = (Boolean) hashMap.get("isNine");
                if (image.getDrawable() instanceof NinePatchDrawable){
                    NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) image.getDrawable();
                    nines[0] = (int) ninePatchDrawable.getPatch().getLeftWidth();
                    nines[1] = (int) ninePatchDrawable.getPatch().getRightWidth();
                    nines[2] = (int) ninePatchDrawable.getPatch().getTopHeight();
                    nines[3] = (int) ninePatchDrawable.getPatch().getBottomHeight();
                }
            }
        }
        AttrFactory.createDrawableChooseAttr(uniqueTable, "Path", imagePath, isNine,nines,new AttrFactory.DrawableAttrListener() {
            @Override
            public void selected(String title,String path, boolean isNine,int[] nines ) {
                XmlUtils.attr2Image(image, path,isNine,nines);
                EditorManager.getInstance().getEventBus().post(new ImagePathEvent(path,isNine,nines));
            }

            @Override
            public void cancel() {

            }

            @Override
            public void changeNine(String title, int[] nines) {
                EditorManager.getInstance().getEventBus().post(new NineDrawableEvent(nines));
                XmlUtils.attr2Image(image,null,true,nines);
            }
        });
    }


    private void initButtonAttr(final Button button, VisTable uniqueTable) {
        final String[] strings = {"","",""};
        final boolean[] isnines = {false,false,false};
        int[] nines = {1,1,1,1};
        if (button.getUserObject() != null && button.getUserObject() instanceof HashMap) {
            HashMap hashMap = (HashMap) button.getUserObject();
            strings[0] = (String) hashMap.get("up");
            isnines[0] = (Boolean) hashMap.get("UpIsNine");

            strings[1] = (String) hashMap.get("down");
            isnines[1] = (Boolean) hashMap.get("DownIsNine");

            strings[2] = (String) hashMap.get("check");
            isnines[2] = (Boolean) hashMap.get("CheckIsNine");
        }
        AttrFactory.DrawableAttrListener drawableAttrListener = new AttrFactory.DrawableAttrListener() {
            @Override
            public void selected(String title,String path, boolean isNine,int[] nines) {
                if (title.equals("Up")){
                    strings[0] = path;
                }else if (title.equals("Down")){
                    strings[1] = path;
                }else if (title.equals("Check")){
                    strings[2] = path;
                }
                EditorManager.getInstance().getEventBus().post(new ButtonPathEvent(
                            strings));
                XmlUtils.attr2Button(button, strings);
            }

            @Override
            public void cancel() {

            }

            @Override
            public void changeNine(String title, int[] nines) {

            }


        };
        AttrFactory.createDrawableChooseAttr(uniqueTable, "Up", strings[0],isnines[0], nines,drawableAttrListener);
        AttrFactory.createDrawableChooseAttr(uniqueTable, "Down", strings[1], isnines[1],nines,drawableAttrListener);
        AttrFactory.createDrawableChooseAttr(uniqueTable, "Check", strings[2], isnines[2],nines,drawableAttrListener);
//        final NativeTextField upTextField = new NativeTextField(
//                imageUp, EditorManager.getInstance().getInputTextStyle());
//        final NativeTextField downTextField = new NativeTextField(
//                imageDown, EditorManager.getInstance().getInputTextStyle()
//        );
//        final NativeTextField checkTextField = new NativeTextField(
//                imageCheck, EditorManager.getInstance().getInputTextStyle()
//        );
//        upTextField.setTextFieldListener(new TextField.TextFieldListener() {
//            @Override
//            public void keyTyped(TextField textField, char c) {
//            }
//        });
//        final VisTextButton chooseUpButton = new VisTextButton("  ....  ");
//        final VisTextButton chooseDownButton = new VisTextButton("  ....  ");
//        final VisTextButton chooseCheckButton = new VisTextButton("  ....  ");
//        final FileChooser chooser = new FileChooser(FileChooser.Mode.OPEN);
//        chooser.setSelectionMode(FileChooser.SelectionMode.FILES);
//        chooser.setListener(new FileChooserListener() {
//            @Override
//            public void selected(Array<FileHandle> files) {
//                if (files.size > 0) {
//                    int type = (int) chooser.getUserObject();
//                    String path = files.get(0).path().replace(Config.getProjectPath(), "");
//                    if (type == 1) {
//                        upTextField.setText(path);
//                    } else if (type == 2) {
//                        downTextField.setText(path);
//                    } else if (type == 3) {
//                        checkTextField.setText(path);
//                    }
//                    EditorManager.getInstance().getEventBus().post(new ButtonPathEvent(
//                            upTextField.getText(), downTextField.getText(), checkTextField.getText()));
//                    XmlUtils.attr2Button(button, upTextField.getText(), downTextField.getText(), checkTextField.getText());
//                }
//            }
//
//            @Override
//            public void canceled() {
//
//            }
//        });
//        chooseUpButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                chooser.setUserObject(1);
//                getStage().addActor(chooser.fadeIn());
//            }
//        });
//
//        chooseDownButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                chooser.setUserObject(2);
//                getStage().addActor(chooser.fadeIn());
//            }
//        });
//        chooseCheckButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                chooser.setUserObject(3);
//                getStage().addActor(chooser.fadeIn());
//            }
//        });
//
//        uniqueTable.add(new VisLabel("Up")).left();
//        uniqueTable.row();
//        uniqueTable.add(upTextField).width(getWidth() * 0.7f).height(25).left();
//        uniqueTable.add(chooseUpButton);
//        uniqueTable.row();
//        uniqueTable.add(new VisLabel("Down")).left();
//        uniqueTable.row();
//        uniqueTable.add(downTextField).width(getWidth() * 0.7f).height(25).left();
//        uniqueTable.add(chooseDownButton);
//        uniqueTable.row();
//        uniqueTable.add(new VisLabel("Check")).left();
//        uniqueTable.row();
//        uniqueTable.add(checkTextField).width(getWidth() * 0.7f).height(25).left();
//        uniqueTable.add(chooseCheckButton);
//        uniqueTable.row();
    }

    public void initTextFieldAttr(final TextField textField,VisTable uniqueTable){

        final NativeTextField contextTextField = new NativeTextField(
                textField.getText()==null?"":textField.getText(), EditorManager.getInstance().getInputTextStyle());
        final NativeTextField messageTextField = new NativeTextField(
                textField.getMessageText() == null?"":textField.getMessageText(), EditorManager.getInstance().getInputTextStyle());
        contextTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField listenerText, char c) {
                EditorManager.getInstance().getEventBus().post(new TextFieldTextEvent(listenerText.getText(),messageTextField.getText()));
                XmlUtils.attr2TextField(textField);
            }
        });


        messageTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField listenerText, char c) {
                EditorManager.getInstance().getEventBus().post(new TextFieldTextEvent(contextTextField.getText(),messageTextField.getText()));
                XmlUtils.attr2TextField(textField);
            }
        });

        uniqueTable.add(new VisLabel("Text")).left();
        uniqueTable.add(contextTextField).width(getWidth() * 0.5f).left();
        uniqueTable.row();
        uniqueTable.add(new VisLabel("Message")).left();
        uniqueTable.add(messageTextField).width(getWidth() * 0.5f).left();
    }

    public void initCheckBoxAttr(final CheckBox checkBox,VisTable uniqueTable){

    }

}
