package com.example.Nyxem.OneClick;

import com.example.EthanApiPlugin.Inventory;
import com.example.InteractionApi.InventoryInteraction;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Provides;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Optional;

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


			Optional<Widget> firstItem = StringUtils.isNumeric(firstItemParse) ?
					Inventory.search().withId(firstItemID).first() :
					Inventory.search().matchesWildCardNoCase(firstItemParse).first();

			Optional<Widget> secondItem = StringUtils.isNumeric(secondItemParse) ?
					Inventory.search().withId(secondItemID).first() :
					Inventory.search().matchesWildCardNoCase(secondItemParse).first();

			if(firstItem.isEmpty()) {
				break; // check item exists
			}

			if(secondItem.isEmpty()) {
				break; // check item exists
			}

			log.info("trying menus");
			MenuEntry[] entries = this.client.getMenuEntries();
			for (MenuEntry entry : entries) {
				log.info("menu if 1"); // can probably be better
				if (entry.getTarget().contains(firstItem.get().getName())) {
					log.info("menu1");
					client.createMenuEntry(1)
							.setOption("One Click")
							.setTarget(secondItem.get().getName());
							// .onClick(ItemOnItem()); // future, remove comment & get widgets of use on item1 to item2
				}
				log.info("menu if 2");
				if (entry.getTarget().contains(secondItem.get().getName())) {
					log.info("menu2");
					client.createMenuEntry(1).setOption("One Click").setTarget(firstItem.get().getName());
				}
			}
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
