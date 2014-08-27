package com.project3.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.project3.screens.ResumeScreen;

public class DialogueBox extends TextArea {

	private ResumeScreen screen;
	private TextFieldStyle style;
	private boolean clickable = false;

	private List<String> textList;
	private int currListIndex = 0;

	private String currText = "";
	private int currTextIndex = 0;

	private float blinkTime = 0.5f;
	private boolean cursorOn = true;
	private long lastBlink;

	public DialogueBox(String text, TextFieldStyle style) {
		super(text, style);
		this.style = style;
	}

	@Override
	protected InputListener createInputListener() {
		return new TextFieldClickListener();
	}

	private class TextFieldClickListener extends ClickListener {
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			Gdx.app.log("DialogueBox", "Dialogue clicked");
			if (currListIndex + 1 < textList.size()) {
				currListIndex++;
				updateDisplay();
				return true;
			}
			return false;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		blink();
		if (cursorOn && clickable) {
			drawCursor(style.cursor, batch, style.font,
					this.getX() + this.getWidth() - 40, this.getY() + 30);

		}

		if (currTextIndex < currText.length()) {
			if (currTextIndex + 2 > currText.length()) {
				currTextIndex++;
			} else {
				currTextIndex += 2;
			}
			setText(currText.substring(0, currTextIndex));
		}
	}

	private void blink() {
		long time = TimeUtils.nanoTime();
		if ((time - lastBlink) / 1000000000.0f > blinkTime) {
			cursorOn = !cursorOn;
			lastBlink = time;
		}
	}

	public void setClickable(boolean b) {
		clickable = b;
	}

	public void setScreen(ResumeScreen screen) {
		this.screen = screen;
	}

	public void setTextList(List<String> list) {
		this.textList = list;
	}

	public void addText(String text) {
		if (textList == null) {
			textList = new ArrayList<String>();
		}

		textList.add(text);
	}

	public void reset() {
		textList.clear();
		currListIndex = 0;
		currText = "";
		currTextIndex = 0;
		setClickable(false);
	}

	public void updateDisplay() {
		currText = textList.get(currListIndex);
		currTextIndex = 0;
		setClickable(currListIndex + 1 < textList.size());
	}
	

	public void updateDisplay(String str) {
		reset();
		addText(str);
		updateDisplay();
	}
}
