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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

import org.sigmapi.protector.core.input.InputEvent;
import org.sigmapi.protector.core.input.InputManager;
import org.sigmapi.protector.core.view.AbstractView;
import org.sigmapi.protector.core.view.ViewManager;

import java.util.Deque;
import java.util.Iterator;

import lombok.Getter;

/**
 * Created by Kyle Fricilone on Mar 02, 2018.
 */
public class Protector extends ApplicationAdapter
{

	@Getter
	private AssetManager assets;

	private GLProfiler profiler;

	private SpriteBatch batch;

	@Getter
	private ViewManager views;

	private InputManager inputs;

	@Override
	public void create()
	{
		assets = new AssetManager();
		profiler = new GLProfiler(Gdx.graphics);
		batch = new SpriteBatch();
		views = new ViewManager();
		inputs = new InputManager();

		profiler.enable();

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void render()
	{
		Deque<InputEvent> events = inputs.getEvents();
		for (AbstractView view : views.getViews())
		{
			view.accept(events);
		}
		events.clear();

		float delta = Gdx.graphics.getDeltaTime();
		for (AbstractView view : views.getViews())
		{
			view.update(delta);
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for (Iterator<AbstractView> it = views.getViews().descendingIterator(); it.hasNext();)
		{
			it.next().render(batch);
		}
		batch.end();
	}

	@Override
	public void dispose()
	{
		for (AbstractView view : views.getViews())
		{
			view.dispose();
		}

		batch.dispose();
		assets.dispose();
	}

}
