package com.project3.actors;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.project3.utils.poi.ResultModel;

public class ScoreWindow extends Window {

	private ScrollPane scrollPane;
	private Table summaryTable;
	private List<ResultModel> resultList;
	private int rawScore;
	private int totalScore;
	private ShapeRenderer shapeRenderer;
	static private boolean projectionMatrixSet;

	private Image stamp;

	public ScoreWindow(String title, WindowStyle style, Skin skin,
			BitmapFont font11, BitmapFont font14, BitmapFont font18,
			List<ResultModel> resultList) {
		super(title, style);

		shapeRenderer = new ShapeRenderer();
		projectionMatrixSet = false;
		summaryTable = new Table();

		String oldText = "";
		String bg = "";
		int index = 0;

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font11;
		labelStyle.fontColor = new Color(99 / 255f, 92 / 255f, 92 / 255f, 1f);

		for (ResultModel r : resultList) {
			String currText = r.getText();
			if (oldText.equals(currText)) {
				currText = "";
			} else {
				index++;
			}

			if (index % 2 == 0) {
				bg = "ui/bg_table-even.9.png";
			} else {
				bg = "ui/bg_table-odd.9.png";
			}

			Table temp = new Table();
			temp.setBackground(new NinePatchDrawable(getNinePatch((bg))));

			oldText = r.getText();
			Label text = new Label(currText, labelStyle);
			text.setWrap(true);
			temp.add(text).width(280).spaceRight(30);

			Label property = new Label(r.getProperty(), labelStyle);
			property.setWrap(true);
			temp.add(property).width(150).align(Align.top);

			Label value = new Label(r.getValue(), labelStyle);
			value.setWrap(true);
			temp.add(value).width(100).align(Align.top);

			Label score = new Label(r.getRawScore() + "", labelStyle);
			score.setWrap(true);
			temp.add(score).width(50).align(Align.top);

			// Label correct = new Label(String.valueOf(r.correct()),
			// labelStyle);
			// correct.setWrap(true);
			Image check = new Image(new TextureRegion(new Texture(
					Gdx.files.internal("ui/check.png"))));
			Image wrong = new Image(new TextureRegion(new Texture(
					Gdx.files.internal("ui/wrong.png"))));

			if (r.correct()) {
				// check.scaleBy(-.1f);
				check.scaleBy(-.1f, -.1f);
				temp.addActor(check);
				check.setX(580);
				// temp.add(check).width(40f).height(40f).align(Align.top);
			} else {
				wrong.scaleBy(-.1f, -.1f);
				temp.addActor(wrong);
				wrong.setX(580);
				// temp.add(wrong).height(40f).height(40f).align(Align.top);
			}

			summaryTable.add(temp);
			summaryTable.row();

			rawScore += r.getRawScore();
			totalScore += r.getTotalScore();
		}

		Table headerTable = new Table(skin);

		VerticalGroup group = new VerticalGroup();

		LabelStyle style14 = new LabelStyle();
		style14.font = font14;
		Label activityLabel = new Label("ACTIVITY", style14);
		activityLabel.setColor(Color.YELLOW);

		LabelStyle style18 = new LabelStyle();
		style18.font = font18;
		Label titleLabel = new Label("Score Summary", style18);
		titleLabel.setColor(Color.WHITE);
		
		
		group.addActor(activityLabel);
		group.addActor(titleLabel);
		group.left();
		headerTable.add(group).width(400).align(Align.left);
		headerTable.add("").height(60f);

		group = new VerticalGroup();
		Label highScoreLabel = new Label("HIGH SCORE", style14);
		highScoreLabel.setColor(Color.WHITE);

		Label scoreLabel = new Label(rawScore + "/" + totalScore, style18);
		scoreLabel.setColor(Color.YELLOW);

		group.addActor(highScoreLabel);
		group.addActor(scoreLabel);
		group.left();
		headerTable.add(group).width(200).align(Align.left);

		headerTable.row().bottom();

		scrollPane = new ScrollPane(summaryTable);
		this.align(Align.top);
		this.add(headerTable).left().width(710);
		this.row().spaceBottom(40f);
		this.add(scrollPane).height(280).spaceTop(50f);
		this.row();

		Label totalLabel = new Label("TOTAL", style14);
		totalLabel.setColor(Color.GRAY);
		Label scoreBigLabel = new Label(rawScore + "/" + totalScore, style18);
		scoreBigLabel.setColor(new Color(1 / 255f, 169 / 255f, 124 / 255f, 1f));

		String stampFile;

		if ((rawScore / (totalScore * 1f) >= .6f)) {
			stampFile = "ui/pass.png";
		} else {
			stampFile = "ui/fail.png";
		}
		stamp = new Image(new TextureRegion(new Texture(
				Gdx.files.internal(stampFile))));

		Table totalTable = new Table();
		totalTable.add(totalLabel).spaceRight(20);
		totalTable.add(scoreBigLabel);
		totalTable.row();

		this.add(totalTable).right().width(710);
		this.row();
		// this.add(stamp);
		this.addActor(stamp);
		stamp.setPosition(440, 20);
	}

	private NinePatch getNinePatch(String fname) {

		final Texture t = new Texture(Gdx.files.internal(fname));
		return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2,
				t.getWidth() - 2), 10, 10, 10, 10);
	}

	public void setResultList(List<ResultModel> list) {
		this.resultList = list;
	}

	public String getMessage() {
		if ((rawScore / (totalScore * 1f) >= .6f)) {
			return "Congratulations! You got a passing score. You may now proceed to the next activity.";
		} else {
			return "Sorry. You failed to get a passing score. You may retry by uploading your resume again.";
		}
	}

	// @Override
	// public void draw(Batch batch, float parentAlpha) {
	//
	// if (!projectionMatrixSet) {
	// shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
	// }
	// shapeRenderer.begin(ShapeType.Filled);
	// shapeRenderer.setColor(new Color(0, 0, 0, .5f));
	// shapeRenderer.rect(0, 0, this.getStage().getWidth(), this.getStage()
	// .getHeight());
	// shapeRenderer.end();
	// batch.end();
	// batch.begin();
	// super.draw(batch, parentAlpha);
	// }
}
