package edgruberman.bukkit.simpletemplate.messaging;

import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.permissions.Permissible;

/**
 * collection of one or more message targets
 * @author EdGruberman (ed@rjump.com)
 * @version 3.0.0
 */
public abstract class Recipient {

    public static TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();

    /** retrieve "TimeZone" MetadataValue for CommandSender */
    public static TimeZone getTimeZone(final CommandSender sender) {
        if (!(sender instanceof Metadatable))
            return Recipient.DEFAULT_TIME_ZONE;

        final Metadatable meta = (Metadatable) sender;
        final List<MetadataValue> values = meta.getMetadata("TimeZone");
        if (values.size() == 0)
            return Recipient.DEFAULT_TIME_ZONE;

        return (TimeZone) values.get(0).value();
    }



    protected Recipient() {};

    /** format and send message to each target */
    public abstract Confirmation deliver(Message message);



    /**
     * single {@link org.bukkit.command.CommandSender CommandSender}
     * @version 4.0.0
     */
    public static class Sender extends Recipient {

        protected CommandSender target;

        public Sender(final CommandSender target) {
            this.target = target;
        }

        @Override
        public Confirmation deliver(final Message message) {
            final String formatted = message.format(this.target).toString();
            this.target.sendMessage(formatted);
            return new Confirmation(this.level(), 1, "[SEND>{1}] {0}", message, this.target.getName());
        }

        protected Level level() {
            return Level.FINER;
        }

    }



    /**
     * the server's {@link org.bukkit.command.ConsoleCommandSender ConsoleCommandSender}
     * @version 4.0.0
     */
    public static class ConsoleSender extends Sender {

        public ConsoleSender(final ConsoleCommandSender sender) {
           super(sender);
        }

        /** easier filtering of console messages that will already appear in console when logged */
        @Override
        protected Level level() {
            return Level.FINEST;
        }

    }



    /**
     * {@link org.bukkit.command.CommandSender CommandSender}s that have the specified permission at message delivery time
     * @version 3.0.0
     */
    public static class PermissionSubscribers extends Recipient {

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

            return new Confirmation(Level.FINER, count, "[PUBLISH-{1}({2})] {0}", message, PermissionSubscribers.this.permission, count);
        }

    }



    /**
     * broadcast to all players in server
     * @version 3.0.0
     */
    public static class ServerPlayers extends PermissionSubscribers {

        public ServerPlayers() {
            super(Server.BROADCAST_CHANNEL_USERS);
        }

        @Override
        public Confirmation deliver(final Message message) {
            final Confirmation confirmation = super.deliver(message);
            return new Confirmation(Level.FINEST, confirmation.getReceived(), "[BROADCAST({1})] {0}", message, confirmation.getReceived());
        }

    }



    /**
     * players in a world at message delivery time
     * @version 3.0.0
     */
    public static class WorldPlayers extends Recipient {

        protected final World world;

        public WorldPlayers(final World world) {
            this.world = world;
        }

        public World getWorld() {
            return this.world;
        }

        @Override
        public Confirmation deliver(final Message message) {
            final List<Player> players = this.world.getPlayers();
            for (final Player player : players)
                if (player.hasPermission(Server.BROADCAST_CHANNEL_USERS))
                    player.sendMessage(message.format(player).toString());

            final int count = players.size();
            return new Confirmation(Level.FINE, count, "[WORLD%{1}({2})] {0}", message, this.world.getName(), count);
        }

    }

}