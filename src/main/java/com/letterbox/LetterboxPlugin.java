package com.letterbox;

import com.google.inject.Provides;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Letterbox",
	description = "Renders the game at a smaller size centered in black space (Resizable - Classic)"
)
public class LetterboxPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private LetterboxConfig config;

	// The game engine component (canvas.getParent(), obfuscated class "client"). Resizing THIS is
	// what makes the game reflow its UI; resizing the canvas itself only crops the rendered frame.
	private Component gameComponent;
	// The ClientPanel that holds the engine. We swap its layout for absolute positioning while active.
	private Container holder;
	// The holder layout we replaced, restored on shutdown.
	private LayoutManager originalLayout;
	// Re-centers the engine whenever ClientUI re-bounds the holder (sidebar toggle, window resize).
	private ComponentListener holderListener;
	// Fires one more applyBounds after a window resize settles, so the client refreshes its mouse
	// mapping at the final stable position (a mid-resize apply recomputes against a transient one).
	private Timer resizeDebounce;

	@Override
	protected void startUp()
	{
		SwingUtilities.invokeLater(() ->
		{
			final Canvas canvas = client.getCanvas();
			final Component engine = canvas == null ? null : canvas.getParent();
			if (engine == null || !(engine.getParent() instanceof Container))
			{
				log.debug("Letterbox: game engine/holder not available; nothing to do");
				return;
			}

			gameComponent = engine;
			holder = (Container) engine.getParent();
			originalLayout = holder.getLayout();

			// null layout = absolute positioning; we own the engine's bounds from here.
			holder.setLayout(null);

			resizeDebounce = new Timer(200, e -> applyBounds());
			resizeDebounce.setRepeats(false);

			holderListener = new ComponentAdapter()
			{
				@Override
				public void componentResized(ComponentEvent e)
				{
					applyBounds();
					// Re-apply once the resize stops, when on-screen geometry is stable.
					resizeDebounce.restart();
				}
			};
			holder.addComponentListener(holderListener);

			applyBounds();
		});
	}

	@Override
	protected void shutDown()
	{
		SwingUtilities.invokeLater(() ->
		{
			if (resizeDebounce != null)
			{
				resizeDebounce.stop();
			}
			if (holder != null)
			{
				if (holderListener != null)
				{
					holder.removeComponentListener(holderListener);
				}
				// Restore the original layout so the engine fills the window again.
				holder.setLayout(originalLayout);
				holder.revalidate();
				holder.repaint();
			}
			gameComponent = null;
			holder = null;
			originalLayout = null;
			holderListener = null;
			resizeDebounce = null;
		});
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!LetterboxConfig.GROUP.equals(event.getGroup()))
		{
			return;
		}
		SwingUtilities.invokeLater(this::applyBounds);
	}

	/**
	 * Sizes the game engine component to the configured target (clamped to the holder) and centers
	 * it, leaving the ClientPanel's black background showing in the margins.
	 */
	private void applyBounds()
	{
		if (holder == null || gameComponent == null || gameComponent.getParent() != holder)
		{
			return;
		}

		final int holderW = holder.getWidth();
		final int holderH = holder.getHeight();
		if (holderW <= 0 || holderH <= 0)
		{
			return;
		}

		final int w = Math.min(config.targetWidth(), holderW);
		final int h = Math.min(config.targetHeight(), holderH);
		final int x = (holderW - w) / 2;
		final int y = (holderH - h) / 2;

		// The injected client refreshes its mouse->canvas mapping only on a genuine RESIZE, not on a
		// move. When the sidebar toggles or the window resizes, the engine shifts position while
		// keeping its size; without a resize the client keeps the stale mapping and clicks land off
		// by the shift. If the size would be unchanged, drop the height by 1px so the reposition
		// still registers as a real resize and the mapping refreshes at the new position.
		int appliedH = h;
		if (gameComponent.getWidth() == w && gameComponent.getHeight() == h && h > 1)
		{
			appliedH = h - 1;
		}
		gameComponent.setBounds(x, y, w, appliedH);
		holder.revalidate();
		holder.repaint();
	}

	@Provides
	LetterboxConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LetterboxConfig.class);
	}
}
