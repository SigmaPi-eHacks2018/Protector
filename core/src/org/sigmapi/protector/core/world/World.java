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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.sigmapi.protector.core.Protector;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.interfaces.Inputable;
import org.sigmapi.protector.core.interfaces.Renderable;
import org.sigmapi.protector.core.interfaces.Updateable;
import org.sigmapi.protector.core.skin.VesselSkin;
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

	public World(Protector protector)
	{
		this.protector = protector;

		this.lasers = new ArrayList<>();
		this.lasersRemove = new ArrayList<>();
		this.asteroids = new ArrayList<>();
		this.asteroidsRemove = new ArrayList<>();

		this.vessel = new Vessel(this, VesselSkin.V2, protector.getLevel());
	}

	@Override
	public void update(float delta)
	{
		for (Asteroid asteroid : asteroids)
		{
			asteroid.update(delta);
		}
		asteroids.removeAll(asteroidsRemove);
		asteroidsRemove.clear();

		vessel.update(delta);

		for (Laser laser : lasers)
		{
			laser.update(delta);
		}
		lasers.removeAll(lasersRemove);
		lasersRemove.clear();
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
	}

	@Override
	public void accept(Deque<InputEvent> events)
	{
		vessel.accept(events);
	}
}
