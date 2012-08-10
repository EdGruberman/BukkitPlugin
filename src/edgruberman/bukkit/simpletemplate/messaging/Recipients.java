package edgruberman.bukkit.simpletemplate.messaging;

import edgruberman.bukkit.simpletemplate.messaging.messages.Confirmation;

public interface Recipients {

    public abstract Confirmation send(Message message);

}
