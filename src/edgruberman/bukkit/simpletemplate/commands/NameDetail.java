package edgruberman.bukkit.simpletemplate.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.simpletemplate.Main;

class NameDetail extends Action {
    
    NameDetail(final Command owner) {
        super("detail", owner);
    }
    
    @Override
    void execute(final Context context) {
        World world = this.parseWorld(context);
        if (world == null) {
            Main.messageManager.respond(context.sender, "Unable to determine world.", MessageLevel.SEVERE, false);
            return;
        }
        
        Main.messageManager.respond(context.sender, "Detail action executed!", MessageLevel.STATUS, false);
    }
    
    private World parseWorld(final Context context) {
        if (context.sender instanceof Player && context.arguments.size() < 2)
            return ((Player) context.sender).getWorld();
        
        if (context.arguments.size() < 2) return null;
        
        return Bukkit.getServer().getWorld(context.arguments.get(1));
    }
}