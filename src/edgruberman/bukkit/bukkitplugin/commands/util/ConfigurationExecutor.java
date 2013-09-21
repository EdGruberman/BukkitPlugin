package edgruberman.bukkit.bukkitplugin.commands.util;

import edgruberman.bukkit.bukkitplugin.messaging.Courier.ConfigurationCourier;

public abstract class ConfigurationExecutor extends Executor {

    protected ConfigurationCourier courier;

    protected ConfigurationExecutor(final ConfigurationCourier courier) {
        this.courier = courier;
    }

    protected void setSyntax(final Parameter.Factory<?, ?, ?> factory) {
        // concatenate values for limited
        if (factory instanceof LimitedParameter.Factory) {
            final LimitedParameter.Factory<?, ?, ?> limited = (LimitedParameter.Factory<?, ?, ?>) factory;
            final String acceptable = JoinList.create(limited.acceptable, this.courier.getBase(), "argument-syntax-limited-").join();
            factory.setSyntax(acceptable);

        // use name for all others
        } else {
            final String name = this.courier.format("argument-syntax-name", factory.name);
            factory.setSyntax(name);
        }

        // reformat for requirement
        final String key = ( factory.required ? "argument-syntax-required" : "argument-syntax-optional" );
        final String requirement = this.courier.format(key, factory.syntax);
        factory.setSyntax(requirement);
    }

    @Override
    protected <P extends Parameter<T>, T> P addParameter(final Parameter.Factory<P, T, ?> factory) {
        this.setSyntax(factory);
        return super.addParameter(factory);
    }

    @Override
    protected boolean execute(final ExecutionRequest request) throws CancellationContingency {
        try {
            return this.executeImplementation(request);

        } catch (final MissingArgumentContingency m) {
            this.courier.send(request.getSender(), "argument-missing"
                    , new Object[] { m.getParameter().getName(), m.getParameter().getSyntax() });
            return false;

        } catch (final UnknownArgumentContingency u) {
            this.courier.send(request.getSender(), "argument-unknown"
                    , new Object[] { u.getParameter().getName(), u.getParameter().getSyntax(), u.getArgument() });
            return false;
        }
    };

    /** @return true if command processing completed successfully */
    protected abstract boolean executeImplementation(final ExecutionRequest request) throws CancellationContingency;

}
