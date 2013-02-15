package edgruberman.bukkit.simpletemplate.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author EdGruberman (ed@rjump.com)
 * @version 1.0.1
 */
public class BufferedYamlConfiguration extends YamlConfiguration implements Runnable {

    private static final int TICKS_PER_SECOND = 20;

    private final Plugin plugin;
    private File file;
    private final long minSave;

    private long lastSaveAttempt = -1;
    private int taskSave = -1;

    /** @param minSave minimum time between saves (milliseconds) */
    public BufferedYamlConfiguration(final Plugin plugin, final File file, final long minSave) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        this.file = file;
        this.minSave = minSave;
        this.load();
    }

    public File getFile() {
        return this.file;
    }

    public long getMinSave() {
        return this.minSave;
    }

    public long getLastSaveAttempt() {
        return this.lastSaveAttempt;
    }

    public void load() throws IOException, InvalidConfigurationException {
        try {
            super.load(this.file);
        } catch (final FileNotFoundException e) {
            this.loadFromString("");
        } catch (final IOException e) {
            throw e;
        } catch (final InvalidConfigurationException e) {
            throw e;
        }
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

        if (elapsed < this.minSave) {
            final long delay = this.minSave - elapsed;

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

}
