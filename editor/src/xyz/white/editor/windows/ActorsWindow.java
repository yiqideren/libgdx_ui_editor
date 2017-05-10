package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisWindow;

import xyz.white.editor.Config;

/**
 * Created by 10037 on 2017/4/15 0015.
 */

public class ActorsWindow extends VisWindow{
    private MainWindow targetWindow;

    public ActorsWindow(String title,MainWindow targetWindow) {
        super(title,false);
        setSize(Config.width*0.2f,Config.height/2);
        this.targetWindow = targetWindow;

        GridGroup table = new GridGroup(80,4);
        init(table);
        VisScrollPane scrollPane = new VisScrollPane(table);
        scrollPane.setOverscroll(true, false);
        add(scrollPane).expand().fill();
    }

    private void init(GridGroup visTable){
        VisImageButton label =  new VisImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("icon/label.png"))))
                            ,"Label");
        VisImageButton image =    new VisImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("icon/image.png"))))
                            ,"Image");
        VisImageButton button =   new VisImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("icon/button.png"))))
                            ,"Button");
        VisImageButton checkbox = new VisImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("icon/checkbox.png"))))
                            ,"CheckBox");
        VisImageButton textfield = new VisImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("icon/textfield.png"))))
                            ,"TextField");
        VisImageButton group = new VisImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("icon/group.png"))))
                            ,"Group");


        label.setUserObject(Config.LABEL);
        image.setUserObject(Config.IMAGE);
        button.setUserObject(Config.BUTTON);
        checkbox.setUserObject(Config.CHECKBOX);
        textfield.setUserObject(Config.TEXTFIELD);
        group.setUserObject(Config.GROUP);

        DragAndDrop dragAndDrop = new DragAndDrop();
        addActorToSelectList(visTable,dragAndDrop,label);
        addActorToSelectList(visTable,dragAndDrop,image);
        addActorToSelectList(visTable,dragAndDrop,button);
        addActorToSelectList(visTable,dragAndDrop,checkbox);
        addActorToSelectList(visTable,dragAndDrop,textfield);
        addActorToSelectList(visTable,dragAndDrop,group);
        dragAndDrop.addTarget(new DragAndDrop.Target(targetWindow) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                targetWindow.addActorIn(source.getActor(),x,y);
            }
        });
    }

    private void addActorToSelectList(GridGroup table,DragAndDrop dragAndDrop ,Actor actor){
        actor.setName(actor.getClass().getSimpleName());
        table.addActor(actor);
        dragAndDrop.addSource(SourceBuilder.builder(actor));
    }


   static class SourceBuilder{
      static DragAndDrop.Source builder(final Actor actor){
           return new DragAndDrop.Source(actor) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();

                    payload.setDragActor(new Label(actor.getName(), VisUI.getSkin()));

                    final Label validLabel = new Label(actor.getName(), VisUI.getSkin());
                    validLabel.setColor(Color.BLUE);
                    payload.setValidDragActor(validLabel);

                    final Label invalidLabel = new Label(actor.getName(), VisUI.getSkin());
                    invalidLabel.setColor(Color.RED);
                    payload.setInvalidDragActor(invalidLabel);
                    return payload;
                }
            };
        }

    }


}
