package edgruberman.bukkit.bukkitplugin.versioning;

public abstract class VersionPart implements Comparable<VersionPart> {

    public abstract String toReadable();

    @Override
    public String toString() {
        return this.toReadable();
    }

}