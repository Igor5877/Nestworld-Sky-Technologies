package com.example.islands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IslandCommands {
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("createisland")
            .requires(source -> source.hasPermission(0))
            .executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                String uuid = player.getUUID().toString();
                HttpUtil.sendCreateIsland(uuid);
                ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Острів створюється..."), false);
                return 1;
            }));
    }
}
