package edgruberman.bukkit.simpletemplate.messaging.recipients;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import edgruberman.bukkit.simpletemplate.messaging.Message;
import edgruberman.bukkit.simpletemplate.messaging.Recipients;
import edgruberman.bukkit.simpletemplate.messaging.messages.Confirmation;

/**
 * {@link org.bukkit.permissions.Permissible}s that have the specified permission at message delivery time
 *
 * @author EdGruberman (ed@rjump.com)
 * @version 1.0.0
 */
public class PermissionSubscribers implements Recipients {

    protected String permission;

    public PermissionSubscribers(final String permission) {
        this.permission = permission;
    }

    @Override
    public Confirmation deliver(final Message message) {
        int count = 0;
        for (final Permissible permissible : Bukkit.getPluginManager().getPermissionSubscriptions(this.permission))
            if (permissible instanceof CommandSender && permissible.hasPermission(this.permission)) {
                final CommandSender target = (CommandSender) permissible;
                target.sendMessage(message.format(target).toString());
                count++;
            }

        return new Confirmation(Level.FINER, count
                , "[PUBLISH@{1}({2})] {0}", message, PermissionSubscribers.this.permission, count);
    }

}
