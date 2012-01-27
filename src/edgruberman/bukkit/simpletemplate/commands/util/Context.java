package edgruberman.bukkit.simpletemplate.commands.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import edgruberman.bukkit.messagemanager.MessageLevel;
import edgruberman.bukkit.simpletemplate.Main;

/**
 * Individual command execution request
 */
public class Context {

    Handler handler;
    CommandSender sender;
    Action action;
    List<String> arguments;

    /**
     * Parse a command execution request.
     * 
     * @param handler command handler
     * @param sender command sender
     * @param args passed command arguments
     */
    Context(final Handler handler, final CommandSender sender, final String[] args) {
        this.handler = handler;
        this.sender = sender;
        this.arguments = parseArguments(args);
        this.action = this.parseAction();
        Main.messageManager.log("Command Context for " + this.handler.command.getLabel() + "; Action: " + this.action.name + "; Arguments: " + this.arguments, MessageLevel.FINEST);
    }

    /**
     * Determines if sender is allowed to use the requested command/action.
     * A message will be sent to the sender if they are not allowed.
     * 
     * @return true if user is allowed; false otherwise
     */
    boolean isAllowed() {
        if (!this.sender.hasPermission(this.handler.permission)) {
            Main.messageManager.respond(this.sender, "You are not allowed to use the " + this.handler.command.getLabel() + " command.", MessageLevel.RIGHTS, false);
            return false;
        }

        if (this.action.permission != null) {
            if (!this.sender.hasPermission(this.action.permission)) {
                Main.messageManager.respond(this.sender, "You are not allowed to use the " + this.action.name + " action of the " + this.handler.command.getLabel() + " command.", MessageLevel.RIGHTS, false);
                return false;
            }
        }

        return true;
    }

    /**
     * Identify requested action.
     * 
     * @return the requested action or the default/first action if none applies
     */
    private Action parseAction() {
        // Determine if a specific action directly applies
        for (Action a : this.handler.actions)
            if (a.matches(this)) return a;

        // Return default action (first action registered)
        return this.handler.actions.get(0);
    }

    /**
     * Concatenate arguments to compensate for double quotes indicating single
     * argument, removing any delimiting double quotes.
     *  
     * @return arguments
     * 
     * TODO use \ for escaping double quote characters
     * TODO make this less messy
     */
    private List<String> parseArguments(String[] args) {
        List<String> arguments = new ArrayList<String>();

        String previous = null;
        for (String arg : args) {
            if (previous != null) {
                if (arg.endsWith("\"")) {
                    arguments.add(Context.stripDoubleQuotes(previous + " " + arg));
                    previous = null;
                } else {
                    previous += " " + arg;
                }
                continue;
            }

            if (arg.startsWith("\"") && !arg.endsWith("\"")) {
                previous = arg;
            } else {
                arguments.add(Context.stripDoubleQuotes(arg));
            }
        }
        if (previous != null) arguments.add(Context.stripDoubleQuotes(previous));

        return arguments;
    }

    private static String stripDoubleQuotes(final String s) {
        return Context.stripDelimiters(s, "\"");
    }

    private static String stripDelimiters(final String s, final String delim) {
        if (!s.startsWith(delim) || !s.endsWith(delim)) return s;

        return s.substring(1, s.length() - 1);
    }

}