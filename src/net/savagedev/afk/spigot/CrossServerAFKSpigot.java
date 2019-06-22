package net.savagedev.afk.spigot;

import com.earth2me.essentials.Essentials;
import net.savagedev.afk.spigot.listeners.AFKStatusChangeE;
import net.savagedev.afk.spigot.messaging.PluginMessageManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class CrossServerAFKSpigot extends JavaPlugin {
    private PluginMessageManager messageManager;
    private PluginManager pluginManager;
    private Essentials essentials;

    @Override
    public void onEnable() {
        this.hookEssentials();
        this.loadListeners();
        this.loadUtils();
    }

    @Override
    public void onDisable() {
        this.messageManager.close();
    }

    private void loadUtils() {
        this.messageManager = new PluginMessageManager(this);
    }

    private void loadListeners() {
        this.pluginManager.registerEvents(new AFKStatusChangeE(this), this);
    }

    private void hookEssentials() {
        this.pluginManager = this.getServer().getPluginManager();

        if (this.pluginManager.getPlugin("Essentials") == null) {
            this.getServer().getLogger().log(Level.WARNING, "[Cross-ServerAFK] Essentials not found. Disabling plugin.");
            this.pluginManager.disablePlugin(this);
            return;
        }

        this.essentials = Essentials.getPlugin(Essentials.class);
    }

    public PluginMessageManager getMessageManager() {
        return this.messageManager;
    }

    public Essentials getEssentials() {
        return this.essentials;
    }
}
