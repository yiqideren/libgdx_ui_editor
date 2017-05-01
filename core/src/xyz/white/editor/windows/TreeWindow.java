package xyz.white.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Draggable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;

import xyz.white.editor.Config;
import xyz.white.editor.EditorManager;
import xyz.white.editor.events.editor.ActorAddEvent;
import xyz.white.editor.events.editor.AttrEvent;
import xyz.white.editor.events.editor.SureActorEvent;
import xyz.white.editor.events.keyboard.KeyDelEvent;
import xyz.white.editor.events.listener.EditorEventListener;
import xyz.white.editor.events.listener.KeyBoardEventListener;
import xyz.white.editor.events.tree.TreeActorMoveEvent;
import xyz.white.editor.events.tree.TreeCancelEvent;
import xyz.white.editor.events.tree.TreeSelectedActroEvent;



/**
 * Created by 10037 on 2017/4/16 0016.
 */

public class TreeWindow extends VisWindow implements EditorEventListener,KeyBoardEventListener{
    private Tree tree;
    private Tree.Node stageNode;
    private Window mainWindw;
    private DragAndDrop dragAndDrop = new DragAndDrop();

    public TreeWindow(Window mainWindow) {
        super("Tree",false);
        this.mainWindw = mainWindow;
        EditorManager.getInstance().getEventBus().register(this);
        setSize(Config.width*0.2f, Config.height/2);
        init();
    }

    private void init(){
        Skin skin = VisUI.getSkin();

        tree = new Tree(skin);
        tree.expandAll();
        stageNode = createNodeItem(mainWindw,"Stage");
        tree.add(stageNode);
        this.addListener(clickListener);
        add(tree).expand().fill();

        dragAndDrop.addSource(new DragAndDrop.Source(tree) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {

                Tree.Node overNode = tree.getNodeAt(y);
                if (overNode != null ){
                    if (!tree.getSelection().contains(overNode)){
                        tree.getSelection().choose(overNode);
                    }
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(new Label("Node", VisUI.getSkin()));

                    final Label validLabel = new Label("Node", VisUI.getSkin());
                    validLabel.setColor(Color.BLUE);
                    payload.setValidDragActor(validLabel);

                    final Label invalidLabel = new Label("Node", VisUI.getSkin());
                    invalidLabel.setColor(Color.RED);
                    payload.setInvalidDragActor(invalidLabel);

                    payload.setObject(overNode);
                    return payload;
                }

                return null;
            }
        });

        dragAndDrop.addTarget(new DragAndDrop.Target(tree) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Tree.Node tagertNode = tree.getNodeAt(y);
                if (tagertNode !=null && tagertNode.getObject() instanceof Group &&(!payload.getObject().equals(tagertNode))){
                    return true;
                }

                return false;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Tree.Node tagertNode = tree.getNodeAt(y);
                if (tagertNode !=null && tagertNode.getObject() instanceof Group
                        &&(!payload.getObject().equals(tagertNode))){
                    Tree.Node sourceNode = (Tree.Node) payload.getObject();
                    sourceNode.remove();
                    tagertNode.add(sourceNode);
                    EditorManager.getInstance().getEventBus().post(new TreeActorMoveEvent(
                            (Actor ) sourceNode.getObject(),(Group) tagertNode.getObject()
                    ));
                }
            }
        });
    }

    private Tree.Node createNodeItem(Object object, String title){
        VisLabel label = new VisLabel(title);

        Tree.Node node = new Tree.Node(label);
        node.setObject(object);
        return node;
    }
    @Override
    public void changePos(AttrEvent attrEvent) {

    }


    @Override
    public void addActor(ActorAddEvent event) {

        Actor actor = event.actor;
        StringBuilder item_name = new StringBuilder();
        item_name.append(actor.getName()==null?"":actor.getName());
        item_name.append("[");
        item_name.append(EditorManager.getInstance()
                .getActorType(actor).getSimpleName());
        item_name.append("]");
        Tree.Node item = createNodeItem(actor,item_name.toString());
        item.getActor().setTouchable(Touchable.disabled);
        if (actor.getParent() instanceof MainWindow){
            stageNode.add(item);
        }else {
            Tree.Node parentNode = stageNode.findNode(actor.getParent());
            if (parentNode!=null) parentNode.add(item);
        }
        tree.getSelection().choose(item);

    }

    //Actor在编辑框中被点击
    @Override
    public void sureActor(SureActorEvent sureActorEvent) {
        Tree.Node selectedNode = tree.findNode(sureActorEvent.actor);
        if (selectedNode !=null && !tree.getSelection().contains(selectedNode)){
            tree.getSelection().choose(selectedNode);
        }

    }

    //节点点击监听
    private InputListener clickListener = new InputListener(){

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (tree.getStage().hit(x, y, true) == tree && tree.getNodeAt(y) != null){
                tree.getClickListener().clicked(event,x,y);
                tree.getClickListener().cancel();
                Array<Actor> actors = new Array<>();
                for (Tree.Node node:tree.getSelection().items() ) {
                    actors.add((Actor) node.getObject());
                }
                if (actors.size>0){
                    EditorManager.getInstance().getEventBus().post(new TreeSelectedActroEvent(actors));
                }else {
                    EditorManager.getInstance().getEventBus().post(new TreeCancelEvent());
                }

            }
            return false;
        }

    };



    @Override
    public void key_del(KeyDelEvent event) {
        if (tree.getSelection().size()>0){
            for (Tree.Node node : tree.getSelection().items()){
                if (node.getObject() instanceof Actor){
                    ((Actor) node.getObject()).remove();
                }
                tree.remove(node);
            }
            tree.getSelection().clear();
        }
    }
}
