package net.savagedev.afk.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.savagedev.afk.bungee.messaging.PluginMessageManager;

public class CrossServerAFKBungee extends Plugin {
    private PluginMessageManager messageManager;

    @Override
    public void onEnable() {
        this.messageManager = new PluginMessageManager(this);
    }

    @Override
    public void onDisable() {
        this.messageManager.close();
    }
}
