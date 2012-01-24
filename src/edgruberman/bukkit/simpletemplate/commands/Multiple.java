package edgruberman.bukkit.simpletemplate.commands;

import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.util.commands.Handler;

public final class Multiple extends Handler {

    public Multiple(final JavaPlugin plugin) {
        super(plugin, "multiple");
        this.actions.add(new MultipleReload(this));
        this.actions.add(new MultipleStatus(this));
    }

}
