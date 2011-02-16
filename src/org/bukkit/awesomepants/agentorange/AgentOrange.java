package org.bukkit.awesomepants.agentorange;

// Java imports
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

// org.bukkit imports
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Server;

// Other imports
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * AgentOrange for Bukkit
 *
 * @author Kaletam
 */
public class AgentOrange extends JavaPlugin
{
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static final Logger log = Logger.getLogger("Minecraft"); // Get the Minecraft logger for, er, logging purposes.
    private Permissions permissions = null;
    private boolean permissionsEnabled = false;

    public AgentOrange(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader)
    {
	super(pluginLoader, instance, desc, folder, plugin, cLoader);
    }

    @Override
    public void onEnable()
    {
	// Register our events
	PluginManager pm = getServer().getPluginManager();

	// Load Permissions settings.
	this.setupPermissions();

	PluginDescriptionFile pdfFile = this.getDescription();
	log.log(Level.INFO, String.format("[%s] version [%s] enabled.", pdfFile.getName(), pdfFile.getVersion()));
    }

    @Override
    public void onDisable()
    {
	PluginDescriptionFile pdfFile = this.getDescription();
	log.log(Level.INFO, String.format("[%s] version [%s] signing off!", pdfFile.getName(), pdfFile.getVersion()));
    }

    // Dunno what this does yet.
    public boolean isDebugging(final Player player)
    {
	if (debugees.containsKey(player))
	{
	    return debugees.get(player);
	}
	else
	{
	    return false;
	}
    }

    // Dunno what this does yet.
    public void setDebugging(final Player player, final boolean value)
    {
	debugees.put(player, value);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
    {
	if ((sender instanceof Player)) // If executed by the player.
	{
	    Player p = (Player) sender;

	    if (this.permissionsEnabled && !this.permissions.Security.permission(p, "agentorange.deforest"))
	    {
		getServer().broadcastMessage(p.getName() + " has deforested a region of the world.");

		return true;
	    }

//	    // This method for getting subcommands stolen from HotSwap.
//	    // No sub commands right now, but keep this code here for now in case we implement any.
//	    SubCommands sub = null;
//
//	    try
//	    {
//		sub = SubCommands.valueOf(args[0].toUpperCase());
//	    }
//	    catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
//	    {
//		return false;
//	    }
//
//	    switch (sub)
//	    {
//		case NOTHING:
//		    p.sendMessage(quote);
//
//		    return true;
//		default:
//		    return false;
//	    }
	}
//	else
//	{
//	    // Don't do anything right now.
//	    //log.log(Level.INFO, "We're in onCommand, !(sender instanceof Player).");
//	    //System.out.println("Console test!");
//	    return false; // Right now, we don't actually succeed or fail, but for the console, let's output usage for testing purposes.
//	}
	return false;
    }

    // Stolen from VoidMage.
    public void setupPermissions()
    {
	Plugin p = this.getServer().getPluginManager().getPlugin("Permissions");

	if (this.permissions == null)
	{
	    if (p != null)
	    {
		this.permissions = (Permissions) p;
		this.permissionsEnabled = true;
	    }
	    else
	    {
		// Do nothing right now
	    }
	}
    }
    // This method for getting subcommands stolen from HotSwap.
    // No subcommands, right now, but keep this in case we implement any.
//    private enum SubCommands
//    {
//	NOTHING
//    }
}
