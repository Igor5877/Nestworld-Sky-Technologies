package com.example.islands;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("islandsmod")
public class IslandsMod {
    public static final Logger LOGGER = LogManager.getLogger();

    public IslandsMod() {
        LOGGER.info("Islands Mod Loaded!");
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
public void onRegisterCommands(RegisterCommandsEvent event) {
    event.getDispatcher().register(
        Commands.literal("createisland")
            .executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                HttpUtil.sendCreateIsland(player.getUUID().toString());
                ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Запит на створення острова відправлено!"), false);
                return 1;
            })
    );
}

}
