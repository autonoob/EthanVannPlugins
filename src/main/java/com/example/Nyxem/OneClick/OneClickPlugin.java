package com.example.Nyxem.OneClick;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Provides;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
		name = "Nyxem One Click",
		description = "Nyxem's One Clicks",
		tags = "nyxem"
)
public class OneClickPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;

	@Inject
	private OneClickConfig config;

	private boolean menu1created = false;
	private boolean menu2created = false;

	@Override
	protected void startUp() throws Exception {
		log.info("Nyxem Plugin Start");
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("Nyxem Plugin stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			log.info("logged in");
		}
	}

	@Subscribe
	public void onGameTick(GameTick tiktok) {

		if (config.isCustomItemOneClickEnabled()) {
			handleItemAction(config.getCustomItemIDs());
		}
	}

	private void handleItemAction(String actionParam) {

		String[] Actions = actionParam.split("\n");
		for (String Action : Actions) {
			if (!Action.contains(":")) {
				continue;
			}
			String firstItemParse = Action.split(":")[0];
			String secondItemParse = Action.split(":")[1];

			int firstItemID = Integer.parseInt(firstItemParse);
			int secondItemID = Integer.parseInt(secondItemParse); // thought I needed these for something

			log.info("trying menus");
			Widget item1 = EthanApiPlugin.getItem(firstItemID, WidgetInfo.INVENTORY);
			Widget item2 = EthanApiPlugin.getItem(secondItemID, WidgetInfo.INVENTORY);

			if (client.getLocalPlayer().getAnimation() != -1 || EthanApiPlugin.isMoving())
				return;

			if (client.getMouseCurrentButton() != 0 )
				ItemOnItem(item1, item2);

		}
	}

	private void ItemOnItem(Widget widget1, Widget widget2)
	{
		MousePackets.queueClickPacket();
		WidgetPackets.queueWidgetOnWidget(widget1, widget2);
	}
	@Provides
	OneClickConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(OneClickConfig.class);
	}
}
