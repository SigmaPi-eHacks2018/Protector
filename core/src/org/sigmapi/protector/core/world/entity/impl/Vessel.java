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

import org.sigmapi.protector.core.Level;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.input.impl.TouchDraggedEvent;
import org.sigmapi.protector.core.skin.LaserSkin;
import org.sigmapi.protector.core.skin.VesselSkin;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.world.World;
import org.sigmapi.protector.core.world.entity.AbstractEntity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class Vessel extends AbstractEntity
{

	private final VesselSkin skin;
	private final Level level;
	private final Texture texture;
	private final Deque<TouchDraggedEvent> drags;

	private final float speed;
	private float time;


	public Vessel(World world, VesselSkin skin, Level level)
	{
		super(world, (Statics.WIDTH / skin.getRatio()), ((Statics.WIDTH / 2) - ((Statics.WIDTH / skin.getRatio()) / 2)), 0.0f, 0.25f, 0.25f);
		this.skin = skin;
		this.level = level;
		this.texture = world.getProtector().getAssets().get(skin.getPath(), Texture.class);
		this.drags = new ArrayDeque<>();

		this.speed = (Statics.MAX_SPEED - level.getSpeed()) / Statics.MAX_SPEED;
	}

	@Override
	public void update(float delta)
	{
		float deltaX = 0.0f;
		float deltaY = 0.0f;

		if (!drags.isEmpty())
		{
			TouchDraggedEvent event;
			while ((event = drags.poll()) != null)
			{
				deltaX += (event.getScreenX() - (x + (length / 2)));
				deltaY += ((Statics.HEIGHT - event.getScreenY()) - (y + (length / 2)));
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
		if (time >= speed)
		{
			float lx = (x + (length / 2));
			float ly = (y + length);
			world.getLasers().add(new Laser(world, LaserSkin.L100, lx, ly, 0f, 600.0f));
			time = 0;
		}
	}

	@Override
	public void render(SpriteBatch batch)
	{
		batch.draw(texture, x, y, length, length);
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
}
