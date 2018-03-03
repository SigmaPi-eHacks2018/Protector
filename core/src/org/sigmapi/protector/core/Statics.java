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

	public static final float MAX_SPEED = 60.0f;
	public static final int MAX_POWER = 200;


	public static final String FONTS = "fonts/";

	public static final String LOAD_TEXURES = "textures/game/";
	public static final String LOAD_BGS = LOAD_TEXURES + "backgrounds/";

	public static final String MENU_TEXURES = "textures/game/";
	public static final String MENU_BGS = MENU_TEXURES + "backgrounds/";

	public static final String GAME_TEXURES = "textures/game/";
	public static final String GAME_BGS = GAME_TEXURES + "backgrounds/";
	public static final String GAME_ASTEROIDS = GAME_TEXURES + "asteroids/";
	public static final String GAME_LASERS = GAME_TEXURES + "lasers/";
	public static final String GAME_VESSELS = GAME_TEXURES + "vessels/";


	private static final Random RAND = new Random();

	public static int nextInt(int bound)
	{
		return RAND.nextInt(bound);
	}


	private Statics()
	{
	}
}

