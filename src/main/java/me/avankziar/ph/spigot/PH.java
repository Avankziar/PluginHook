package main.java.me.avankziar.ph.spigot;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.avankziar.ph.spigot.ifh.provider.PermissionProvider;
import main.java.me.avankziar.ph.spigot.ifh.provider.VanishProvider;

public class PH extends JavaPlugin
{
	public static Logger log;
	private static PH plugin;
	public String pluginName = "PluginHook";
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=PH
		log.info(" ██████╗ ██╗  ██╗ | API-Version: "+plugin.getDescription().getAPIVersion());
		log.info(" ██╔══██╗██║  ██║ | Author: "+plugin.getDescription().getAuthors().toString());
		log.info(" ██████╔╝███████║ | Plugin Website: "+plugin.getDescription().getWebsite());
		log.info(" ██╔═══╝ ██╔══██║ | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		log.info(" ██║     ██║  ██║ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		log.info(" ╚═╝     ╚═╝  ╚═╝ | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
	
		setupIFHProvider();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		log.info(pluginName + " is disabled!");
	}

	public static PH getPlugin()
	{
		return plugin;
	}
	
	public boolean existHook(String externPluginName)
	{
		return plugin.getServer().getPluginManager().isPluginEnabled(externPluginName);
	}
	
	private void setupIFHProvider()
	{
		setupIFHVanish();
	}
	
	private void setupIFHVanish()
	{
		if(existHook("PremiumVanish"))
		{
			VanishProvider vp = new VanishProvider();
	    	plugin.getServer().getServicesManager().register(
	    	main.java.me.avankziar.ifh.spigot.interfaces.Vanish.class,
	        vp,
	        this,
	        ServicePriority.Normal);
	    	log.info(pluginName + " detected InterfaceHub >>> Vanish.class is provided!");
		}
		if (existHook("LuckPerms")) 
		{
			PermissionProvider p = new PermissionProvider();
        	plugin.getServer().getServicesManager().register(
        			main.java.me.avankziar.ifh.spigot.permission.Permission.class,
             		p,
             		plugin,
             		ServicePriority.Normal);
        	log.info(pluginName + " detected InterfaceHub >>> Permission.class is consumed!");
			return;
		}
	}
	
	/*private void setupIFHConsumer()
	{ 
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
		new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
			    if(i == 20)
			    {
				cancel();
				return;
			    }
			    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.administration.Administration> rsp = 
		                         getServer().getServicesManager().getRegistration(Administration.class);
			    if (rsp == null) 
			    {
			    	i++;
			        return;
			    }
			    rootAConsumer = rsp.getProvider();
			    log.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
			    cancel();
			}
        }.runTaskTimer(plugin, 20L, 20*2);
	}*/
}