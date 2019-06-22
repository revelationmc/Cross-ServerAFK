package net.savagedev.afk.spigot.listeners;

import com.earth2me.essentials.IUser;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.savagedev.afk.commons.ProtocolConstraints;
import net.savagedev.afk.spigot.CrossServerAFKSpigot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AFKStatusChangeE implements Listener {
    private final CrossServerAFKSpigot plugin;

    public AFKStatusChangeE(CrossServerAFKSpigot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAFKStatusChangeE(final AfkStatusChangeEvent e) {
        IUser user = e.getAffected();
        Player base = user.getBase();

        if (user.isHidden()) {
            return;
        }

        this.plugin.getMessageManager().send(base, ProtocolConstraints.AFK, base.getUniqueId().toString(), base.getDisplayName(), String.valueOf(e.getValue()));
    }
}
