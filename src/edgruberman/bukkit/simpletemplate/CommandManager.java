package edgruberman.bukkit.simpletemplate;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edgruberman.bukkit.messagemanager.MessageLevel;

public class CommandManager implements CommandExecutor {
    private Main plugin;

    protected CommandManager (Main plugin) {
        this.plugin = plugin;
        
        this.plugin.getCommand("command").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        Main.messageManager.log(MessageLevel.FINE
                , ((sender instanceof Player) ? ((Player) sender).getName() : "[CONSOLE]")
                + " issued command: " + label + " " + join(split)
        );
        
        if (!sender.isOp()) {
            Main.messageManager.respond(sender, MessageLevel.RIGHTS, "You must be a server operator to use this command.");
            return false;
        }

        // TODO Add command processing logic here.
        
        return false;
    }
    
    /**
     * Concatenate all string elements of an array together with a space.
     * 
     * @param s String array
     * @return Concatenated elements
     */
    private static String join(String[] s) {
        return join(Arrays.asList(s), " ");
    }
    
    /**
     * Combine all the elements of a list together with a delimiter between each.
     * 
     * @param list List of elements to join.
     * @param delim Delimiter to place between each element.
     * @return String combined with all elements and delimiters.
     */
    private static String join(List<String> list, String delim) {
        if (list == null || list.isEmpty()) return "";
     
        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s + delim);
        sb.delete(sb.length() - delim.length(), sb.length());
        
        return sb.toString();
    }
}