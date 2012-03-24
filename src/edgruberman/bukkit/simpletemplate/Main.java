package edgruberman.bukkit.simpletemplate;

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
        this.configurationFile.setLoggingLevel();

        new Message(this);

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


}
