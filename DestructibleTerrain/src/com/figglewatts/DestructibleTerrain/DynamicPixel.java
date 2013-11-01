package com.figglewatts.DestructibleTerrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class DynamicPixel {
	private Vector2 position = Vector2.Zero;
	private Vector2 velocity = Vector2.Zero;
	private Vector2 oldPosition = Vector2.Zero;
	private Color color = Color.WHITE;
	private float stickiness = 0F;
	private float bounciness = 0F;
	
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public Vector2 getVelocity() {
		return velocity;
	}
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	public Vector2 getOldPosition() {
		return oldPosition;
	}
	public void setOldPosition(Vector2 oldPosition) {
		this.oldPosition = oldPosition;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public float getStickiness() {
		return stickiness;
	}
	public void setStickiness(float stickiness) {
		this.stickiness = stickiness;
	}
	public float getBounciness() {
		return bounciness;
	}
	public void setBounciness(float bounciness) {
		this.bounciness = bounciness;
	}
	
	public void Update(float delta) {
		this.position = this.position.add(this.velocity.mul(delta));
	}
	
	public DynamicPixel(int x, int y) {
		this.position = new Vector2(x, y);
	}
	public DynamicPixel(Vector2 position) {
		this.position = position;
	}
	public DynamicPixel(int x, int y, Color color) {
		this.position = new Vector2(x, y);
		this.color = color;
	}
	public DynamicPixel(int x, int y, Color color, float stickiness, float bounciness) {
		this.position = new Vector2(x, y);
		this.color = color;
		this.stickiness = stickiness;
		this.bounciness = bounciness;
	}
	public DynamicPixel(Vector2 position, Color color) {
		this.position = position;
		this.color = color;
	}
	public DynamicPixel(int x, int y, int xVel, int yVel) {
		this.position = new Vector2(x, y);
		this.velocity = new Vector2(xVel, yVel);
	}
	public DynamicPixel(int x, int y, Color color, int xVel, int yVel) {
		this.position = new Vector2(x, y);
		this.color = color;
		this.velocity = new Vector2(xVel, yVel);
	}
	public DynamicPixel(Vector2 position, Color color, Vector2 velocity) {
		this.position = position;
		this.color = color;
		this.velocity = velocity;
	}
}
