package edgruberman.bukkit.simpletemplate.commands;

import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.simpletemplate.commands.util.Handler;

public final class Multiple extends Handler {

    public Multiple(final JavaPlugin plugin) {
        super(plugin, "multiple");
        this.actions.add(new MultipleReload(this));
        this.actions.add(new MultipleStatus(this));
    }

}
