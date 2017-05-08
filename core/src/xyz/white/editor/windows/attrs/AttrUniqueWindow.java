package xyz.white.editor.windows.attrs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.*;

import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import net.mwplay.nativefont.NativeTextField;

import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.attrs.*;
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
            add(uniqueTable).expand().fill();
        }
        uniqueTable.clearChildren();
        if (actor instanceof Label) {
            initLabelAttr((Label) actor, uniqueTable);
        } else if (actor instanceof Image) {
            initImageAttr((Image) actor, uniqueTable);
        } else if (actor instanceof Button) {
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
                EditorManager.getInstance().getEventBus().post(new ActorTextEvent(textField.getText(), c));
                XmlUtils.Attr2Label(label, checkBox_wrap.isChecked());
            }
        });
        checkBox_wrap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EditorManager.getInstance().getEventBus().post(new LabelWrapEvent(checkBox_wrap.isChecked()));
                XmlUtils.Attr2Label(label, checkBox_wrap.isChecked());
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
        uniqueTable.add(labelTextField).width(getWidth() * 0.3f).left();
        uniqueTable.add(checkBox_wrap);
        uniqueTable.row();
        uniqueTable.add(new VisLabel("Align:"));
        uniqueTable.add(selectBox).width(getWidth() * 0.3f).left();
        uniqueTable.row();
    }


    private void initImageAttr(final Image image, VisTable uniqueTable) {
        String imagePath = "                   ";
        if (image.getUserObject() != null && image.getUserObject() instanceof HashMap) {
            HashMap hashMap = (HashMap) image.getUserObject();
            if (hashMap.containsKey("image")) {
                imagePath = (String) hashMap.get("image");
            }
        }
        uniqueTable.add(new VisLabel("Path")).left();
        uniqueTable.row();
        final NativeTextField pathTextField = new NativeTextField(
                imagePath, EditorManager.getInstance().getInputTextStyle());
        pathTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        final VisTextButton chooseImageButton = new VisTextButton("  ....  ");
        final FileChooser chooser = new FileChooser(FileChooser.Mode.OPEN);
        chooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        chooser.setListener(new FileChooserListener() {
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size > 0) {
                    String path = files.get(0).path().replace(Config.getProjectPath(), "");
                    pathTextField.setText(path);
                    EditorManager.getInstance().getEventBus().post(new ImagePathEvent(path));
                    XmlUtils.Attr2Image(image, path);
                }
            }

            @Override
            public void canceled() {

            }
        });
        chooseImageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(chooser.fadeIn());
            }
        });

        uniqueTable.add(pathTextField).width(getWidth() * 0.7f).left();
        uniqueTable.add(chooseImageButton);
        uniqueTable.row();
    }


    public void initButtonAttr(final Button button, VisTable uniqueTable) {
        final int checkType = 0;
        String imageUp = null;
        String imageDown = null;
        String imageCheck = null;
        if (button.getUserObject() != null && button.getUserObject() instanceof HashMap) {
            HashMap hashMap = (HashMap) button.getUserObject();
            imageUp = (String) hashMap.get("up");
            imageDown = (String) hashMap.get("down");
            imageCheck = (String) hashMap.get("check");
        }

        final NativeTextField upTextField = new NativeTextField(
                imageUp, EditorManager.getInstance().getInputTextStyle());
        final NativeTextField downTextField = new NativeTextField(
                imageDown, EditorManager.getInstance().getInputTextStyle()
        );
        final NativeTextField checkTextField = new NativeTextField(
                imageCheck, EditorManager.getInstance().getInputTextStyle()
        );
        upTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        final VisTextButton chooseUpButton = new VisTextButton("  ....  ");
        final VisTextButton chooseDownButton = new VisTextButton("  ....  ");
        final VisTextButton chooseCheckButton = new VisTextButton("  ....  ");
        final FileChooser chooser = new FileChooser(FileChooser.Mode.OPEN);
        chooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        chooser.setListener(new FileChooserListener() {
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size > 0) {
                    int type = (int) chooser.getUserObject();
                    String path = files.get(0).path().replace(Config.getProjectPath(), "");
                    if (type == 1) {
                        upTextField.setText(path);
                    } else if (type == 2) {
                        downTextField.setText(path);
                    } else if (type == 3) {
                        checkTextField.setText(path);
                    }
                    EditorManager.getInstance().getEventBus().post(new ButtonPathEvent(
                            upTextField.getText(), downTextField.getText(), checkTextField.getText()));
                    XmlUtils.Attr2Button(button, upTextField.getText(), downTextField.getText(), checkTextField.getText());
                }
            }

            @Override
            public void canceled() {

            }
        });
        chooseUpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setUserObject(1);
                getStage().addActor(chooser.fadeIn());
            }
        });

        chooseDownButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setUserObject(2);
                getStage().addActor(chooser.fadeIn());
            }
        });
        chooseCheckButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setUserObject(3);
                getStage().addActor(chooser.fadeIn());
            }
        });

        uniqueTable.add(new VisLabel("Up")).left();
        uniqueTable.row();
        uniqueTable.add(upTextField).width(getWidth() * 0.7f).left();
        uniqueTable.add(chooseUpButton);
        uniqueTable.row();
        uniqueTable.add(new VisLabel("Down")).left();
        uniqueTable.row();
        uniqueTable.add(downTextField).width(getWidth() * 0.7f).left();
        uniqueTable.add(chooseDownButton);
        uniqueTable.row();
        uniqueTable.add(new VisLabel("Check")).left();
        uniqueTable.row();
        uniqueTable.add(checkTextField).width(getWidth() * 0.7f).left();
        uniqueTable.add(chooseCheckButton);
        uniqueTable.row();

    }

}
