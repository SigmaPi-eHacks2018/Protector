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

package org.sigmapi.protector.core.view.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.sigmapi.protector.core.Protector;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.background.Background;
import org.sigmapi.protector.core.buttons.Button;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.input.impl.TouchDownEvent;
import org.sigmapi.protector.core.view.AbstractView;

import java.util.Deque;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class HangerView extends AbstractView
{
	private final Texture bg0;
	private final Texture bg1;
	private final Texture logo;

	private final Texture vessel;
	private final Texture laser;
	private final Texture upgrade;
	private final Texture home;

	private float width;
	private float height;

	private float xVessel;
	private float yVessel;

	private float xLaser;
	private float yLaser;

	private float xUpgrade;
	private float yUpgrade;

	private float xHome;
	private float yHome;

	private float y0;
	private float y1;

	public HangerView(Protector protector)
	{
		super(protector);

		bg0 = protector.getAssets().get(Background.BG0.getPath(), Texture.class);
		bg1 = protector.getAssets().get(Background.BG1.getPath(), Texture.class);
		logo = protector.getAssets().get(Statics.LOGO, Texture.class);

		vessel = protector.getAssets().get(Button.VESSELS.getPath(), Texture.class);
		laser = protector.getAssets().get(Button.LASERS.getPath(), Texture.class);
		upgrade = protector.getAssets().get(Button.UPGRADES.getPath(), Texture.class);
		home = protector.getAssets().get(Button.HOME.getPath(), Texture.class);

		height = Statics.HEIGHT * 0.08f;
		width =  Statics.WIDTH * 0.5f;

		float x = (Statics.WIDTH / 2.0f) - (width / 2.0f);
		xVessel = x;
		xLaser = x;
		xUpgrade = x;
		xHome = x;

		float y = (Statics.HEIGHT / 2.0f);
		yVessel = y;
		y -= (1.5f * height);
		yLaser = y;
		y -= (1.5f * height);
		yUpgrade = y;
		y -= (1.5f * height);
		yHome = y;

		y0 = 0;
		y1 = Statics.HEIGHT;
	}

	@Override
	public void accept(Deque<InputEvent> events)
	{
		for (InputEvent event : events)
		{
			if (event instanceof TouchDownEvent)
			{
				TouchDownEvent td = (TouchDownEvent) event;
				int sx = td.getScreenX();
				int sy = Statics.HEIGHT - td.getScreenY();

				if ((sx >= xVessel && sx <= (xVessel + width))
						&& (sy >= yVessel && sy <= (yVessel + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new VesselView(protector));
				}

				else if ((sx >= xLaser && sx <= (xLaser + width))
						&& (sy >= yLaser && sy <= (yLaser + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new LaserView(protector));
				}

				else if ((sx >= xUpgrade && sx <= (xUpgrade + width))
						&& (sy >= yUpgrade && sy <= (yUpgrade + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new UpgradesView(protector));
				}

				else if ((sx >= xHome && sx <= (xHome + width))
						&& (sy >= yHome && sy <= (yHome + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new MenuView(protector));
				}
			}
		}
	}

	@Override
	public void update(float delta)
	{
		y0 += Statics.BG_VEL * delta;
		y1 += Statics.BG_VEL * delta;

		if (y0 <= -Statics.HEIGHT)
		{
			y0 = Statics.HEIGHT;
		}

		if (y1 <= -Statics.HEIGHT)
		{
			y1 = Statics.HEIGHT;
		}
	}

	@Override
	public void render(SpriteBatch batch)
	{
		batch.draw(bg0, 0, y0, Statics.WIDTH, Statics.HEIGHT + 1);
		batch.draw(bg1, 0, y1, Statics.WIDTH, Statics.HEIGHT + 1);
		batch.draw(logo, 0, (Statics.HEIGHT * 0.15f), Statics.WIDTH, Statics.HEIGHT);

		batch.draw(vessel, xVessel, yVessel, width, height);
		batch.draw(laser, xLaser, yLaser, width, height);
		batch.draw(upgrade, xUpgrade, yUpgrade, width, height);
		batch.draw(home, xHome, yHome, width, height);
	}

	@Override
	public void dispose()
	{
	}
}
