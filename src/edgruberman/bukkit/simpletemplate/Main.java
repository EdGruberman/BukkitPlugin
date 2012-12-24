package edgruberman.bukkit.simpletemplate;

import edgruberman.bukkit.simpletemplate.commands.Reload;
import edgruberman.bukkit.simpletemplate.messaging.ConfigurationCourier;
import edgruberman.bukkit.simpletemplate.util.CustomPlugin;

public final class Main extends CustomPlugin {

    public static ConfigurationCourier courier;

    @Override
    public void onLoad() { this.putConfigMinimum("0.0.0a0"); }

    @Override
    public void onEnable() {
        this.reloadConfig();
        Main.courier = ConfigurationCourier.Factory.create(this).setBase(this.loadConfig("messages.yml")).setColorCode("color-code").build();

        // TODO things

        this.getCommand("simpletemplate:reload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        Main.courier = null;
    }

}
