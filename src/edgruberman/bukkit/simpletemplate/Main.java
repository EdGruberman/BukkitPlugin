package edgruberman.bukkit.simpletemplate;

import java.util.logging.Handler;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.simpletemplate.commands.Multiple;
import edgruberman.bukkit.simpletemplate.commands.Single;
import edgruberman.bukkit.simpletemplate.dependencies.DependencyChecker;

public final class Main extends JavaPlugin {

    private static final String MINIMUM_VERSION_CONFIG = "0.0.0a0";
    private ConfigurationFile configurationFile;

    @Override
    public void onLoad() {
        new DependencyChecker(this);
    }

    @Override
    public void onEnable() {
        this.configurationFile = new ConfigurationFile(this);
        this.configurationFile.setMinVersion(Main.MINIMUM_VERSION_CONFIG);
        this.configurationFile.load();
        this.setLoggingLevel();

        new Message(this);

        this.configure();

        new Single(this);
        new Multiple(this);
    }

    @Override
    public void onDisable() {
        if (this.configurationFile.isSaveQueued()) this.configurationFile.save();
    }

    private void setLoggingLevel() {
        final String name = this.configurationFile.getConfig().getString("logLevel", "INFO");
        Level level;
        try { level = Level.parse(name); } catch (final Exception e) {
            level = Level.INFO;
            this.getLogger().warning("Unrecognized java.util.logging.Level in \"" + this.configurationFile.getFile().getPath() + "\"; logLevel: " + name);
        }

        // Only set the parent handler lower if necessary, otherwise leave it alone for other configurations that have set it.
        for (final Handler h : this.getLogger().getParent().getHandlers())
            if (h.getLevel().intValue() > level.intValue()) h.setLevel(level);

        this.getLogger().setLevel(level);
        this.getLogger().log(Level.CONFIG, "Logging level set to: " + this.getLogger().getLevel());
    }

    public void configure() {
        @SuppressWarnings("unused")
        final FileConfiguration config = this.configurationFile.getConfig();
    }


}
