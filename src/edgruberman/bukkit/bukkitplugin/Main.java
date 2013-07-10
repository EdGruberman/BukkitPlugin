package edgruberman.bukkit.bukkitplugin;

import edgruberman.bukkit.bukkitplugin.commands.Reload;
import edgruberman.bukkit.bukkitplugin.messaging.Courier.ConfigurationCourier;
import edgruberman.bukkit.bukkitplugin.util.CustomPlugin;

public final class Main extends CustomPlugin {

    public static ConfigurationCourier courier;

    @Override
    public void onLoad() {
        this.putConfigMinimum("0.0.0a0");
        this.putConfigMinimum("language.yml", "0.0.0a0");
    }

    @Override
    public void onEnable() {
        this.reloadConfig();
        Main.courier = ConfigurationCourier.create(this).setBase(this.loadConfig("language.yml")).setFormatCode("format-code").build();

        // TODO things

        this.getCommand("simpletemplate:reload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        Main.courier = null;
    }

}
