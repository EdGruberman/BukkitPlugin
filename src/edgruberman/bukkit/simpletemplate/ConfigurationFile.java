package edgruberman.bukkit.simpletemplate;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.plugin.Plugin;

/**
 * Standardized plugin configuration file management class.<br />
 * <br />
 * Defaults will be extracted from the JAR.  Save requests can be queued to
 * avoid performance penalties for too many save requests occurring too
 * frequently.
 * 
 * @author EdGruberman
 */
public final class ConfigurationFile {
    
    private static final int TICKS_PER_SECOND = 20;
    
    private Plugin owner;
    private File file;
    private URL defaults;
    private int maxSaveFrequency;
    private Calendar lastSave = null;
    private Integer taskSave = null;
    
    /**
     * Construct configuration file reference for standardized load and save
     * management.  (No restrictions on how frequent saves can occur.)
     * 
     * @param owner plugin that owns this configuration file.
     * @param file name of file in the default data directory.
     * @param defaults path to default configuration file supplied in JAR.
     */
    protected ConfigurationFile(Plugin owner, String file, String defaults) {
        this(owner, file, defaults, -1);
    }
    
    /**
     * Construct configuration file reference for standardized load and save
     * management.
     * 
     * @param owner plugin that owns this configuration file.
     * @param file name of file in the default data directory.
     * @param defaults path to default configuration file supplied in JAR.
     * @param maxSaveFrequency shortest duration in seconds each save can occur.
     */
    protected ConfigurationFile(Plugin owner, String file, String defaults, int maxSaveFrequency) {
        this.owner = owner;
        this.file = new File(this.owner.getDataFolder(), file);
        this.defaults = this.owner.getClass().getResource(defaults);
        this.maxSaveFrequency = maxSaveFrequency;
    }
    
    /**
     * Loads the configuration file from plugin data folder.  This method will
     * create the file from the default supplied in the JAR if necessary.
     */
    protected void load() {
        if (!this.file.exists()) {
            try {
                this.extract(this.defaults, this.file);
            } catch (Exception e) {
                System.err.println("[" + this.owner.getDescription().getName() + "] Unable to extract default configuration file.");
                e.printStackTrace();
            }
        }
        
        this.owner.getConfiguration().load();
    }
    
    /**
     * Extract a file from the JAR to the local file system.
     * 
     * @param source Location of file in JAR.
     * @param destination File system path to save file to.
     * @throws Exception
     */
    private void extract(URL source, File destination) throws Exception {
        InputStream in = source.openStream();
        
        destination.getParentFile().mkdir();
        OutputStream out = new FileOutputStream(destination);
        
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    protected int getMaxSaveFrequency() {
        return this.maxSaveFrequency;
    }
    
    protected void setMaxSaveFrequency(int frequency) {
        this.maxSaveFrequency = frequency;
    }
    
    /**
     * Save the configuration file immediately. All cached save requests will be
     * saved to the file system.
     */
    protected void save() {
        this.save(true);
    }
    
    /**
     * Request a save of the configuration file. If request is not required to
     * be done immediately and last save was less than configured max frequency
     * then request will be cached and a scheduled task will kick off after the
     * max frequency has expired since last save.
     * 
     * @param immediately true to force a save of the configuration file immediately.
     */
    protected void save(boolean immediately) {
        if (!immediately) {
            // Determine how long since last save.
            long sinceLastSave = this.maxSaveFrequency;
            if (this.lastSave != null)
                sinceLastSave = (System.currentTimeMillis() - this.lastSave.getTimeInMillis()) / 1000;
            
            // Schedule a cache flush to run if last save was less than maximum save frequency.
            if (sinceLastSave < this.maxSaveFrequency) {
                // If task already scheduled let it run when expected.
                if (this.taskSave != null && this.owner.getServer().getScheduler().isQueued(this.taskSave)) return;
                
                // Schedule task to save cache to file system.
                final ConfigurationFile configurationManager = this;
                this.taskSave = this.owner.getServer().getScheduler().scheduleSyncDelayedTask(
                          this.owner
                        , new Runnable() { public void run() { configurationManager.save(true); } }
                        , (this.maxSaveFrequency - sinceLastSave) * ConfigurationFile.TICKS_PER_SECOND
                );
            
                return;
            }
        }
        
        this.owner.getConfiguration().save();
        this.lastSave = new GregorianCalendar();
    }
}