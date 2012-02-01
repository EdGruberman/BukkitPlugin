package edgruberman.bukkit.simpletemplate;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.messagemanager.MessageManager;
import edgruberman.bukkit.simpletemplate.commands.Multiple;
import edgruberman.bukkit.simpletemplate.commands.Single;

public final class Main extends JavaPlugin {

    public static MessageManager messageManager;

    private ConfigurationFile configurationFile;

    @Override
    public void onLoad() {
        Main.messageManager = new MessageManager(this);
        Main.messageManager.log("Version " + this.getDescription().getVersion());
        this.configurationFile = new ConfigurationFile(this);
    }

    @Override
    public void onEnable() {
        this.loadConfiguration();

        new Single(this);
        new Multiple(this);

        Main.messageManager.log("Plugin Enabled");
    }

    @Override
    public void onDisable() {
        if (this.configurationFile.isSaveQueued()) this.configurationFile.save();
        Main.messageManager.log("Plugin Disabled");
    }

    public void loadConfiguration() {
        @SuppressWarnings("unused")
        FileConfiguration config = this.configurationFile.load();
    }

}