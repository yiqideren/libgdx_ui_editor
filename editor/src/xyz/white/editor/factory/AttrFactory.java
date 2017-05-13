package xyz.white.editor.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import net.mwplay.nativefont.NativeTextField;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.DigitsFieldFilter;
import xyz.white.editor.actors.ninepatch.MainPanel;

/**
 * Created by 10037 on 2017/5/12 0012.
 */
public class AttrFactory {

    public interface DrawableAttrListener {
        void selected(String title, String path, boolean isNine,final int[] nines);

        void cancel();

        void changeNine(String title, int[] nines);
    }

    public static void createDrawableChooseAttr(final Table contentTable, final String title, final String imagePath, boolean isNine, final int[] nines, final DrawableAttrListener drawableAttrListener) {
        contentTable.add(new VisLabel(title)).left();
        contentTable.row();
        final NativeTextField pathTextField = new NativeTextField(
                imagePath, EditorManager.getInstance().getInputTextStyle());
        pathTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        final VisTextButton chooseImageButton = new VisTextButton("  ....  ");
        final FileChooser chooser = new FileChooser(FileChooser.Mode.OPEN);
        final VisCheckBox nineCheck = new VisCheckBox("ninePatch");
        nineCheck.setChecked(isNine);
        final VisTable collapsTable = new VisTable();
        final CollapsibleWidget collapsibleWidget = new CollapsibleWidget(collapsTable);
        collapsibleWidget.setCollapsed(!nineCheck.isChecked(),false);
        final VisTextField leftTF = new VisTextField(String.valueOf(nines[0]));
        final VisTextField rightTF = new VisTextField(String.valueOf(nines[1]));
        final VisTextField topTF = new VisTextField(String.valueOf(nines[2]));
        final VisTextField bottomTF = new VisTextField(String.valueOf(nines[3]));
        chooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        chooser.setListener(new FileChooserListener() {
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size > 0) {
                    nines[0] = Integer.valueOf(leftTF.getText());
                    nines[1]  = Integer.valueOf(rightTF.getText());
                    nines[2]  = Integer.valueOf(topTF.getText());
                    nines[3]  = Integer.valueOf(bottomTF.getText());
                    String path = files.get(0).path().replace(Config.getProjectPath(), "");
                    pathTextField.setText(path);
                    drawableAttrListener.selected(title, path, nineCheck.isChecked(), nines);
                }
            }

            @Override
            public void canceled() {
                drawableAttrListener.cancel();
            }
        });
        chooseImageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                contentTable.getStage().addActor(chooser.fadeIn());

            }
        });


        VisTextField.TextFieldListener nineTFListener = new VisTextField.TextFieldListener() {
            @Override
            public void keyTyped(VisTextField textField, char c) {
                if (leftTF.isEmpty() || rightTF.isEmpty()||topTF.isEmpty()||bottomTF.isEmpty()){
                    return;
                }
                nines[0] = Integer.valueOf(leftTF.getText());
                nines[1] = Integer.valueOf(rightTF.getText());
                nines[2] = Integer.valueOf(topTF.getText());
                nines[3] = Integer.valueOf(bottomTF.getText());
                drawableAttrListener.changeNine(title,nines);
            }
        };

        leftTF.setTextFieldListener(nineTFListener);
        rightTF.setTextFieldListener(nineTFListener);
        topTF.setTextFieldListener(nineTFListener);
        bottomTF.setTextFieldListener(nineTFListener);

        leftTF.setTextFieldFilter(DigitsFieldFilter.instance);
        rightTF.setTextFieldFilter(DigitsFieldFilter.instance);
        topTF.setTextFieldFilter(DigitsFieldFilter.instance);
        bottomTF.setTextFieldFilter(DigitsFieldFilter.instance);

        VisTable table = new VisTable();
        table.add(new VisLabel("left"));
        table.add(leftTF).width(Config.width * 0.03f).spaceLeft(2);
        table.add(new VisLabel("right"));
        table.add(rightTF).width(Config.width * 0.03f).spaceLeft(2).row();
        table.add(new VisLabel("top"));
        table.add(topTF).width(Config.width * 0.03f).spaceLeft(2);
        table.add(new VisLabel("buttom"));
        table.add(bottomTF).width(Config.width * 0.03f).spaceLeft(2);
        collapsTable.add(table);

        nineCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int left = Integer.valueOf(leftTF.getText());
                int right = Integer.valueOf(rightTF.getText());
                int top = Integer.valueOf(topTF.getText());
                int bottom = Integer.valueOf(bottomTF.getText());
                String path = pathTextField.getText();
                drawableAttrListener.selected(title, path, nineCheck.isChecked(), new int[]{left, right, top, bottom});
                collapsibleWidget.setCollapsed(!nineCheck.isChecked());
            }
        });

        VisTextButton nineEditorBtn = new VisTextButton("editor");
        nineEditorBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final MainPanel mainPanel = new MainPanel();
                mainPanel.setListeners(contentTable.getStage());
                Texture texture = new Texture(Config.getImageFilePath(imagePath));
                mainPanel.setTexture(new TextureRegion(texture),nines);
                mainPanel.setSaveListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        nines[0] = mainPanel.getSplits()[0];
                        nines[1] = mainPanel.getSplits()[1];
                        nines[2] = mainPanel.getSplits()[2];
                        nines[3] = mainPanel.getSplits()[3];
                        mainPanel.fadeOut();
                        Gdx.app.log("tt","1"+nines[0]+"2"+nines[1]);
                        leftTF.setText(String.valueOf(nines[0]));
                        rightTF.setText(String.valueOf(nines[1]));
                        topTF.setText(String.valueOf(nines[2]));
                        bottomTF.setText(String.valueOf(nines[3]));
                        drawableAttrListener.changeNine(title,nines);
                        super.clicked(event, x, y);
                    }
                });
                contentTable.getStage().addActor(mainPanel);
            }
        });
        collapsTable.add(nineEditorBtn).spaceLeft(20);
        contentTable.add(pathTextField).width(Config.width * 0.12f).height(25).left();
        contentTable.add(chooseImageButton);
        contentTable.row();
        contentTable.add(nineCheck).left();
        contentTable.row();
        contentTable.add(collapsibleWidget).expandX().fillX();
        contentTable.row();
    }
}
