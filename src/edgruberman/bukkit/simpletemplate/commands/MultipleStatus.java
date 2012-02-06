package edgruberman.bukkit.simpletemplate.commands;

import edgruberman.bukkit.simpletemplate.commands.util.Action;
import edgruberman.bukkit.simpletemplate.commands.util.Context;
import edgruberman.bukkit.simpletemplate.commands.util.Handler;

final class MultipleStatus extends Action {

    MultipleStatus(final Handler handler) {
        super(handler, "status");
    }

    @Override
    public boolean perform(final Context context) {
        // TODO add actions here
        return true;
    }

    @Override
    public boolean matches(Context context) {
        return super.matchesBreadcrumb(context);
    }

}