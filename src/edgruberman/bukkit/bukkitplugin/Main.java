package edgruberman.bukkit.bukkitplugin;

import edgruberman.bukkit.bukkitplugin.commands.Reload;
import edgruberman.bukkit.bukkitplugin.messaging.Courier.ConfigurationCourier;
import edgruberman.bukkit.bukkitplugin.util.DefaultPlugin;

public final class Main extends DefaultPlugin {

    @Override
    public void onLoad() {
        this.putConfigMinimum("0.0.0a0");
        this.putConfigMinimum("language.yml", "0.0.0a0");
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
