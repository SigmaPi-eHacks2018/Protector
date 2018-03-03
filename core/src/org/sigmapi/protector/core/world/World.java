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

package org.sigmapi.protector.core.world;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.sigmapi.protector.core.Protector;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.font.Font;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.interfaces.Inputable;
import org.sigmapi.protector.core.interfaces.Renderable;
import org.sigmapi.protector.core.interfaces.Updateable;
import org.sigmapi.protector.core.skin.AsteroidSkin;
import org.sigmapi.protector.core.skin.VesselSkin;
import org.sigmapi.protector.core.view.impl.OverView;
import org.sigmapi.protector.core.world.entity.AbstractEntity;
import org.sigmapi.protector.core.world.entity.impl.Asteroid;
import org.sigmapi.protector.core.world.entity.impl.Laser;
import org.sigmapi.protector.core.world.entity.impl.Vessel;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import lombok.Getter;

/**
 * Created by Kyle Fricilone on Mar 02, 2018.
 */
public class World implements Inputable, Updateable, Renderable
{

	@Getter
	private final Protector protector;

	private final Vessel vessel;

	@Getter
	private final List<Laser> lasers;

	@Getter
	private final List<Laser> lasersRemove;

	@Getter
	private final List<Asteroid> asteroids;

	@Getter
	private final List<Asteroid> asteroidsRemove;

	private final BitmapFont font;

	private boolean over = false;
	private int score = 0;
	private float time = 0;

	private float velocity = Statics.HEIGHT / -6.0f;

	public World(Protector protector)
	{
		this.protector = protector;

		this.lasers = new ArrayList<>();
		this.lasersRemove = new ArrayList<>();
		this.asteroids = new ArrayList<>();
		this.asteroidsRemove = new ArrayList<>();

		this.font = protector.getAssets().get(Font.GAME.getPath(), BitmapFont.class);

		this.vessel = new Vessel(this, VesselSkin.V2, protector.getLevel());

		int w = Statics.WIDTH / 5;
		for (int i = 0; i < 5; i++)
		{
			asteroids.add(new Asteroid(this, AsteroidSkin.get(), w * i, Statics.HEIGHT, 0.0f, velocity));
		}
	}

	@Override
	public void update(float delta)
	{
		time += delta;

		for (Asteroid asteroid : asteroids)
		{
			asteroid.update(delta);
		}
		asteroids.removeAll(asteroidsRemove);
		asteroidsRemove.clear();

		if (time >= 5.0f)
		{
			generateBlocks(3);
			time = 0;
		}

		if (!over)
		{
			vessel.update(delta);
		}

		for (Laser laser : lasers)
		{
			laser.update(delta);
		}
		lasers.removeAll(lasersRemove);
		lasersRemove.clear();

		if (!asteroids.isEmpty() && !lasers.isEmpty())
		{
			for (Asteroid asteroid : asteroids)
			{
				if (collides(vessel, asteroid, 2))
				{
					lasers.clear();
					protector.getViews().push(new OverView(protector));
					over = true;
					return;
				}
			}

			for (Laser laser : lasers)
			{
				for (Asteroid asteroid : asteroids)
				{
					if (collides(laser, asteroid, 2))
					{
						lasersRemove.add(laser);

						int strength = asteroid.getStrength();
						if (strength < 250)
						{
							asteroids.remove(asteroid);
							score += strength;
						}

						else
						{
							asteroid.setStrength(strength - 250);
							score += 250;
						}

						break;
					}
				}
			}

			lasers.removeAll(lasersRemove);
			lasersRemove.clear();
		}
	}

	@Override
	public void render(SpriteBatch batch)
	{
		vessel.render(batch);

		for (Laser laser : lasers)
		{
			laser.render(batch);
		}

		for (Asteroid asteroid : asteroids)
		{
			asteroid.render(batch);
		}

		font.draw(batch, String.valueOf(score), ((Statics.WIDTH / 2.0f) - (10 * Font.GAME.getRatio())), (Statics.HEIGHT - 50));
	}

	@Override
	public void accept(Deque<InputEvent> events)
	{
		vessel.accept(events);
	}

	private void generateBlocks(int wave)
	{
		List<Asteroid> toAdd = new ArrayList<>();
		for (int i = 0; i < wave; i++)
		{
			patternLoop:
			for (int j = 0; j < 5; j++)
			{
				float x = Statics.LENGTH * Statics.nextInt(5);
				float y = Statics.HEIGHT + (Statics.LENGTH * Statics.nextInt(3));
				Asteroid newAsteroid = new Asteroid(this, AsteroidSkin.get(), x, y, 0.0f, velocity);

				for (Asteroid asteroid : toAdd)
				{
					if (collides(newAsteroid, asteroid, 5))
					{
						if (asteroid.getStrength() < Statics.MAX_STRENGTH)
						{
							asteroid.setStrength(asteroid.getStrength() + newAsteroid.getStrength());
						}
						continue patternLoop;
					}
				}

				toAdd.add(newAsteroid);
			}
		}

		asteroids.addAll(toAdd);
	}

	private static boolean collides(AbstractEntity a, AbstractEntity b, float gap)
	{
		float l1x = a.getX() + gap;
		float l1y = a.getY() + gap;
		float r1x = (a.getX() + a.getLength()) - gap;
		float r1y = (a.getY() + a.getLength()) - gap;

		float l2x = b.getX() + gap;
		float l2y = b.getY() + gap;
		float r2x = (b.getX() + b.getLength()) - gap;
		float r2y = (b.getY() + b.getLength()) - gap;

		return !(l1x > r2x) && !(l2x > r1x) && !(l1y > r2y) && !(l2y > r1y);
	}
}
