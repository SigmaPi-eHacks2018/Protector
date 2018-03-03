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

package org.sigmapi.protector.core.input;

import com.badlogic.gdx.InputProcessor;

import org.sigmapi.protector.core.input.impl.KeyDownEvent;
import org.sigmapi.protector.core.input.impl.KeyTypedEvent;
import org.sigmapi.protector.core.input.impl.KeyUpEvent;
import org.sigmapi.protector.core.input.impl.MouseMovedEvent;
import org.sigmapi.protector.core.input.impl.ScrolledEvent;
import org.sigmapi.protector.core.input.impl.TouchDownEvent;
import org.sigmapi.protector.core.input.impl.TouchDraggedEvent;
import org.sigmapi.protector.core.input.impl.TouchUpEvent;

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.Getter;

/**
 * Created by Kyle Fricilone on Mar 02, 2018.
 */
public class InputManager implements InputProcessor
{

	@Getter
	private final Deque<InputEvent> events = new ArrayDeque<>();

	@Override
	public boolean keyDown(int keycode)
	{
		return events.offer(new KeyDownEvent(keycode));
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return events.offer(new KeyUpEvent(keycode));
	}

	@Override
	public boolean keyTyped(char character)
	{
		return events.offer(new KeyTypedEvent(character));
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return events.offer(new TouchDownEvent(screenX, screenY, pointer, button));
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return events.offer(new TouchUpEvent(screenX, screenY, pointer, button));
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return events.offer(new TouchDraggedEvent(screenX, screenY, pointer));
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return events.offer(new MouseMovedEvent(screenX, screenY));
	}

	@Override
	public boolean scrolled(int amount)
	{
		return events.offer(new ScrolledEvent(amount));
	}
}
