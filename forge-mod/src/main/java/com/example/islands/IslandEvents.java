package com.example.islands;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IslandEvents {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        String uuid = event.getEntity().getUUID().toString();
        HttpUtil.sendUpdateActivity(uuid);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        String uuid = event.getEntity().getUUID().toString();
        HttpUtil.sendUpdateActivity(uuid);
    }
}
