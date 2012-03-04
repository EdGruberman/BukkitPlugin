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
        this.configurationFile = new ConfigurationFile(this);
    }

    @Override
    public void onEnable() {
        this.loadConfiguration();

        new Single(this);
        new Multiple(this);
    }

    @Override
    public void onDisable() {
        if (this.configurationFile.isSaveQueued()) this.configurationFile.save();
    }

    public void loadConfiguration() {
        @SuppressWarnings("unused")
        final
        FileConfiguration config = this.configurationFile.load();
    }

}