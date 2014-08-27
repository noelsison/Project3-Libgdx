package com.project3.resume;

import com.badlogic.gdx.Game;
import com.project3.screens.ResumeScreen;

public class Main extends Game {

	@Override
	public void create() {
//		AssetLoader.load();
		setScreen(new ResumeScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
//		AssetLoader.dispose();
	}
	
}