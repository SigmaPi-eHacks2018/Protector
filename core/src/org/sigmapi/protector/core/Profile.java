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

package org.sigmapi.protector.core;

import com.badlogic.gdx.Preferences;

import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.skin.LaserSkin;
import org.sigmapi.protector.core.skin.VesselSkin;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class Profile
{

	@Getter
	@Setter
	private int power;

	@Getter
	@Setter
	private int speed;

	@Getter
	@Setter
	private int shield;

	@Getter
	@Setter
	private int vessel;

	@Getter
	@Setter
	private int laser;

	@Getter
	@Setter
	private long points;

	@Getter
	private Map<String, Boolean> vessels;

	@Getter
	private Map<String, Boolean> lasers;

	public Profile()
	{
		this.power = Statics.BASE_POWER;
		this.speed = Statics.BASE_SPEED;
		this.shield = 0;
		this.vessel = VesselSkin.DEFAULT.ordinal();
		this.laser = LaserSkin.DEFAULT.ordinal();
		this.points = 0;
		this.vessels = new HashMap<>();
		this.lasers = new HashMap<>();

		for (VesselSkin skin : VesselSkin.values())
		{
			vessels.put(skin.getClass().getSimpleName() + "." + skin.name(), false);
		}

		for (LaserSkin skin : LaserSkin.values())
		{
			lasers.put(skin.getClass().getSimpleName() + "." + skin.name(), false);
		}

		vessels.put(VesselSkin.class.getSimpleName() + "." + VesselSkin.DEFAULT.name(), true);
		lasers.put(LaserSkin.class.getSimpleName() + "." + LaserSkin.DEFAULT.name(), true);
	}

	public void save(Preferences prefs)
	{
		prefs.putInteger("power", power);
		prefs.putInteger("speed", speed);
		prefs.putInteger("shield", shield);
		prefs.putInteger("vessel", vessel);
		prefs.putInteger("laser", laser);
		prefs.putLong("points", points);
		prefs.put(vessels);
		prefs.put(lasers);

		prefs.flush();
	}

	public void load(Preferences prefs)
	{
		power = prefs.getInteger("power", Statics.BASE_POWER);
		speed = prefs.getInteger("speed", Statics.BASE_SPEED);
		shield = prefs.getInteger("shield", 0);
		vessel = prefs.getInteger("vessel", VesselSkin.DEFAULT.ordinal());
		laser = prefs.getInteger("laser", LaserSkin.DEFAULT.ordinal());
		points = prefs.getLong("points", 0);

		for (String key : prefs.get().keySet())
		{
			if (key.startsWith(VesselSkin.class.getSimpleName()))
			{
				vessels.put(key, prefs.getBoolean(key));
			}

			else if (key.startsWith(LaserSkin.class.getSimpleName()))
			{
				lasers.put(key, prefs.getBoolean(key));
			}
		}
	}

}
