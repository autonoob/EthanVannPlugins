package com.example.Nyxem.OneClick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("oneclick")
public interface OneClickConfig extends Config
{
	@ConfigSection(
		position = 0,
		name = "Custom Items Config",
		description = ""
	)
	String customItemsConfig = "Custom Items Config";

	@ConfigItem(
			position = 0,
			keyName = "customItemToggle",
			name = "Custom Item Toggle",
			description = "Turn Custom Item One Clicks on or off",
			section = customItemsConfig
	)
	default boolean isCustomItemOneClickEnabled() { return true; }

	@ConfigItem(
		position = 1,
		keyName = "customItemIDs",
		name = "Custom Item IDs",
		description = "Enter item ids. Format id:id. When you hover over any of two IDs, it will change to use on the other id. Toggle must be on.",
		section = customItemsConfig
	)
	default String getCustomItemIDs() { return "0:0"; }
}
