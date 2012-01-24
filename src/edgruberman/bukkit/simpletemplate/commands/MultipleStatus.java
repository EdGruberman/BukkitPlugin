package edgruberman.bukkit.simpletemplate.commands;

import edgruberman.bukkit.util.commands.Action;
import edgruberman.bukkit.util.commands.Context;
import edgruberman.bukkit.util.commands.Handler;

final class MultipleStatus extends Action {

    MultipleStatus(final Handler handler) {
        super(handler, "status");
    }

    @Override
    public boolean perform(final Context context) {
        // TODO add actions here
        return true;
    }

}