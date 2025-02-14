package com.eseabsolute.swapservervelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

@Plugin(
        id = "swapservervelocity",
        name = "SwapServerVelocity",
        version = BuildConstants.VERSION,
        authors = {"ESEAbsolute"}
)
public class SwapServerVelocity {
    private final ProxyServer proxy;
    private final ChannelIdentifier BUNGEE_CHANNEL =
            MinecraftChannelIdentifier.from("bungeecord:main");

    @Inject
    public SwapServerVelocity(ProxyServer proxy) {
        this.proxy = proxy;
        proxy.getChannelRegistrar().register(BUNGEE_CHANNEL);
    }

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(BUNGEE_CHANNEL)) return;
        if (!(event.getSource() instanceof Player player)) return;

        byte[] data = event.getData();

        if (data[0] == 0x03) {
            String serverName = new String(data, 1, data.length - 1);
            proxy.getServer(serverName).ifPresent(server ->
                    player.createConnectionRequest(server).fireAndForget()
            );
        }
    }
}