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

import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.skin.LaserSkin;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.world.World;
import org.sigmapi.protector.core.world.entity.AbstractEntity;

import java.util.Deque;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class Laser extends AbstractEntity
{

	private final LaserSkin skin;
	private final Texture texture;

	public Laser(World world, LaserSkin skin, float x, float y, float xVel, float yVel)
	{
		super(world, (Statics.WIDTH / skin.getRatio()), x, y, xVel, yVel);
		this.texture = world.getProtector().getAssets().get(skin.getPath(), Texture.class);
		this.skin = skin;

		this.x -= (length / 2);
	}

	@Override
	public void update(float delta)
	{
		y += (yVel * delta);
		x += (xVel * delta);
		if (y >= Statics.HEIGHT)
		{
			world.getLasersRemove().add(this);
			return;
		}

		if (y <= 0)
		{
			world.getLasersRemove().add(this);
		}

		if (x >= Statics.WIDTH)
		{
			world.getLasersRemove().add(this);
			return;
		}

		if (x <= 0)
		{
			world.getLasersRemove().add(this);
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
	}
}
