package io.github.Igor5877.nestworld;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer; // ТИ ЗАБУВ ІМПОРТУВАТИ ЦЕ!!!
import org.slf4j.Logger;

@Plugin(id = "nestworld", name = "nestworld", version = BuildConstants.VERSION)
public class Nestworld {

    private final ProxyServer server;

    @Inject
    public Nestworld(ProxyServer server) {
        this.server = server;
        new HTTPApiListener(server).start();
        System.out.println("Proxy Server Manager plugin loaded!");
    }
}
