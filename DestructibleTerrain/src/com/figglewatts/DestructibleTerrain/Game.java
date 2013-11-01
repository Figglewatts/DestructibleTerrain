package com.figglewatts.DestructibleTerrain;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Game implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Pixmap pixmap;
	private Texture pixmapTexture;
	private int width;
	private int height;
	
	@Override
	public void create() {		
		Texture.setEnforcePotImages(false); // don't enforce width/height powers of two
		
		GlobalSettings.DRAW_CALCULATED_NORMALS = true;
	
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		width = MathUtils.round(w);
		height = MathUtils.round(h);
		
		camera = new OrthographicCamera(1, h/w);
		camera.setToOrtho(false);
		batch = new SpriteBatch();
		
		pixmap = new Pixmap(Gdx.files.internal("textures/pixmap/pixmapTest.png"));
		
		pixmapTexture = new Texture(pixmap, Format.RGBA8888, false);
		findNormal(255, 159);
		findNormal(338, 170);
		findNormal(320, 170);
		findNormal(300, 173);
		findNormal(275, 170);
		findNormal(235, 154);
	}

	@Override
	public void dispose() {
		batch.dispose();
		pixmap.dispose();
		pixmapTexture.dispose();
	}

	@Override
	public void render() {	
		Gdx.gl.glClearColor(0, 0.8F, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(pixmapTexture, 0, 0, width, height);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	
	}

	@Override
	public void pause() {
	
	}

	@Override
	public void resume() {
	
	}
	
	private void pixmapToTexture() {
		pixmapTexture = new Texture(pixmap, Format.RGBA8888, false);
	}
	
	private void drawPixel(int x, int y) {
		pixmap.setColor(Color.RED);
		pixmap.drawPixel(x, y);
		pixmapToTexture();
	}
	
	private void drawCircle(int x, int y, int radius) {
		pixmap.setColor(Color.RED);
		pixmap.fillCircle(x, y, radius);
		pixmapToTexture();
	}
	
	private boolean isPixelSolid(int x, int y) {
		int tempRGB8888 = pixmap.getPixel(x, y);
		Color tempColor = new Color();
		Color.rgba8888ToColor(tempColor, tempRGB8888);
		if (tempColor.a == 1.0F) {
			return true;
		} else {
			return false;
		}
	}
	
	private SurfaceNormal findNormal(int xPos, int yPos) {
		Vector2 average = new Vector2();
		for (int x = -3; x <= 3; x++) {
			for (int y = -3; y <= 3; y++) {
				if (isPixelSolid(x+xPos, y+yPos)) {
					average.sub(x, y);
				}
			}
		}
		Vector2 normal = average.nor();
		normal.mul(20F);
		SurfaceNormal surfaceNormal = new SurfaceNormal(xPos, yPos, normal);
		if (GlobalSettings.DRAW_CALCULATED_NORMALS) {
			drawNormal(surfaceNormal);
		}
		return surfaceNormal;
	}
	
	private void drawNormal(SurfaceNormal surfaceNormal) {
		pixmap.setColor(Color.BLACK);
		pixmap.drawLine((int)surfaceNormal.getPosition().x, (int)surfaceNormal.getPosition().y,
						(int)surfaceNormal.getNormalPosition().x, (int)surfaceNormal.getNormalPosition().y);
		pixmapToTexture();
	}
}
