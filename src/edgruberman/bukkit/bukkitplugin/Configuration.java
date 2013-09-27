package edgruberman.bukkit.bukkitplugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edgruberman.bukkit.bukkitplugin.configuration.ConfigurationDefinition;
import edgruberman.bukkit.bukkitplugin.configuration.ConfigurationInstruction;
import edgruberman.bukkit.bukkitplugin.configuration.HeaderInstruction;
import edgruberman.bukkit.bukkitplugin.configuration.PutKeyInstruction;
import edgruberman.bukkit.bukkitplugin.configuration.RemoveKeyInstruction;
import edgruberman.bukkit.bukkitplugin.util.StandardPlugin;
import edgruberman.bukkit.bukkitplugin.versioning.StandardVersion;

/** organizes configuration file definitions into a separate class container */
public final class Configuration {

    private static Map<String, ConfigurationDefinition> definitions = new HashMap<String, ConfigurationDefinition>();

    static {
        Configuration.definitions.put(StandardPlugin.CONFIGURATION_FILE, Configuration.defineConfig());
        Configuration.definitions.put("language.yml", Configuration.defineLanguage());
    }

    public static ConfigurationDefinition getDefinition(final String name) {
        return Configuration.definitions.get(name);
    }

    private static ConfigurationDefinition defineConfig() {
        final ConfigurationDefinition result = new ConfigurationDefinition();

        final List<ConfigurationInstruction> v0_0_0a0 = result.createInstructions(StandardVersion.parse("0.0.0a0"));
        v0_0_0a0.add(HeaderInstruction.create(
                "\n"
                + "---- comments ----\n"
                + "value: important notes related to value\n"
                + "\n"
                + "---- values ----"
        ));

        v0_0_0a0.add(PutKeyInstruction.create(StandardPlugin.DEFAULT_LOG_LEVEL.getName(), StandardPlugin.KEY_LOG_LEVEL));

        return result;
    }

    private static ConfigurationDefinition defineLanguage() {
        final ConfigurationDefinition result = new ConfigurationDefinition();

        final List<ConfigurationInstruction> v0_0_0a0 = result.createInstructions(StandardVersion.parse("0.0.0a0"));
        v0_0_0a0.add(HeaderInstruction.create(
                "\n"
                + "---- arguments ----\n"
                + "reload: 0 = generated, 1 = plugin name\n"
                + "sender-rejected: 0 = generated, 1 = acceptable, 2 = label\n"
                + "sender-rejected-valid-item: 0 = name\n"
                + "argument-missing: 0 = generated, 1 = name 2 = syntax\n"
                + "argument-unknown: 0 = generated, 1 = name, 2 = syntax, 3 = value\n"
                + "argument-syntax-name: 0 = name\n"
                + "argument-syntax-known-item: 0 = value\n"
                + "argument-syntax-required: 0 = argument\n"
                + "argument-syntax-optional: 0 = argument\n"
                + "\n"
                + "---- patterns ----"
        ));

        v0_0_0a0.add(PutKeyInstruction.create("§", "format-code"));

        v0_0_0a0.add(PutKeyInstruction.create("§f-> §2Reloaded §7{1} plugin", "reload"));

        v0_0_0a0.add(PutKeyInstruction.create("sender-rejected", "§f-> §cOnly {1} §7can use the §b/{2} §7command"));
        v0_0_0a0.add(PutKeyInstruction.create("sender-rejected-valid-item", "§c{0}s"));
        v0_0_0a0.add(PutKeyInstruction.create("sender-rejected-valid-delimiter", "§4,"));

        v0_0_0a0.add(PutKeyInstruction.create("§f-> §cMissing §7required argument§8: {2}", "argument-missing"));
        v0_0_0a0.add(PutKeyInstruction.create("§f-> §cUnknown§7 argument for {2}§8: §f{3}", "argument-unknown"));
        v0_0_0a0.add(PutKeyInstruction.create("§3§o{0}", "argument-syntax-name"));
        v0_0_0a0.add(PutKeyInstruction.create("§b{0}", "argument-syntax-known-item"));
        v0_0_0a0.add(PutKeyInstruction.create("§3|", "argument-syntax-known-delimiter"));
        v0_0_0a0.add(PutKeyInstruction.create("{0}", "argument-syntax-required"));
        v0_0_0a0.add(PutKeyInstruction.create("§3[{0}§3]", "argument-syntax-optional"));

        v0_0_0a0.add(RemoveKeyInstruction.create("version"));

        return result;
    }

}
