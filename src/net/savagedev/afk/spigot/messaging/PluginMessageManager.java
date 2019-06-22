package net.savagedev.afk.spigot.messaging;

import com.earth2me.essentials.I18n;
import net.savagedev.afk.commons.DataStreamUtils;
import net.savagedev.afk.commons.ProtocolConstraints;
import net.savagedev.afk.spigot.CrossServerAFKSpigot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import java.io.DataInput;
import java.io.IOException;
import java.util.UUID;

public class PluginMessageManager implements PluginMessageListener {
    private final CrossServerAFKSpigot plugin;

    public PluginMessageManager(CrossServerAFKSpigot plugin) {
        this.plugin = plugin;
        this.init();
    }

    private void init() {
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, ProtocolConstraints.CHANNEL, this);
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, ProtocolConstraints.CHANNEL);
    }

    public void close() {
        this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(this.plugin, ProtocolConstraints.CHANNEL);
        this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(this.plugin, ProtocolConstraints.CHANNEL);
    }

    @Override
    public void onPluginMessageReceived(@Nonnull String channel, @Nonnull Player user, @Nonnull byte[] message) {
        if (!channel.equals(ProtocolConstraints.CHANNEL)) {
            return;
        }

        DataInput input = DataStreamUtils.newDataInput(message);

        try {
            String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase(ProtocolConstraints.AFK)) {
                String uuid = input.readUTF();

                if (this.plugin.getServer().getPlayer(UUID.fromString(uuid)) != null) {
                    return;
                }

                String displayName = input.readUTF();
                boolean value = Boolean.valueOf(input.readUTF());

                String afkMessage = value ? I18n.tl("userIsAway", displayName) : I18n.tl("userIsNotAway", displayName);
                this.plugin.getEssentials().broadcastMessage(afkMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Player user, String... message) {
        user.sendPluginMessage(this.plugin, ProtocolConstraints.CHANNEL, DataStreamUtils.toByteArray(message));
    }
}
