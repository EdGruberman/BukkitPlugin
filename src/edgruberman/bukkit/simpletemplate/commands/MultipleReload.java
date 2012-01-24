package edgruberman.bukkit.simpletemplate.commands;

import edgruberman.bukkit.util.commands.Action;
import edgruberman.bukkit.util.commands.Context;
import edgruberman.bukkit.util.commands.Handler;

final class MultipleReload extends Action {

    MultipleReload(final Handler handler) {
        super(handler, "reload");
    }

    @Override
    public boolean perform(final Context context) {
        // TODO add actions here
        return true;
    }

}