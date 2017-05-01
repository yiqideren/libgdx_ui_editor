package xyz.white.editor.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import xyz.white.editor.Config;


/**
 * Created by 10037 on 2017/4/23 0023.
 */
public class FileItem extends VisTable {
    private FileHandle file;
    private boolean selected = false;

    public FileItem(FileHandle file) {
        this.file = file;
        setBackground("menu-bg");
        VisImage image;
        if (file.isDirectory()) {
            image = new VisImage(
                    new TextureRegion(
                            new Texture(Gdx.files.internal("icon/folder_64.png")))
            );
        } else {
            final String extension = file.extension();
            switch (extension) {
                case "jpg":
                case "png":
                    TextureRegion drawable = new TextureRegion(new Texture(file));
                    image = new VisImage(drawable);
                    break;
                case Config.sceneExtension:
                    image = new VisImage(new TextureRegion(new Texture(Gdx.files.internal("icon/scene.png"))));
                    break;
                default:
                    image = new VisImage(
                            new TextureRegion(
                                    new Texture(Gdx.files.internal("icon/file.png"))
                            ));
                    break;
            }
        }
        image.setScaling(Scaling.fit);
        add(image).size(42);
        row();
        VisLabel nameLabel = new VisLabel(file.name());
        nameLabel.setEllipsis(true);

        add(nameLabel).expandX().width(80f);
        nameLabel.setAlignment(Align.center);
        row();
    }

    public void setSelected() {
        if (selected){
            setBackground("menu-bg");
        } else{
            setBackground("selection");
        }
        selected = !selected;
    }

    public FileHandle getFile() {
        return file;
    }
}
