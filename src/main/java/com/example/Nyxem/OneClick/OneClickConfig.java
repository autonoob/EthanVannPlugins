package com.example.Nyxem.OneClick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("custom")
public interface OneClickConfig extends Config
{
	@ConfigItem(
		keyName = "customItem",
		name = "Custom Item Config",
		description = "Enter item ids. Format id:id"
	)
	default String getCustomItemIDs()
	{
		return "0:0";
	}
}
