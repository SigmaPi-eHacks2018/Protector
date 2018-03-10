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
import com.badlogic.gdx.utils.Pools;

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
 * Created by Kyle Fricilone on Mar 02, 2018.
 */
public class MenuView extends AbstractView
{
	private final Texture bg0;
	private final Texture bg1;
	private final Texture logo;

	private final Texture play;
	private final Texture hanger;
	private final Texture about;

	private final BitmapFont font;
	private final GlyphLayout glyph;

	private float width;
	private float height;

	private float xPlay;
	private float yPlay;

	private float xHanger;
	private float yHanger;

	private float xAbout;
	private float yAbout;

	private float y0;
	private float y1;

	public MenuView(Protector protector)
	{
		super(protector);

		bg0 = protector.getAssets().get(Background.BG0.getPath(), Texture.class);
		bg1 = protector.getAssets().get(Background.BG1.getPath(), Texture.class);
		logo = protector.getAssets().get(Statics.LOGO, Texture.class);

		font = protector.getAssets().get(Font.GAME.getPath(), BitmapFont.class);
		glyph = Pools.obtain(GlyphLayout.class);

		play = protector.getAssets().get(Button.PLAY.getPath(), Texture.class);
		hanger = protector.getAssets().get(Button.HANGER.getPath(), Texture.class);
		about = protector.getAssets().get(Button.ABOUT.getPath(), Texture.class);

		height = Statics.HEIGHT * 0.1f;
		width =  Statics.WIDTH * 0.5f;

		float x = (Statics.WIDTH / 2.0f) - (width / 2.0f);
		xPlay = x;
		xHanger = x;
		xAbout = x;

		float y = (Statics.HEIGHT / 2.0f);
		yPlay = y;
		y -= (1.5f * height);
		yHanger = y;
		y -= (1.5f * height);
		yAbout = y;

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

				if ((sx >= xPlay && sx <= (xPlay + width))
						&& (sy >= yPlay && sy <= (yPlay + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new GameView(protector));
				}

				else if ((sx >= xHanger && sx <= (xHanger + width))
						&& (sy >= yHanger && sy <= (yHanger + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new HangerView(protector));
				}

				else if ((sx >= xAbout && sx <= (xAbout + width))
						&& (sy >= yAbout && sy <= (yAbout + height)))
				{
					protector.getViews().pop();
					protector.getViews().push(new AboutView(protector));
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

		batch.draw(play, xPlay, yPlay, width, height);
		batch.draw(hanger, xHanger, yHanger, width, height);
		batch.draw(about, xAbout, yAbout, width, height);

		glyph.setText(font, Statics.format(protector.getProfile().getPoints()));
		font.draw(batch, glyph, (Statics.WIDTH / 2.0f) - (glyph.width / 2), Statics.HEIGHT * 0.70f);
	}

	@Override
	public void dispose()
	{
	}
}
