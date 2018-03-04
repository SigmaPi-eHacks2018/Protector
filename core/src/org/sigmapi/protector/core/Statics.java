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

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Created by Kyle Fricilone on Mar 03, 2018.
 */
public class Statics
{

	public static final int HEIGHT = Gdx.graphics.getHeight();
	public static final int WIDTH = Gdx.graphics.getWidth();

	public static final int BASE_POWER = 10;
	public static final int BASE_SPEED = 10;
	public static final float MAX_SPEED = 60.0f;
	public static final int MAX_POWER = 600;

	public static final float BG_VEL = -100f;

	public static final int MAX_STRENGTH = 10_000;

	public static final float LENGTH = WIDTH / 5.0f;


	public static final String FONTS = "fonts/";


	public static final String LOGO = "textures/0.png";

	public static final String BGS = "textures/backgrounds/";

	public static final String LOAD_TEXURES = "textures/load/";
	public static final String BUTTONS = "textures/buttons/";
	public static final String PAUSE_TEXTURES = "textures/pause/";
	public static final String GAME_TEXURES = "textures/game/";
	public static final String GAME_EXPLOSION = GAME_TEXURES + "0.png";
	public static final String GAME_SHIELD = GAME_TEXURES + "shields/0.png";
	public static final String GAME_ASTEROIDS = GAME_TEXURES + "asteroids/";
	public static final String GAME_LASERS = GAME_TEXURES + "lasers/";
	public static final String GAME_UFOS = GAME_TEXURES + "ufos/";
	public static final String GAME_VESSELS = GAME_TEXURES + "vessels/";


	private static final Random RAND = new Random();

	public static int nextInt(int bound)
	{
		return RAND.nextInt(bound);
	}

	private static final int[] DIST = { 1, 2, 3, 4, 5, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 };
	//public static final String s = "1234566777788888888888888888888999999999999999999999999999999999999999";

	public static int getMax(int wave)
	{
		return 10_000 + (int) (1_500 * (Math.pow(wave, 1.15)));
	}

	public static int nextStrength(int max)
	{
		double base = (DIST[RAND.nextInt(DIST.length)] / 10d);
		double value = (base + (Math.random() / 10d));
		return (int) (max * value);
	}


	private Statics()
	{
	}
}

