package edgruberman.bukkit.simpletemplate.commands;

import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.simpletemplate.Main;

class NameReload extends Action {
    
    NameReload(final Command owner) {
        super("reload", owner);
    }
    
    @Override
    void execute(final Context context) {
        Main main = (Main) this.command.plugin;
        main.loadConfiguration();
        Main.messageManager.respond(context.sender, "Configuration reloaded.", MessageLevel.STATUS, false);
    }
}