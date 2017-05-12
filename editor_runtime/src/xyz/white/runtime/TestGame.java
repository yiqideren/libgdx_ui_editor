package xyz.white.runtime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by 10037 on 2017/5/10 0010.
 */
public class TestGame extends Game {
    private Stage stage;

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        float scale=1f;
        config.width= (int) (800*scale);
        config.height= (int) (480*scale);
        new LwjglApplication(new TestGame(), config);
    }

    @Override
    public void create() {
        stage = new Stage();
        StageLoad.loadScene(stage.getRoot(),"scenes/testscene.vwx");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage!=null) {
            stage.act();
            stage.draw();
        }
    }
}
