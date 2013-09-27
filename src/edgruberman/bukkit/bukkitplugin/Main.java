package edgruberman.bukkit.bukkitplugin;

import edgruberman.bukkit.bukkitplugin.commands.Reload;
import edgruberman.bukkit.bukkitplugin.messaging.Courier.ConfigurationCourier;
import edgruberman.bukkit.bukkitplugin.util.StandardPlugin;

public final class Main extends StandardPlugin {

    @Override
    public void onLoad() {
        this.putDefinition(StandardPlugin.CONFIGURATION_FILE, Configuration.getDefinition(StandardPlugin.CONFIGURATION_FILE));
        this.putDefinition("language.yml", Configuration.getDefinition("language.yml"));
    }

    @Override
    public void onEnable() {
        this.reloadConfig();
        final ConfigurationCourier courier = ConfigurationCourier.Factory.create(this).setBase(this.loadConfig("language.yml")).setFormatCode("format-code").build();

        // TODO do things

        this.getCommand("simpletemplate:reload").setExecutor(new Reload(courier, this));
    }

    @Override
    public void onDisable() {
        // TODO undo things
    }

}
