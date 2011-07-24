package edgruberman.bukkit.simpletemplate;

import edgruberman.bukkit.messagemanager.MessageManager;

public final class Main extends org.bukkit.plugin.java.JavaPlugin {

    static ConfigurationFile configurationFile;
    static MessageManager messageManager;
    
    public void onLoad() {
        Main.configurationFile = new ConfigurationFile(this);
        Main.configurationFile.load();
        
        Main.messageManager = new MessageManager(this);
        Main.messageManager.log("Version " + this.getDescription().getVersion());
        
        // TODO: Add plugin load code here.
    }
	
    public void onEnable() {
        // TODO: Add plugin enable code here.
        
        new CommandManager(this);
        
        Main.messageManager.log("Plugin Enabled");
    }
    
    public void onDisable() {
        // TODO: Add plugin disable code here.
        
        Main.messageManager.log("Plugin Disabled");
    }
}
