package net.savagedev.afk.bungee.messaging;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.savagedev.afk.bungee.CrossServerAFKBungee;
import net.savagedev.afk.commons.DataStreamUtils;
import net.savagedev.afk.commons.ProtocolConstraints;

import java.io.DataInput;
import java.io.IOException;

public class PluginMessageManager implements Listener {
    private final CrossServerAFKBungee plugin;

    public PluginMessageManager(CrossServerAFKBungee plugin) {
        this.plugin = plugin;
        this.init();
    }

    private void init() {
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
        this.plugin.getProxy().registerChannel(ProtocolConstraints.CHANNEL);
    }

    public void close() {
        this.plugin.getProxy().unregisterChannel(ProtocolConstraints.CHANNEL);
    }

    @EventHandler
    public void onPluginMessageReceivedE(final PluginMessageEvent e) {
        if (!e.getTag().equals(ProtocolConstraints.CHANNEL)) {
            return;
        }

        DataInput input = DataStreamUtils.newDataInput(e.getData());

        try {
            String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase(ProtocolConstraints.AFK)) {
                String uuid = input.readUTF();
                String displayName = input.readUTF();
                String value = input.readUTF();

                for (ServerInfo server : this.plugin.getProxy().getServers().values()) {
                    if (server.getPlayers().isEmpty()) {
                        continue;
                    }

                    this.send(server, ProtocolConstraints.AFK, uuid, displayName, value);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void send(ServerInfo server, String... message) {
        server.sendData(ProtocolConstraints.CHANNEL, DataStreamUtils.toByteArray(message));
    }
}
