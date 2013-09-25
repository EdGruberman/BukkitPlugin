package edgruberman.bukkit.bukkitplugin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import edgruberman.bukkit.bukkitplugin.versioning.StandardVersion;
import edgruberman.bukkit.bukkitplugin.versioning.Version;

/**
 * @author EdGruberman (ed@rjump.com)
 * @version 3.0.0
 */
public class DefaultPlugin extends JavaPlugin {

    public static final Level DEFAULT_LOG_LEVEL = Level.INFO;

    public static final String KEY_LOG_LEVEL = "log-level";

    public static final Charset CONFIGURATION_TARGET = Charset.defaultCharset();
    public static final Charset CONFIGURATION_SOURCE = Charset.forName("UTF-8");
    public static final String CONFIGURATION_ARCHIVE = "{0} - Archive version {1} - {2,date,yyyyMMddHHmmss}.yml"; // 0 = name, 1 = version, 2 = date
    public static final String CONFIGURATION_FILE = "config.yml";

    /** minimum version required for configuration files; indexed by relative file name (e.g. "config.yml") */
    private final Map<String, Version> configurationMinimums = new HashMap<String, Version>();

    private FileConfiguration config = null;

    public void putConfigMinimum(final String version) {
        this.putConfigMinimum(DefaultPlugin.CONFIGURATION_FILE, version);
    }

    public void putConfigMinimum(final String resource, final String version) {
        this.configurationMinimums.put(resource, StandardVersion.parse(version));
    }

    @Override
    public FileConfiguration getConfig() {
        if (this.config == null) this.reloadConfig();
        return this.config;
    }

    @Override
    public void reloadConfig() {
        this.config = this.loadConfig(DefaultPlugin.CONFIGURATION_FILE);
        this.setLogLevel(this.getConfig().getString(DefaultPlugin.KEY_LOG_LEVEL));
        this.getLogger().log(Level.FINEST, "YAML configuration file encoding: {0}", DefaultPlugin.CONFIGURATION_TARGET);
    }

    @Override
    public void saveDefaultConfig() {
        this.extractConfig(DefaultPlugin.CONFIGURATION_FILE, false);
    }

    /** @param resource file name relative to plugin data folder and base of jar (embedded file extracted to file system if does not exist) */
    public VersionedYamlConfiguration loadConfig(final String resource) {
        return this.loadConfig(resource, this.configurationMinimums.get(resource));
    }

    /** @param resource file name relative to plugin data folder and base of jar (embedded file extracted to file system if does not exist) */
    public VersionedYamlConfiguration loadConfig(final String resource, final Version required) {
        // extract default if not existing
        this.extractConfig(resource, false);

        final File existing = new File(this.getDataFolder(), resource);
        final VersionedYamlConfiguration result = new VersionedYamlConfiguration();
        try {
            result.load(existing);
        } catch (final InvalidConfigurationException e) {
            throw new IllegalStateException("Unable to load configuration file: " + existing.getPath() + " (Ensure file is encoded as " + DefaultPlugin.CONFIGURATION_TARGET + ")", e);
        } catch (final Exception e) {
            throw new RuntimeException("Unable to load configuration file: " + existing.getPath(), e);
        }
        if (required == null) return result;

        // verify required or later version
        final Version version = result.getVersion();
        if (version.compareTo(required) >= 0) return result;

        this.archiveConfig(resource, version);

        // extract default and reload
        return this.loadConfig(resource, null);
    }

    /** extract embedded configuration file from jar, translating character encoding to default character set */
    public void extractConfig(final String resource, final boolean replace) {
        final File config = new File(this.getDataFolder(), resource);
        if (config.exists() && !replace) return;

        this.getLogger().log(Level.FINE, "Extracting configuration file {1} {0} as {2}", new Object[] { resource, DefaultPlugin.CONFIGURATION_SOURCE.name(), DefaultPlugin.CONFIGURATION_TARGET.name() });
        config.getParentFile().mkdirs();

        final char[] cbuf = new char[1024]; int read;
        try {
            final Reader in = new BufferedReader(new InputStreamReader(this.getResource(resource), DefaultPlugin.CONFIGURATION_SOURCE));
            final Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(config), DefaultPlugin.CONFIGURATION_TARGET));
            while((read = in.read(cbuf)) > 0) out.write(cbuf, 0, read);
            out.close(); in.close();

        } catch (final Exception e) {
            throw new IllegalStateException("Could not extract configuration file \"" + resource + "\" to " + config.getPath() + "\"", e);
        }
    }

    /** make a backup copy of an existing configuration file */
    public void archiveConfig(final String resource, final Version version) {
        final File backup = new File(this.getDataFolder(), MessageFormat.format(DefaultPlugin.CONFIGURATION_ARCHIVE, resource.replaceAll("(?i)\\.yml$", ""), version, new Date()));
        final File existing = new File(this.getDataFolder(), resource);

        if (!existing.renameTo(backup))
            throw new IllegalStateException("Unable to archive configuration file \"" + existing.getPath() + "\" with version \"" + version + "\" to \"" + backup.getPath() + "\"");

        this.getLogger().warning("Archived configuration file \"" + existing.getPath() + "\" with version \"" + version + "\" to \"" + backup.getPath() + "\"");
    }

    public void setLogLevel(final String name) {
        Level level;
        try { level = Level.parse(name); } catch (final Exception e) {
            level = DefaultPlugin.DEFAULT_LOG_LEVEL;
            this.getLogger().warning("Log level defaulted to " + level.getName() + "; Unrecognized java.util.logging.Level: " + name + "; " + e);
        }

        // only set the parent handler lower if necessary, otherwise leave it alone for other configurations that have set it
        for (final Handler h : this.getLogger().getParent().getHandlers())
            if (h.getLevel().intValue() > level.intValue()) h.setLevel(level);

        this.getLogger().setLevel(level);
        this.getLogger().log(Level.CONFIG, "Log level set to: {0} ({1,number,#})"
                , new Object[] { this.getLogger().getLevel(), this.getLogger().getLevel().intValue() });
    }

}
