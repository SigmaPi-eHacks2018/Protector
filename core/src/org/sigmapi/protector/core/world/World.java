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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pools;

import org.sigmapi.protector.core.Protector;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.font.Font;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.interfaces.Inputable;
import org.sigmapi.protector.core.interfaces.Renderable;
import org.sigmapi.protector.core.interfaces.Updateable;
import org.sigmapi.protector.core.Profile;
import org.sigmapi.protector.core.skin.AsteroidSkin;
import org.sigmapi.protector.core.skin.UfoSkin;
import org.sigmapi.protector.core.skin.VesselSkin;
import org.sigmapi.protector.core.view.impl.OverView;
import org.sigmapi.protector.core.world.entity.AbstractEntity;
import org.sigmapi.protector.core.world.entity.impl.Asteroid;
import org.sigmapi.protector.core.world.entity.impl.Laser;
import org.sigmapi.protector.core.world.entity.impl.Ufo;
import org.sigmapi.protector.core.world.entity.impl.Vessel;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import lombok.Getter;

import static java.lang.Math.floor;

/**
 * Created by Kyle Fricilone on Mar 02, 2018.
 */
public class World implements Inputable, Updateable, Renderable
{

	@Getter
	private final Protector protector;

	@Getter
	private final Vessel vessel;

	@Getter
	private final List<Laser> lasers;

	@Getter
	private final List<Laser> lasersRemove;

	@Getter
	private final List<Asteroid> asteroids;

	@Getter
	private final List<Asteroid> asteroidsRemove;

	@Getter
	private final List<Ufo> ufos;

	@Getter
	private final List<Ufo> ufosRemove;

	private final BitmapFont font;
	private final GlyphLayout glyph;

	private int score = 0;
	private float time = 0;
	private int layers = 0;
	private int wave = 1;

	private float speed = 0.85f;
	private float velocity = Statics.HEIGHT / -6.0f;

	private int power = 100;

	public World(Protector protector)
	{
		this.protector = protector;

		this.lasers = new ArrayList<>();
		this.lasersRemove = new ArrayList<>();
		this.asteroids = new ArrayList<>();
		this.asteroidsRemove = new ArrayList<>();
		this.ufos = new ArrayList<>();
		this.ufosRemove = new ArrayList<>();

		this.font = protector.getAssets().get(Font.GAME.getPath(), BitmapFont.class);
		this.glyph = Pools.obtain(GlyphLayout.class);

		this.vessel = new Vessel(this, VesselSkin.values()[protector.getProfile().getVessel()], protector.getProfile());

		this.power *= protector.getProfile().getPower();

		int w = Statics.WIDTH / 5;
		int max = Statics.getMax(wave);
		for (int i = 0; i < 5; i++)
		{
			asteroids.add(new Asteroid(this, AsteroidSkin.get(), w * i, Statics.HEIGHT, 0.0f, velocity, Statics.nextStrength(max), max));
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

		for (Ufo ufo : ufos)
		{
			ufo.update(delta);
		}
		ufos.removeAll(ufosRemove);
		ufosRemove.clear();

		vessel.update(delta);

		for (Laser laser : lasers)
		{
			laser.update(delta);
		}
		lasers.removeAll(lasersRemove);
		lasersRemove.clear();

		if (!vessel.isExploded() && !asteroids.isEmpty() && !lasers.isEmpty())
		{
			for (Asteroid asteroid : asteroids)
			{
				if (!asteroid.isExploded() && collides(vessel, asteroid, 15))
				{
					vessel.hit();
					if (vessel.isExploded())
					{
						Profile profile = protector.getProfile();
						profile.setPoints(profile.getPoints() + (int) floor(score / 10_000f));
						protector.getViews().push(new OverView(protector));
						return;
					}
				}
			}

			laserLoop:
			for (Laser laser : lasers)
			{
				if (laser.getYVel() < 0)
				{
					if (collides(vessel, laser, 2))
					{
						vessel.hit();
						if (vessel.isExploded())
						{
							Profile profile = protector.getProfile();
							profile.setPoints(profile.getPoints() + (int) floor(score / 10_000f));
							protector.getViews().push(new OverView(protector));
							return;
						}
					}
				}

				else
				{
					for (Ufo ufo : ufos)
					{
						if (!ufo.isExploded() && collides(laser, ufo, 2))
						{
							lasersRemove.add(laser);
							ufo.setExploded(true);
							score += 10_000;
							continue laserLoop;
						}
					}

					for (Asteroid asteroid : asteroids)
					{
						if (!asteroid.isExploded() && collides(laser, asteroid, 2))
						{
							lasersRemove.add(laser);

							int strength = asteroid.getStrength();
							if (strength < power)
							{
								asteroid.setExploded(true);
								score += strength;
							}

							else
							{
								asteroid.setStrength(strength - power);
								score += power;
							}

							continue laserLoop;
						}
					}
				}
			}

			lasers.removeAll(lasersRemove);
			lasersRemove.clear();
		}

		if (time >= 5.0f)
		{
			layers++;
			wave = (layers / 5) + 1;
			generateBlocks();
			generateUfos();
			time = 0;

			if (layers % 5 == 0)
			{
				velocity += 0.01f;
				speed -= 0.05f;
			}
		}
	}

	@Override
	public void render(SpriteBatch batch)
	{
		for (Asteroid asteroid : asteroids)
		{
			asteroid.render(batch);
		}

		for (Ufo ufo : ufos)
		{
			ufo.render(batch);
		}

		for (Laser laser : lasers)
		{
			laser.render(batch);
		}

		if (vessel.isDraw())
		{
			vessel.render(batch);
		}

		glyph.setText(font, "Score: " + Statics.format(score));
		font.draw(batch, glyph, ((Statics.WIDTH / 2.0f) - (glyph.width / 2)), (Statics.HEIGHT - (5 * Font.GAME.getRatio())));

		glyph.setText(font, "Wave: " + String.valueOf(wave));
		font.draw(batch, glyph, ((Statics.WIDTH / 2.0f) - (glyph.width / 2)), (Statics.HEIGHT - (20 * Font.GAME.getRatio())));
	}

	@Override
	public void accept(Deque<InputEvent> events)
	{
		vessel.accept(events);
	}

	private void generateBlocks()
	{
		int max = Statics.getMax(wave);

		int rows = 3;
		int iterations = 1;

		if (wave > 20)
		{
			rows++;
		}

		if (wave > 5)
		{
			iterations++;
		}

		if (wave > 10)
		{
			iterations++;
		}

		if (wave > 20)
		{
			iterations++;
		}

		if (wave > 30)
		{
			iterations++;
		}

		List<Asteroid> toAdd = new ArrayList<>();
		for (int i = 0; i < iterations; i++)
		{
			patternLoop:
			for (int j = 0; j < 5; j++)
			{
				float x = Statics.LENGTH * Statics.nextInt(5);
				float y = Statics.HEIGHT + (Statics.LENGTH * Statics.nextInt(rows));
				Asteroid newAsteroid = new Asteroid(this, AsteroidSkin.get(), x, y, 0.0f, velocity, Statics.nextStrength(max), max);

				for (Asteroid asteroid : toAdd)
				{
					if (collides(newAsteroid, asteroid, 5))
					{
						if (asteroid.getStrength() < max)
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

	private void generateUfos()
	{
		int shots = (wave >= 5) ? 2 : 3;
		float x = Statics.LENGTH * Statics.nextInt(5);
		float y = Statics.HEIGHT + (Statics.LENGTH * Statics.nextInt(3));
		Ufo ufo = new Ufo(this, UfoSkin.get(), x, y, 0.0f, velocity, shots, speed);

		ufos.add(ufo);

		if (wave >= 5)
		{
			Ufo ufo2;
			do
			{
				x = Statics.LENGTH * Statics.nextInt(5);
				y = Statics.HEIGHT + (Statics.LENGTH * Statics.nextInt(3));
				ufo2 = new Ufo(this, UfoSkin.get(), x, y, 0.0f, velocity, shots, speed);
			} while (collides(ufo, ufo2, 2));

			ufos.add(ufo2);
		}
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
