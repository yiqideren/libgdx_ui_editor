package xyz.white.editor.windows.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import xyz.white.editor.Config;
import xyz.white.editor.utils.FileUtils;

import java.io.*;

/**
 * Created by 10037 on 2017/5/1 0001.
 */
public class NewSceneDialog extends VisWindow {
    private VisTable contentTable;
    private  FileHandle path;
    private NewSceneListener newSceneListener;

    public NewSceneDialog(FileHandle path,NewSceneListener newSceneListener) {
        super("New Scene");
        this.path = path;
        this.newSceneListener = newSceneListener;
        TableUtils.setSpacingDefaults(this);
        setModal(true);
        addCloseButton();
        closeOnEscape();

        init();
    }

    private void init() {

        contentTable = new VisTable(true);

        final VisLabel widthLabel = new VisLabel("width");
        final VisLabel nameLabel = new VisLabel("Name");
        final VisTextField nameTF = new VisTextField();
        final VisTextField widthTF = new VisTextField();
        VisLabel heightLabel = new VisLabel("height");
        final VisTextField heightTF = new VisTextField();

        VisTextButton okButton = new VisTextButton("OK", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (widthTF.getText().isEmpty()||heightTF.getText().isEmpty()||nameTF.getText().isEmpty()) return;
                final float width = Float.valueOf(widthTF.getText());
                final float height = Float.valueOf(heightTF.getText());
                final String name = nameTF.getText().toString();
                try {
                    if (newSceneListener !=null) newSceneListener.create( createSceneFile(width,height,name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                NewSceneDialog.this.fadeOut();
            }
        });

        contentTable.add(nameLabel);
        contentTable.add(nameTF);
        contentTable.row();
        contentTable.add(widthLabel);
        contentTable.add(widthTF);
        contentTable.add(heightLabel);
        contentTable.add(heightTF);
        contentTable.row();
        contentTable.add();
        contentTable.add();
        contentTable.add();
        contentTable.add(okButton).expandX().right();
        contentTable.center();
        add(contentTable).pad(10);
        pack();
        centerWindow();
    }

    private FileHandle createSceneFile(float width,float height,String name) throws IOException {
        if (!path.isDirectory()){
            path = path.parent();
        }
        StringBuilder filePath = new StringBuilder();
        filePath.append(path.path());
        filePath.append("/");
        filePath.append(name);
        filePath.append(".");
        filePath.append(Config.sceneExtension);
        FileHandle fileHandle = new FileHandle(filePath.toString());
        if (fileHandle.file().exists()) fileHandle.delete();
        fileHandle.file().createNewFile();

        Writer writer = new FileWriter(fileHandle.file());
        FileUtils.createScene(writer,width,height,name);
        writer.close();
        return fileHandle;
    }

    public interface NewSceneListener{
        void create(FileHandle fileHandle);
    }
}
