package edgruberman.bukkit.simpletemplate.commands;

import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.util.commands.Action;
import edgruberman.bukkit.util.commands.Context;

public final class Single extends Action {

    public Single(final JavaPlugin plugin) {
        super(plugin, "single");
    }

    @Override
    public boolean perform(final Context context) {
        // TODO add actions here
        return true;
    }

}