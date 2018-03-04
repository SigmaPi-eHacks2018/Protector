/*
 * Copyright (c) 2018, SigmaPi-eHacks2018 Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sigmapi.protector.core.world.entity.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.skin.LaserSkin;
import org.sigmapi.protector.core.skin.UfoSkin;
import org.sigmapi.protector.core.world.World;
import org.sigmapi.protector.core.world.entity.AbstractEntity;

import java.util.Deque;

import lombok.Getter;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class Ufo extends AbstractEntity
{

	private final UfoSkin skin;
	private final Texture texture;
	private final TextureRegion[] animations;
	private final Texture explosion;
	private final TextureRegion[] explosions;
	private float time;
	private float speed;

	private final int maxShots;
	private int shots = 0;

	@Getter
	private boolean exploded = false;
	private int frames = 0;
	private int anim = 0;

	public Ufo(World world, UfoSkin skin, float x, float y, float xVel, float yVel, int maxShots, float speed)
	{
		super(world, (Statics.WIDTH / skin.getRatio()), x, y, xVel, yVel);
		this.skin = skin;
		this.texture = world.getProtector().getAssets().get(skin.getPath(), Texture.class);
		this.animations = new TextureRegion[2];
		this.animations[0] = new TextureRegion(texture, 0, 0, 45, 45);
		this.animations[1] = new TextureRegion(texture, 45, 0, 45, 45);
		this.explosion = world.getProtector().getAssets().get(Statics.GAME_EXPLOSION, Texture.class);
		this.explosions = new TextureRegion[5];
		for (int i = 0; i < explosions.length; i++)
		{
			this.explosions[i] = new TextureRegion(explosion, (i * 45), 0, 45, 45);
		}

		this.maxShots = maxShots;
		this.speed = speed;
	}

	@Override
	public void update(float delta)
	{
		y += (yVel * delta);
		if ((y + length) <= 0)
		{
			world.getUfosRemove().add(this);
			return;
		}

		if (!exploded)
		{
			time += delta;
			if (time >= speed &&  shots < maxShots)
			{
				float lx = x + (length / 2);
				float ly = getY() - 50;
				float lxvel = (((world.getVessel().getX() - lx) / 2));
				world.getLasers().add(new Laser(world, LaserSkin.UFO, lx, ly, lxvel, Statics.HEIGHT / -3.0f));

				shots++;
				time = 0;
			}
		}

		frames++;
		if (exploded)
		{
			if (frames == 10)
			{
				frames = 0;

				anim++;
				if (anim == 5)
				{
					world.getUfosRemove().add(this);
				}
			}
		}

		else
		{
			if (frames == 10)
			{
				anim++;
				frames = 0;
			}
		}
	}

	@Override
	public void render(SpriteBatch batch)
	{
		if (exploded)
		{
			batch.draw(explosions[anim], x, y, length, length);
		}

		else
		{
			batch.draw(animations[anim & 0x1], x, y, length / 2.0f, length / 2.0f, length, length, 0.70f, 0.70f, 0.0f);
		}
	}

	@Override
	public void accept(Deque<InputEvent> events)
	{
	}

	public void setExploded(boolean exploded)
	{
		this.exploded = exploded;
		this.frames = 0;
		this.anim = 0;
	}
}
