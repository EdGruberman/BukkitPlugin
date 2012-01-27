package edgruberman.bukkit.simpletemplate.commands;

import edgruberman.bukkit.simpletemplate.commands.util.Action;
import edgruberman.bukkit.simpletemplate.commands.util.Context;
import edgruberman.bukkit.simpletemplate.commands.util.Handler;

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