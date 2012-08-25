package edgruberman.bukkit.simpletemplate;

import edgruberman.bukkit.simpletemplate.commands.Reload;
import edgruberman.bukkit.simpletemplate.messaging.ConfigurationCourier;
import edgruberman.bukkit.simpletemplate.messaging.Courier;
import edgruberman.bukkit.simpletemplate.util.CustomPlugin;

public final class Main extends CustomPlugin {

    public static Courier courier;

    @Override
    public void onLoad() { this.putConfigMinimum(CustomPlugin.CONFIGURATION_FILE, "0.0.0a0"); }

    @Override
    public void onEnable() {
        this.reloadConfig();
        Main.courier = new ConfigurationCourier(this);

        // TODO Instantiate Objects

        this.getCommand("simpletemplate:reload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        Main.courier = null;
    }

}
