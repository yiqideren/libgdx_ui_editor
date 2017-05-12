package xyz.white.editor.factory;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import net.mwplay.nativefont.NativeTextField;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.DigitsFieldFilter;

/**
 * Created by 10037 on 2017/5/12 0012.
 */
public class AttrFactory {

    public interface DrawableAttrListener {
        void selected(String title, String path, boolean isNine, int[] nines);

        void cancel();

        void changeNine(String title, int[] nines);
    }

    public static void createDrawableChooseAttr(final Table contentTable, final String title, String imagePath, boolean isNine,int[] nines, final DrawableAttrListener drawableAttrListener) {
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
        VisTable table = new VisTable();
        final CollapsibleWidget collapsibleWidget = new CollapsibleWidget(table);
        collapsibleWidget.setCollapsed(!nineCheck.isChecked());
        final VisTextField leftTF = new VisTextField(String.valueOf(nines[0]));
        final VisTextField rightTF = new VisTextField(String.valueOf(nines[1]));
        final VisTextField topTF = new VisTextField(String.valueOf(nines[2]));
        final VisTextField bottomTF = new VisTextField(String.valueOf(nines[3]));
        chooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        chooser.setListener(new FileChooserListener() {
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size > 0) {
                    int left = Integer.valueOf(leftTF.getText());
                    int right = Integer.valueOf(rightTF.getText());
                    int top = Integer.valueOf(topTF.getText());
                    int bottom = Integer.valueOf(bottomTF.getText());
                    String path = files.get(0).path().replace(Config.getProjectPath(), "");
                    pathTextField.setText(path);
                    drawableAttrListener.selected(title, path, nineCheck.isChecked(), new int[]{left, right, top, bottom});
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
                int left = Integer.valueOf(leftTF.getText());
                int right = Integer.valueOf(rightTF.getText());
                int top = Integer.valueOf(topTF.getText());
                int bottom = Integer.valueOf(bottomTF.getText());
                drawableAttrListener.changeNine(title, new int[]{left, right, top, bottom});
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

        table.add(new VisLabel("left"));
        table.add(leftTF).width(Config.width * 0.05f).spaceLeft(5);
        table.add(new VisLabel("right"));
        table.add(rightTF).width(Config.width * 0.05f).spaceLeft(5).row();
        table.add(new VisLabel("top"));
        table.add(topTF).width(Config.width * 0.05f).spaceLeft(5);
        table.add(new VisLabel("buttom"));
        table.add(bottomTF).width(Config.width * 0.05f).spaceLeft(5);

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

        contentTable.add(pathTextField).width(Config.width * 0.15f).height(25).left();
        contentTable.add(chooseImageButton);
        contentTable.row();
        contentTable.add(nineCheck).left();
        contentTable.row();
        contentTable.add(collapsibleWidget).width(Config.width * 0.15f);
        contentTable.row();
    }
}
