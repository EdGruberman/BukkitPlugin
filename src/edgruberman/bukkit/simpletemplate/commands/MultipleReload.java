package edgruberman.bukkit.simpletemplate.commands;

import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.simpletemplate.commands.util.Action;
import edgruberman.bukkit.simpletemplate.commands.util.Context;
import edgruberman.bukkit.simpletemplate.commands.util.Handler;

final class MultipleReload extends Action {

    MultipleReload(final Handler handler) {
        super(handler, "reload");
    }

    @Override
    public boolean perform(final Context context) {
        context.handler.command.getPlugin().onDisable();
        context.handler.command.getPlugin().onEnable();
        context.respond("Configuration reloaded", MessageLevel.STATUS);
        return true;
    }

}
