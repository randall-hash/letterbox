package com.viewportshrink;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(ViewportShrinkConfig.GROUP)
public interface ViewportShrinkConfig extends Config
{
	String GROUP = "viewport-shrink";

	@Range(
		min = 200,
		max = 5000
	)
	@ConfigItem(
		keyName = "targetWidth",
		name = "Target width",
		description = "Width in pixels of the shrunken game play area. Clamped to the window size.",
		position = 1
	)
	default int targetWidth()
	{
		return 1200;
	}

	@Range(
		min = 200,
		max = 5000
	)
	@ConfigItem(
		keyName = "targetHeight",
		name = "Target height",
		description = "Height in pixels of the shrunken game play area. Clamped to the window size.",
		position = 2
	)
	default int targetHeight()
	{
		return 800;
	}
}
