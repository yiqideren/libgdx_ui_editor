package xyz.white.runtime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class TestGame extends Game {
    private Stage stage;

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        float scale=1f;
        config.width= (int) (480*scale);
        config.height= (int) (800*scale);
        new LwjglApplication(new TestGame(), config);
    }

    @Override
    public void create() {
        stage = new Stage();
        StageLoad.loadScene(stage.getRoot(),"scenes/testscene.vwx");
    }

    @Override
    public void render() {
        super.render();
        if (stage!=null) {
            stage.act();
            stage.draw();
        }
    }
}
