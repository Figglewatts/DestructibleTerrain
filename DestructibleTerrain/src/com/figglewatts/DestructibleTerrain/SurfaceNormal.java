package com.figglewatts.DestructibleTerrain;

import com.badlogic.gdx.math.Vector2;

public class SurfaceNormal {
	private Vector2 position;
	private Vector2 normal;
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public Vector2 getNormal() {
		return normal;
	}
	public void setNormal(Vector2 normal) {
		this.normal = normal;
	}
	
	public SurfaceNormal(Vector2 position, Vector2 normal) {
		this.position = position;
		this.normal = normal;
	}
	public SurfaceNormal(int x, int y, Vector2 normal) {
		this.position = new Vector2(x, y);
		this.normal = normal;
	}
	
	public Vector2 getNormalPosition() {
		return new Vector2(this.position.x + this.normal.x, this.position.y + this.normal.y);
	}
}
