package edgruberman.bukkit.simpletemplate.commands;

import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.simpletemplate.commands.util.Handler;

public final class Multiple extends Handler {

    public Multiple(final JavaPlugin plugin) {
        super(plugin, "multiple");
        new MultipleReload(this);
        new MultipleStatus(this);
    }

}
