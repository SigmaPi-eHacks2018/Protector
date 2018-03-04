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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.sigmapi.protector.core.Protector;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.background.Background;
import org.sigmapi.protector.core.buttons.Button;
import org.sigmapi.protector.core.font.Font;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.input.impl.TouchDownEvent;
import org.sigmapi.protector.core.view.AbstractView;

import java.util.Deque;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class UpgradesView extends AbstractView
{
	private final Texture bg0;
	private final Texture bg1;
	private final Texture logo;

	private final Texture buy;
	private final Texture home;

	private final BitmapFont font;
	private final BitmapFont score;

	private float width;
	private float height;

	private float xPower;
	private float yPower;

	private float xSpeed;
	private float ySpeed;

	private float xHome;
	private float yHome;

	private float y0;
	private float y1;

	public UpgradesView(Protector protector)
	{
		super(protector);

		bg0 = protector.getAssets().get(Background.BG0.getPath(), Texture.class);
		bg1 = protector.getAssets().get(Background.BG1.getPath(), Texture.class);
		logo = protector.getAssets().get(Statics.LOGO, Texture.class);
		score = protector.getAssets().get(Font.ABOUT.getPath(), BitmapFont.class);
		font = protector.getAssets().get(Font.GAME.getPath(), BitmapFont.class);

		buy = protector.getAssets().get(Button.BUY.getPath(), Texture.class);
		home = protector.getAssets().get(Button.HOME.getPath(), Texture.class);

		height = Statics.HEIGHT * 0.08f;
		width =  Statics.WIDTH * 0.5f;

		float x = (Statics.WIDTH / 2.0f) - (width / 4.0f);
		xPower = x - (width / 2);
		xSpeed = x - (width / 2);
		xHome = x - (width / 4.0f);

		float y = (Statics.HEIGHT / 2.0f);
		yPower = y;
		y -= (1.5f * height);
		ySpeed = y;
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

				//batch.draw(vessels[i], xVessels[i], yVessels[i], height, height);
				//batch.draw(buy, xPower + width, yPower, width / 2.0f, height);


					if ((sx >= (xPower + width)  && sx <= ((xPower + width) + (width / 2.0f)))
							&& (sy >= yPower && sy <= (yPower + height)))
					{
						int level = protector.getProfile().getPower() + 1;
						long points = (long) Math.pow(2, level - 5);
						if (protector.getProfile().getPoints() >= points && level <= Statics.MAX_POWER)
						{
							protector.getProfile().setPower(protector.getProfile().getPower() + 1);
							protector.getProfile().setPoints(protector.getProfile().getPoints() - points);
						}
					}

				if ((sx >= (xSpeed + width)  && sx <= ((xSpeed + width) + (width / 2.0f)))
						&& (sy >= ySpeed && sy <= (ySpeed + height)))
				{
					int level = protector.getProfile().getSpeed() + 1;
					long points = (long) Math.pow(2, level - 5);
					if (protector.getProfile().getPoints() >= points && level <= Statics.MAX_SPEED)
					{
						protector.getProfile().setSpeed(level);
						protector.getProfile().setPoints(protector.getProfile().getPoints() - points);
					}
				}

				if ((sx >= xHome && sx <= (xHome + width))
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

		int level = protector.getProfile().getPower() + 1;
		if (level <= Statics.MAX_POWER)
		{
			long points = (long) Math.pow(2, level - 5);
			batch.draw(buy, xPower + width, yPower, width / 2.0f, height);
			score.draw(batch, "Power: " + String.valueOf(level), xPower, yPower + (height * 0.6f));
			score.draw(batch, String.valueOf(points), xPower + (width / 2.0f), yPower + (height * 0.6f));
		}

		level = protector.getProfile().getSpeed() + 1;
		if (level <= Statics.MAX_SPEED)
		{
			long points = (long) Math.pow(2, level - 5);
			batch.draw(buy, xSpeed + width, ySpeed, width / 2.0f, height);
			score.draw(batch, "Speed: " + String.valueOf(level), xSpeed, ySpeed + (height * 0.6f));
			score.draw(batch, String.valueOf(points), xSpeed + (width / 2.0f), ySpeed + (height * 0.6f));
		}

		batch.draw(home, xHome, yHome, width, height);

		font.draw(batch, String.valueOf(protector.getProfile().getPoints()), Statics.WIDTH / 2.0f, Statics.HEIGHT * 0.70f);
	}

	@Override
	public void dispose()
	{
	}
}
