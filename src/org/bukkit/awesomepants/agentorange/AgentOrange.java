package org.bukkit.awesomepants.agentorange;

// Java imports
import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;


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
	Player p = (Player) sender;
	List worlds;

	if ((sender instanceof Player)) // If executed by the player.
	{
	    // The bounds of the cube around the Player.
	    int minX, minY, minZ;
	    int maxX, maxY, maxZ;
	    int radius = 20; // Hardcoded for now.
	    int leavesDeforested = 0;

	    // Our helper Block.
	    Block b;

	    // For now, just implement Deforest, and check against permissions.
	    // In the future, potentially implement /agentorange napalm and /agentorange forestfire.
	    if (this.permissionsEnabled && this.permissions.Security.permission(p, "agentorange.deforest"))
	    {
		// Get all World objects on this Server.
		worlds = getServer().getWorlds();

		// Since there should only be one, right now, go with the first one.
		// If there *are* more than one World, presume the first is the default world.
		// Eventually, we need to be able to determine *which* World the Player is in, which I haven't figured out yet - no Player.getWorld(), for example.
		//	However, there are World.getEntities() and World.getLivingEntities(), which return List<[Living]Entity>, so we should be able to run a List l.contains(Player p).
		//	So what we should be able to do is iterate through worlds, execute .getLivingEntities, and assign w to the World that returns true. We'll do that later.
		World w = (World) worlds.get(0);

		minX = (int) Math.round(p.getLocation().getX() - radius);
		minY = (int) Math.round(p.getLocation().getY() - radius);
		minZ = (int) Math.round(p.getLocation().getZ() - radius);
		maxX = (int) Math.round(p.getLocation().getX() + radius);
		maxY = (int) Math.round(p.getLocation().getY() + radius);
		maxZ = (int) Math.round(p.getLocation().getZ() + radius);

		// For possible useful reasons in the future, start at the top and work our way down.
		for (int y = maxY; y >= minY; y--)
		{
		    for (int x = minX; x <= maxX; x++)
		    {
			for (int z = minZ; z <= maxZ; z++)
			{
			    b = w.getBlockAt(x, y, z);

			    if (b.getType() == Material.LEAVES)
			    {
				b.setType(Material.AIR);
				leavesDeforested++;
			    }
			}
		    }
		}

		// We can make this optional in the future.
		getServer().broadcastMessage(String.format("%s has deforested a region of the world (%s leaves destroyed).", p.getName(), leavesDeforested));

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
