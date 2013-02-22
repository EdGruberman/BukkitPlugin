package edgruberman.bukkit.simpletemplate.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;

/**
 * Queues save requests to prevent occurring more than a maximum rate.  Also enables delayed serialization processing for individual sections to occur at time of save.
 *
 * @author EdGruberman (ed@rjump.com)
 * @version 1.3.0
 */
public class BufferedYamlConfiguration extends YamlConfiguration implements Runnable {

    private static final int TICKS_PER_SECOND = 20;

    private final Plugin plugin;
    private File file;
    private final long rate;
    private final Map<String, ConfigurationSerializable> lazySections = new LinkedHashMap<String, ConfigurationSerializable>();
    private long lastSaveAttempt = -1;
    private int taskSave = -1;

    /** @param rate minimum time between saves (milliseconds) */
    public BufferedYamlConfiguration(final Plugin plugin, final File file, final long rate) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        this.file = file;
        this.rate = rate;
        this.load();
    }

    public File getFile() {
        return this.file;
    }

    public long getMinSave() {
        return this.rate;
    }

    public long getLastSaveAttempt() {
        return this.lastSaveAttempt;
    }

    public BufferedYamlConfiguration load() throws IOException, InvalidConfigurationException {
        try {
            super.load(this.file);
        } catch (final FileNotFoundException e) {
            this.loadFromString("");
        } catch (final IOException e) {
            throw e;
        } catch (final InvalidConfigurationException e) {
            throw e;
        }
        return this;
    }

    @Override
    public void load(final File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        this.file = file;
        this.load();
    }

    @Override
    public void save(final File file) throws IOException {
        this.file = file;
        super.save(file);
    }

    @Override
    public void run() {
        this.save();
        this.taskSave = -1;
    }

    /** force immediate save */
    public void save() {
        try {
            for (final Map.Entry<String, ConfigurationSerializable> section : this.lazySections.entrySet())
                this.set(section.getKey(), section.getValue().serialize());
            this.lazySections.clear();

            super.save(this.file);

        } catch (final IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Unable to save configuration file: {0}; {1}", new Object[] { this.file, e });
            return;

        } finally {
            this.lastSaveAttempt = System.currentTimeMillis();
        }

        this.plugin.getLogger().log(Level.FINEST, "Saved configuration file: {0}", this.file);
    }

    public void queueSave() {
        final long elapsed = System.currentTimeMillis() - this.lastSaveAttempt;

        if (elapsed < this.rate) {
            final long delay = this.rate - elapsed;

            if (this.isQueued()) {
                this.plugin.getLogger().log(Level.FINEST
                        , "Save request already queued to run in {0} seconds for file: {1} (Last attempted {2} seconds ago)"
                        , new Object[] { delay / 1000, this.getFile(), elapsed });
                return;
            }

            // schedule task to flush cache to file system
            this.taskSave = Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, this, delay * BufferedYamlConfiguration.TICKS_PER_SECOND);
            this.plugin.getLogger().log(Level.FINEST
                    , "Queued save request to run in {0} seconds for configuration file: {1} (Last attempted {2} seconds ago)"
                    , new Object[] { delay / 1000, this.getFile(), elapsed });
            return;
        }

        this.save();
    }

    public boolean isQueued() {
        return Bukkit.getScheduler().isQueued(this.taskSave);
    }

    /**
     * queue a ConfigurationSection to be updated when the next save occurs, request order preserved
     * @param path section path (a conflicting pending request will be replaced and put at the end of the queue)
     */
    public void setLazy(final String path, final ConfigurationSerializable section) {
        this.lazySections.put(path, section);
    }

}
