package edgruberman.bukkit.simpletemplate.commands.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.simpletemplate.Main;

/**
 * Command execution manager
 */
public class Handler implements CommandExecutor  {

    protected PluginCommand command;
    protected String permission;

    protected List<Action> actions = new ArrayList<Action>();

    /**
     * Create a command executor with a default plugin.command permission.
     * 
     * @param plugin command owner
     * @param label command name
     */
    protected Handler(final JavaPlugin plugin, final String label) {
        this.setExecutorOf(plugin, label);
        this.permission = Main.PERMISSION_PREFIX + "." + this.command.getLabel();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Main.messageManager.log(
                ((sender instanceof Player) ? ((Player) sender).getName() : "[CONSOLE]")
                    + " issued command: " + this.command.getLabel() + " " + Arrays.toString(args)
                , MessageLevel.FINE
        );

        Context context = new Context(this, sender, args);
        if (!context.isAllowed()) return true;

        return context.action.perform(context);
    }

    /**
     * Registers executor for a command.
     * 
     * @param label command label to register
     */
    private void setExecutorOf(final JavaPlugin plugin, final String label) {
        this.command = plugin.getCommand(label);
        if (this.command == null) {
            Main.messageManager.log("Unable to register " + label + " command.", MessageLevel.WARNING);
            return;
        }

        this.command.setExecutor(this);
    }

}