package com.letterbox;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(LetterboxConfig.GROUP)
public interface LetterboxConfig extends Config
{
	String GROUP = "letterbox";

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
