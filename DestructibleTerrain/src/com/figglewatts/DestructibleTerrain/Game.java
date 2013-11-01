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

public class Game implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Pixmap pixmap;
	private Texture pixmapTexture;
	private int width;
	private int height;
	
	private int circleRadius = 2;
	
	private Texture mapImage;
	
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
		
		/*pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(Color.BLUE);
		pixmap.fillRectangle(0, 0, width, height);
		pixmap.setColor(Color.RED);
		pixmap.fillCircle(width/2, height/2, circleRadius);*/
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
		//updatePixmap();
		Gdx.gl.glClearColor(0, 0, 0.7F, 1);
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
	
	private void updatePixmap() {
		circleRadius++;
		pixmap.setColor(Color.RED);
		pixmap.fillCircle(width/2, height/2, circleRadius);
		pixmapTexture = new Texture(pixmap, Format.RGBA8888, false);
	}
}
