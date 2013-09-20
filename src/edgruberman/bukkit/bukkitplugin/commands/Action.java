package edgruberman.bukkit.bukkitplugin.commands;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import edgruberman.bukkit.bukkitplugin.commands.util.ArgumentContingency;
import edgruberman.bukkit.bukkitplugin.commands.util.ConfigurationExecutor;
import edgruberman.bukkit.bukkitplugin.commands.util.ExecutionRequest;
import edgruberman.bukkit.bukkitplugin.commands.util.LowerCaseParameter;
import edgruberman.bukkit.bukkitplugin.commands.util.OnlinePlayerParameter;
import edgruberman.bukkit.bukkitplugin.messaging.Courier.ConfigurationCourier;

public class Action extends ConfigurationExecutor {

    private final LowerCaseParameter type;
    private final OnlinePlayerParameter player;

    public Action(final ConfigurationCourier courier, final Server server) {
        super(courier);

        this.type = this.addRequired(LowerCaseParameter.Factory.create("type"));
        this.player = this.addOptional(OnlinePlayerParameter.Factory.create("player", server));
    }

    // usage: /<command> type [player]
    @Override
    protected boolean executeImplementation(final ExecutionRequest request) throws ArgumentContingency {
        final String type = request.parse(this.type);
        final Player player = request.parse(this.player);
        this.courier.send(request.getSender(), "action", type, player);
        return true;
    }

}
