package xyz.white.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

public class MainEditor extends ApplicationAdapter {
	private MainStage mainStage;
	private Viewport mainViewPort;

	@Override
	public void create () {
		this.resize(Config.width,Config.height);
//		VisUI.load("skins/tixel/x1/tixel.json");
		VisUI.load();
		mainViewPort = new FillViewport(Config.width,Config.height);
		mainStage = new MainStage(mainViewPort);
		Gdx.input.setInputProcessor(mainStage);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mainStage.act(Gdx.graphics.getDeltaTime());
		mainStage.draw();
	}

	@Override
	public void resize(int width, int height) {
			super.resize(width, height);
			if (mainViewPort!=null)mainViewPort.update(width,height,true);
	}

	@Override
	public void dispose () {
	    Gdx.app.log("app","dispose-----1111-----");
        EditorManager.getInstance().clearEvents();
		VisUI.dispose();
		mainStage.dispose();
	}
}
