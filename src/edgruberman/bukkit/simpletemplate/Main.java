package edgruberman.bukkit.simpletemplate;

import edgruberman.bukkit.messagemanager.MessageManager;

public final class Main extends org.bukkit.plugin.java.JavaPlugin {

    private static ConfigurationFile configurationFile;
    private static MessageManager messageManager;
    
    public void onLoad() {
        Main.configurationFile = new ConfigurationFile(this);
        Main.getConfigurationFile().load();
        
        Main.messageManager = new MessageManager(this);
        Main.getMessageManager().log("Version " + this.getDescription().getVersion());
        
        // TODO: Add plugin load code here.
    }
	
    public void onEnable() {
        // TODO: Add plugin enable code here.
        
        new CommandManager(this);
        
        Main.getMessageManager().log("Plugin Enabled");
    }
    
    public void onDisable() {
        // TODO: Add plugin disable code here.
        
        Main.getMessageManager().log("Plugin Disabled");
    }
    
    static ConfigurationFile getConfigurationFile() {
        return Main.configurationFile;
    }
    
    static MessageManager getMessageManager() {
        return Main.messageManager;
    }
}
