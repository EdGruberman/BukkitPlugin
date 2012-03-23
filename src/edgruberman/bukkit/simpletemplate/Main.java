package edgruberman.bukkit.simpletemplate;

import java.util.logging.Handler;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.messagemanager.MessageManager;
import edgruberman.bukkit.simpletemplate.commands.Multiple;
import edgruberman.bukkit.simpletemplate.commands.Single;

public final class Main extends JavaPlugin {

    public static MessageManager messageManager;

    private ConfigurationFile configurationFile;

    @Override
    public void onEnable() {
        this.configurationFile = new ConfigurationFile(this);
        this.configurationFile.load();
        this.setLoggingLevel();

        Main.messageManager = new MessageManager(this);

        this.configure();

        new Single(this);
        new Multiple(this);
    }

    @Override
    public void onDisable() {
        if (this.configurationFile.isSaveQueued()) this.configurationFile.save();
    }

    public void configure() {
        @SuppressWarnings("unused")
        final FileConfiguration config = this.configurationFile.getConfig();
    }

    private void setLoggingLevel() {
        final String name = this.configurationFile.getConfig().getString("logLevel", "INFO");
        Level level = MessageLevel.parse(name);
        if (level == null) level = Level.INFO;

        // Only set the parent handler lower if necessary, otherwise leave it alone for other configurations that have set it.
        for (final Handler h : this.getLogger().getParent().getHandlers())
            if (h.getLevel().intValue() > level.intValue()) h.setLevel(level);

        this.getLogger().setLevel(level);
        this.getLogger().log(Level.CONFIG, "Logging level set to: " + this.getLogger().getLevel());
    }

}
