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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.font.Font;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.skin.AsteroidSkin;
import org.sigmapi.protector.core.world.World;
import org.sigmapi.protector.core.world.entity.AbstractEntity;

import java.util.Deque;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class Asteroid extends AbstractEntity
{

	private final AsteroidSkin skin;
	private final Texture texture;
	private final BitmapFont font;

	@Getter
	@Setter
	private int strength;
	private float color;

	public Asteroid(World world, AsteroidSkin skin, float x, float y, float xVel, float yVel)
	{
		super(world, (Statics.WIDTH / skin.getRatio()), x, y, xVel, yVel);
		this.skin = skin;
		this.texture = world.getProtector().getAssets().get(skin.getPath(), Texture.class);
		this.font = world.getProtector().getAssets().get(Font.ASTEROID.getPath(), BitmapFont.class);

		this.strength = Statics.nextStrength(Statics.MAX_STRENGTH);
	}

	@Override
	public void update(float delta)
	{
		y += (yVel * delta);
		if ((y + length) <= 0)
		{
			world.getAsteroidsRemove().add(this);
			return;
		}

		float cb = strength / (float) Statics.MAX_STRENGTH;
		if (cb >= 0.5f)
		{
			color = Color.toFloatBits(1.0f, ((1.0f - cb) / 0.5f), 0.0f, 1.0f);
		}

		else
		{
			color = Color.toFloatBits(cb * 2.0f, 1.0f, 0.0f, 1.0f);
		}
	}

	@Override
	public void render(SpriteBatch batch)
	{
		float prevColor = batch.getPackedColor();

		batch.setColor(color);
		batch.draw(texture, x, y, length, length);
		batch.setColor(prevColor);
		font.draw(batch, String.valueOf(strength), (x + (length / 2)) - (Font.ASTEROID.getRatio() * 1.5f), (y + (length / 2)) + 25);
	}

	@Override
	public void accept(Deque<InputEvent> events)
	{
	}
}
