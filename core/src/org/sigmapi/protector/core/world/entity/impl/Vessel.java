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

import org.sigmapi.protector.core.Profile;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.input.impl.TouchDraggedEvent;
import org.sigmapi.protector.core.skin.LaserSkin;
import org.sigmapi.protector.core.skin.VesselSkin;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.world.World;
import org.sigmapi.protector.core.world.entity.AbstractEntity;

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.Getter;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class Vessel extends AbstractEntity
{

	private final VesselSkin skin;
	private final Profile profile;
	private final Texture texture;
	private final TextureRegion[] animations;
	private final Texture explosion;
	private final TextureRegion[] explosions;
	private final Texture shield;
	private final TextureRegion[][] shields;
	private final Deque<TouchDraggedEvent> drags;

	private final float speed;

	private final LaserSkin laser;

	@Getter
	private boolean exploded = false;

	@Getter
	private boolean draw = true;
	private float time;
	private int frames = 0;
	private int anim = 0;

	private int shieldFrames = 0;
	private int shieldAnim = 0;
	private int shieldLevel = 0;
	private boolean drawShield = true;
	private boolean shieldIn = true;
	private float hitTime;
	private float lastHit;


	public Vessel(World world, VesselSkin skin, Profile profile)
	{
		super(world, (Statics.WIDTH / skin.getRatio()), ((Statics.WIDTH / 2.0f) - ((Statics.WIDTH / skin.getRatio()) / 2.0f)), 0.0f, 0.25f, 0.25f);
		this.skin = skin;
		this.laser = LaserSkin.values()[profile.getLaser()];
		this.profile = profile;

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

		this.shield = world.getProtector().getAssets().get(Statics.GAME_SHIELD, Texture.class);
		this.shields = new TextureRegion[5][5];
		for (int i = 0; i < shields.length; i++)
		{
			for (int j = 0; j < shields[i].length; j++)
			{
				this.shields[j][i] = new TextureRegion(shield, (i * 45), (j * 45), 45, 45);
			}
		}

		this.drags = new ArrayDeque<>();

		this.speed = ((Statics.MAX_SPEED - profile.getSpeed()) / Statics.MAX_SPEED) / 2.0f;
	}

	@Override
	public void update(float delta)
	{
		if (!exploded)
		{
			float deltaX = 0.0f;
			float deltaY = 0.0f;

			if (!drags.isEmpty())
			{
				TouchDraggedEvent event;
				while ((event = drags.poll()) != null)
				{
					deltaX += (event.getScreenX() - (x + (length / 2)));
					deltaY += ((Statics.HEIGHT - event.getScreenY()) - y);
				}
			}

			x += ((deltaX * 0.60f) * xVel);
			if (x < 0)
			{
				x = 0;
			}

			y += ((deltaY * 0.60f) * yVel);
			if (y < 0)
			{
				y = 0;
			}

			time += delta;
			hitTime += delta;

			if ((hitTime - 2) >= lastHit)
			{
				lastHit = 0.0f;
			}

			if (time >= speed)
			{
				float lx = (x + (length / 2));
				float ly = (y + length);
				world.getLasers().add(new Laser(world, laser, lx, ly, 0f, Statics.HEIGHT / 3.0f));
				time = 0;
			}
		}

		frames++;
		shieldFrames++;
		if (exploded)
		{
			if (frames == 10)
			{
				frames = 0;

				anim++;
				if (anim == 5)
				{
					draw = false;
				}
			}
		}

		else
		{
			if (frames == 5)
			{
				anim++;
				frames = 0;
			}

			if (shieldFrames == 10)
			{
				if (shieldIn)
				{
					shieldAnim++;
					if (shieldAnim == 5)
					{
						shieldAnim--;
						shieldIn = false;
					}
				}

				else
				{
					shieldAnim--;
					if (shieldAnim == -1)
					{
						shieldAnim++;
						shieldIn = true;
					}
				}

				shieldFrames = 0;
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
			batch.draw(animations[anim & 0x1], x, y, length, length);
		}

		if (drawShield)
		{
			batch.draw(shields[shieldLevel][shieldAnim], x, y, length, length);
		}
	}

	@Override
	public void accept(Deque<InputEvent> events)
	{
		for (InputEvent event : events)
		{
			if (event instanceof TouchDraggedEvent)
			{
				drags.offer((TouchDraggedEvent) event);
			}
		}
	}

	public void hit()
	{
		if (lastHit == 0.0f)
		{
			shieldLevel--;
			if (shieldLevel == -1)
			{
				drawShield = false;
			}

			else if (shieldLevel == -2)
			{
				exploded = true;
				frames = 0;
				anim = 0;
			}

			lastHit = hitTime;
		}
	}
}
