package com.example.Nyxem.OneClick;

import com.example.EthanApiPlugin.Inventory;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Provides;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.apache.commons.lang3.StringUtils;
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

	int timeout = 0;

	@Override
	protected void startUp() throws Exception {
		log.info("message");
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Subscribe
	public void onGameTick(GameTick tiktok) {

		log.info("tick");
		handleAction(config.getCustomItemIDs());

	}

	public void handleAction(String actionParam) {

		String[] Actions = actionParam.split("\n");
		for (String Action : Actions) {
			if (!Action.contains(":")) {
				continue;
			}
			String firstItemParse = Action.split(":")[0];
			String secondItemParse = Action.split(":")[1];

			Optional<Widget> firstItem = StringUtils.isNumeric(firstItemParse) ?
					Inventory.search().withId(Integer.parseInt(firstItemParse)).first() :
					Inventory.search().matchesWildCardNoCase(firstItemParse).first();

			Optional<Widget> secondItem = StringUtils.isNumeric(secondItemParse) ?
					Inventory.search().withId(Integer.parseInt(secondItemParse)).first() :
					Inventory.search().matchesWildCardNoCase(secondItemParse).first();

			if (firstItem.isEmpty()) {
				break;
			}

			if(secondItem.isEmpty()) {
				break;
			}

			log.info("Attempting use");
			MousePackets.queueClickPacket();
			WidgetPackets.queueWidgetOnWidget(firstItem.get(), secondItem.get());
		}
	}

	@Provides
	OneClickConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(OneClickConfig.class);
	}
}
