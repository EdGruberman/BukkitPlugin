package edgruberman.bukkit.bukkitplugin.commands.util;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import edgruberman.bukkit.bukkitplugin.messaging.Courier.ConfigurationCourier;

public class OnlinePlayerParameter extends Parameter<Player> {

    private final Server server;

    public OnlinePlayerParameter(final OnlinePlayerParameter.Factory factory) {
        super(factory);
        this.server = factory.server;
    }

    @Override
    public Player parse(final ExecutionRequest request) throws ArgumentParseException {
        String argument = request.getArgument(this.index);

        if (argument == null) {
            if (!(request.getSender() instanceof Player)) throw new ArgumentMissingException(request, this);
            argument = request.getSender().getName();
        }

        final Player result = this.server.getPlayer(argument);
        if (result == null) throw new ArgumentUnknownException(request, this);

        return result;
    }





    public static class Factory extends Parameter.Factory<OnlinePlayerParameter, Player, OnlinePlayerParameter.Factory> {

        public static OnlinePlayerParameter.Factory create(final String name, final ConfigurationCourier courier, final Server server) {
            final OnlinePlayerParameter.Factory result = new OnlinePlayerParameter.Factory(name, courier);
            result.setServer(server);
            return result;
        }

        private Server server;

        public Factory(final String name, final ConfigurationCourier courier) {
            super(name, courier);
        }

        public OnlinePlayerParameter.Factory setServer(final Server server) {
            this.server = server;
            return this;
        }

        @Override
        public OnlinePlayerParameter build() {
            return new OnlinePlayerParameter(this);
        }

    }

}
