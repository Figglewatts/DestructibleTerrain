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
	
	private float currentTime = 0;
	private float elapsedTime = 0;
	private float lastTime = 0;
	private float leftOverTime = 0;
	private int timesteps = 0;
	
	@Override
	public void create() {		
		Texture.setEnforcePotImages(false); // don't enforce width/height powers of two
	
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		width = MathUtils.round(w);
		height = MathUtils.round(h);
		
		camera = new OrthographicCamera(1, h/w);
		camera.setToOrtho(false);
		batch = new SpriteBatch();
		
		pixmap = new Pixmap(Gdx.files.internal("textures/pixmap/pixmapTest.png"));
		
		pixmapTexture = new Texture(pixmap, Format.RGBA8888, false);
	}

	@Override
	public void dispose() {
		batch.dispose();
		pixmap.dispose();
		pixmapTexture.dispose();
	}

	@Override
	public void render() {	
		update();
		Gdx.gl.glClearColor(0, 0.8F, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(pixmapTexture, 0, 0, width, height);
		batch.end();
	}
	
	public void update() {
		elapsedTime = lastTime - currentTime;
		currentTime = System.nanoTime();
		lastTime = currentTime; // reset lastTime
		
		// add time that couldn't be used last frame
		elapsedTime += leftOverTime;
		
		// divide it up in chunks of 16ms
		timesteps = (int)Math.floor(elapsedTime / 16);
		
		// store time we couldn't use for next frame
		leftOverTime = elapsedTime - timesteps;
		
		for (int i = 0; i < timesteps; i++) {
			// update things here
			
		}
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
	private void drawPixel(int x, int y, Color color) {
		pixmap.setColor(color);
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
	
	private Vector2 rayCast(int startX, int startY, int endX, int endY) {
		int deltaX = (int)Math.abs(endX-startX);
		int deltaY = (int)Math.abs(endY-startY);
		int x = startX;
		int y = startY;
		
		int xInc1, xInc2, yInc1, yInc2;
		// determine whether x and y is increasing or decreasing
		if (endX >= startX) { // the x values are increasing
			xInc1 = 1;
			xInc2 = 1;
		} else { // the x values are decreasing
			xInc1 = -1;
			xInc2 = -1;
		}
		if (endY >= startY) { // the y values are increasing
			yInc1 = 1;
			yInc2 = 1;
		} else { // the y values are decreasing
			yInc1 = -1;
			yInc2 = -1;
		}
		
		int denominator, numerator, numAdd, numPixels;
		if (deltaX >= deltaY) { // there is at least 1 x-value for every y-value
			xInc1 = 0; // don't change the x when numerator >= denominator
			yInc2 = 0; // don't change the y for every iteration
			denominator = deltaX;
			numerator = deltaX / 2;
			numAdd = deltaY;
			numPixels = deltaX; // there are more x-values than y-values
		} else { // there is at least 1 y-value for every x-value
			xInc2 = 0; // don't change the x for every iteration
			yInc1 = 0; // don't change the y when numerator >= denominator
			denominator = deltaY;
			numerator = deltaY / 2;
			numAdd = deltaX;
			numPixels = deltaY; // there are more y-values than x-values
		}
		
		int prevX = startX;
		int prevY = startY;
		for (int curPixel = 0; curPixel <= numPixels; curPixel++) {
			if (isPixelSolid(x, y)) {
				if (GlobalSettings.DRAW_RAYCASTS) {
					drawPixel(x, y, Color.RED);
				}
				return new Vector2(prevX, prevY);
			}
			if (GlobalSettings.DRAW_RAYCASTS) {
				drawPixel(x, y, Color.MAGENTA);
			}
			prevX = x;
			prevY = y;
			
			numerator += numAdd; // increase the numerator by the top of the fraction
			
			if (numerator >= denominator) {
				numerator -= denominator; // calculate the new numerator value
				x += xInc1; // change x as appropriate
				y += yInc1; // change y as appropriate
			}
			
			x += xInc2; // change the x as appropriate
			y += yInc2; // change the y as appropriate
		}
		return null; // nothing found
	}
}
