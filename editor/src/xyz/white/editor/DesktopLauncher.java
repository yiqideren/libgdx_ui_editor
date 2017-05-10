package xyz.white.editor;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final MainEditor mainEditor = new MainEditor();
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		float scale = 1f;
		config.setTitle("UI Editor");
		config.setWindowIcon("logo.jpg");
		config.setWindowedMode((int)(1280*scale),(int)(720*scale));
		new Lwjgl3Application(mainEditor, config);
	}
}
