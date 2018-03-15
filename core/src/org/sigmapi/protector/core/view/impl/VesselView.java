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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pools;

import org.sigmapi.protector.core.Protector;
import org.sigmapi.protector.core.Statics;
import org.sigmapi.protector.core.background.Background;
import org.sigmapi.protector.core.buttons.Button;
import org.sigmapi.protector.core.font.Font;
import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.input.impl.TouchDownEvent;
import org.sigmapi.protector.core.skin.VesselSkin;
import org.sigmapi.protector.core.view.AbstractView;

import java.util.Deque;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class VesselView extends AbstractView
{
	private final Texture bg0;
	private final Texture bg1;
	private final Texture logo;

	private final TextureRegion[] vessels;
	private final Texture buy;
	private final Texture back;

	private final BitmapFont font;
	private final BitmapFont score;
	private final GlyphLayout glyph;

	private float width;
	private float height;

	private float[] xVessels;
	private float[] yVessels;

	private float xHome;
	private float yHome;

	private float y0;
	private float y1;

	public VesselView(Protector protector)
	{
		super(protector);

		bg0 = protector.getAssets().get(Background.BG0.getPath(), Texture.class);
		bg1 = protector.getAssets().get(Background.BG1.getPath(), Texture.class);
		logo = protector.getAssets().get(Statics.LOGO, Texture.class);
		score = protector.getAssets().get(Font.ABOUT.getPath(), BitmapFont.class);
		font = protector.getAssets().get(Font.GAME.getPath(), BitmapFont.class);
		glyph = Pools.obtain(GlyphLayout.class);

		vessels = new TextureRegion[5];
		xVessels = new float[5];
		yVessels = new float[5];
		for (VesselSkin skin : VesselSkin.values())
		{
			Texture t = protector.getAssets().get(skin.getPath(), Texture.class);
			vessels[skin.ordinal()] = new TextureRegion(t, 0, 0, 45, 45);
		}
		buy = protector.getAssets().get(Button.BUY.getPath(), Texture.class);
		back = protector.getAssets().get(Button.BACK.getPath(), Texture.class);

		height = Statics.HEIGHT * 0.08f;
		width =  Statics.WIDTH * 0.5f;

		float x = (Statics.WIDTH / 2.0f) - (width / 4.0f);
		for (int i = 0; i < xVessels.length; i++)
		{
			xVessels[i] = x - (width / 2.0f);
		}
		xHome = x - (width / 4.0f);

		float y = (Statics.HEIGHT / 2.0f) + (Statics.HEIGHT / 8.0f);
		for (int i = 0; i < yVessels.length; i++)
		{
			yVessels[i] = y;
			y -= (1.5f * height);
		}
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
				//batch.draw(buy, xVessels[i] + width, yVessels[i], width / 2.0f, height);

				for (int i = 0; i < xVessels.length; i++)
				{
					VesselSkin skin = VesselSkin.values()[i];
					if ((sx >= xVessels[i]  && sx <= (xVessels[i] + height))
							&& (sy >= yVessels[i] && sy <= (yVessels[i] + height)))
					{
						String id = skin.getClass().getSimpleName() + "." + skin.name();
						if (protector.getProfile().getVessels().get(id))
						{
							protector.getProfile().setVessel(i);
						}
					}

					if ((sx >= (xVessels[i] + width)  && sx <= ((xVessels[i] + width) + (width / 2.0f)))
							&& (sy >= yVessels[i] && sy <= (yVessels[i] + height)))
					{
						int points = (int) Math.pow(2, 10 + i);
						if (protector.getProfile().getPoints() >= points)
						{
							protector.getProfile().getVessels().put(skin.getClass().getSimpleName() + "." + skin.name(), true);
							protector.getProfile().setVessel(i);
							protector.getProfile().setPoints(protector.getProfile().getPoints() - points);
						}
					}
				}

				if ((sx >= xHome && sx <= (xHome + width))
						&& (sy >= yHome && sy <= (yHome + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new HangerView(protector));
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

		for (int i = 0; i < vessels.length; i++)
		{
			VesselSkin skin = VesselSkin.values()[i];
			batch.draw(vessels[i], xVessels[i], yVessels[i], height, height);
			if (!protector.getProfile().getVessels().get(skin.getClass().getSimpleName() + "." + skin.name()))
			{
				int points = (int) Math.pow(2, 10 + i);
				batch.draw(buy, xVessels[i] + width, yVessels[i], width / 2.0f, height);

				glyph.setText(score, Statics.format(points));
				score.draw(batch, glyph, xVessels[i] + (width / 2.0f), yVessels[i] + (height * 0.6f));
			}
		}

		batch.draw(back, xHome, yHome, width, height);

		glyph.setText(font, Statics.format(protector.getProfile().getPoints()));
		font.draw(batch, glyph, (Statics.WIDTH / 2.0f) - (glyph.width / 2), Statics.HEIGHT * 0.70f);
	}

	@Override
	public void dispose()
	{
	}
}
