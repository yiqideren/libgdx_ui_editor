package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.file.FileUtils;
import com.kotcrab.vis.ui.widget.file.internal.FileChooserText;
import net.mwplay.nativefont.NativeLabel;
import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.actors.FileItem;
import xyz.white.editor.events.application.LoadProjectEvent;
import xyz.white.editor.events.assets.LoadSceneEvent;
import xyz.white.editor.events.listener.ApplicationEventListener;
import xyz.white.editor.windows.dialogs.NewSceneDialog;

import java.awt.*;
import java.io.IOException;


/**
 * Created by 10037 on 2017/4/23 0023.
 */
public class AssetWindow extends VisTable implements ApplicationEventListener {
    private VisTable contentTable,toobarTable;
    private GridGroup filesView;
    private FileHandle curFileHandle;
    private Label fileNameLabel;
    private AssetPopupMenu rightMenu;

    public AssetWindow() {
        setBackground("window-bg");
        setSize(Config.width*0.6f,Config.height*0.3f);
        EditorManager.getInstance().getEventBus().register(this);
        init();
    }

    private void init(){

        toobarTable = new VisTable(true);
        VisImageButton preImage = new VisImageButton(VisUI.getSkin().getDrawable("icon-folder-parent"),
                "Previous Directory");
        VisImageButton createDirImage = new VisImageButton(VisUI.getSkin().getDrawable("icon-folder-new"),"Create A New Directory");

        fileNameLabel = new NativeLabel("",EditorManager.getInstance().getMainFont());

        toobarTable.add(preImage).expandX().left().padLeft(10);
        toobarTable.add(createDirImage).expandX().left().padLeft(5);
        toobarTable.addSeparator(true).expandX().left().padLeft(4);
        toobarTable.add(fileNameLabel).width(650).expandX().left().padLeft(4);
        preImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (curFileHandle!=null ){
                    load(curFileHandle.parent().path());

                }
                super.clicked(event, x, y);
            }
        });

        createDirImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (curFileHandle!=null){
                    Dialogs.showInputDialog(getStage(), FileChooserText.NEW_DIRECTORY_DIALOG_TITLE.get(), FileChooserText.NEW_DIRECTORY_DIALOG_TEXT.get(), true, new InputDialogAdapter() {
                        @Override
                        public void finished (String input) {
                            FileHandle currentDirectory = curFileHandle;
                            if (!currentDirectory.isDirectory()){
                                currentDirectory = currentDirectory.parent();
                            }
                            if (FileUtils.isValidFileName(input) == false) {
                                Dialogs.showErrorDialog(getStage(), FileChooserText.NEW_DIRECTORY_DIALOG_ILLEGAL_CHARACTERS.get());
                                return;
                            }

                            for (FileHandle file : currentDirectory.list()) {
                                if (file.name().equals(input)) {
                                    Dialogs.showErrorDialog(getStage(), FileChooserText.NEW_DIRECTORY_DIALOG_ALREADY_EXISTS.get());
                                    return;
                                }
                            }

                            currentDirectory.child(input).mkdirs();
                            load(curFileHandle.path());
                        }
                    });
                }

                super.clicked(event, x, y);
            }
        });

        rightMenu = new AssetPopupMenu();
        rightMenu.addItem(new MenuItem("New Scene", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(new NewSceneDialog(curFileHandle, new NewSceneDialog.NewSceneListener() {
                    @Override
                    public void create(FileHandle fileHandle) {
                        AssetWindow.this.load(fileHandle.path());
                    }
                }).fadeIn());
            }
        }));

        rightMenu.addItem(new MenuItem("Open On System", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    Desktop.getDesktop().open(curFileHandle.file());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));

        filesView = new GridGroup(80,4);
        final VisScrollPane scrollPane = new VisScrollPane(filesView);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true,false);
        contentTable = new VisTable(true);
        filesView.debug();

        scrollPane.addListener(rightMenu.getDefaultInputListener());
        contentTable.add(toobarTable).expandX().fillX().padTop(5);
        contentTable.row();
        contentTable.add(new Separator()).expandX().fillX().padTop(0);
        contentTable.row();
        contentTable.add(scrollPane).expand().fill();
        add(contentTable).expand().fill();


        String path = Config.getProjectPath();
        if (path!=null&&!path.isEmpty()){
            load(path);
        }
    }

    private void load(String path){
        filesView.clearChildren();
        filesView.layout();
        FileHandle file =Gdx.files.absolute(path);
        curFileHandle = file;
        if (file.isDirectory()){
            for (FileHandle child :file.list()){
                if (child.name().startsWith(".")) continue;
                loadAsset(child);
            }
        }else {
            loadAsset(file);
        }
    }

    private ClickListener fileClickListener = new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (event.getListenerActor() instanceof FileItem){
                FileItem item = (FileItem) event.getListenerActor();
                if (getTapCount() == 2){
                    if (item.getFile().isDirectory()) {
                        AssetWindow.this.load(item.getFile().path());
                    }else if (item.getFile().extension().equals(Config.sceneExtension)){
                        EditorManager.getInstance().getEventBus().post(new LoadSceneEvent(item.getFile()));
                    }
                }
                item.setSelected();
                fileNameLabel.setText(item.getFile().path());
            }
            super.clicked(event, x, y);
        }
    };

    private void loadAsset(FileHandle file){
        FileItem fileItem = new FileItem(file);
        fileItem.addListener(fileClickListener);
        filesView.addActor(fileItem);
    }


    @Override
    public void LoadProject(LoadProjectEvent event) {
        String projectPath = Config.getProjectPath();
        if (!projectPath.isEmpty()){
            load(projectPath);
        }
    }

    private class AssetPopupMenu extends PopupMenu{


        public void build(FileItem fileItem){
            clearChildren();
            if (fileItem == null){
                addItem(new MenuItem("New Scene", new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {

                    }
                }));
            }else {

            }
        }
    }
}
