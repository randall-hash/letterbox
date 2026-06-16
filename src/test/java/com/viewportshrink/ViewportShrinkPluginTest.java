package com.viewportshrink;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ViewportShrinkPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ViewportShrinkPlugin.class);
		RuneLite.main(args);
	}
}
