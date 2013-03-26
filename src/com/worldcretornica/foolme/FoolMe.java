package com.worldcretornica.foolme;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class FoolMe extends JavaPlugin implements Listener
{
	protected Map<String, Integer> fooledplayers = null;
	private static Plugin plugin = null;
	public boolean isEnabled = true;
	
	private String VERSION;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(label.equalsIgnoreCase("foolme"))
		{
			if(sender.hasPermission("foolme.admin"))
			{
				if(args.length == 0)
				{
					sender.sendMessage("FoolMe " + VERSION);
					sender.sendMessage(ChatColor.GREEN + "/foolme disable " + ChatColor.RESET + " To disable the plugin.");
					sender.sendMessage(ChatColor.GREEN + "/foolme enable " + ChatColor.RESET + " To enable the plugin.");
					return true;
				}
				else
				{
					if(args[0].equalsIgnoreCase("disable"))
					{
						isEnabled = false;
						sender.sendMessage("FoolMe disabled");
						return true;
					}else if(args[0].equalsIgnoreCase("enable"))
					{
						isEnabled = true;
						sender.sendMessage("FoolMe enabled. Only new logins will be affected.");
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void onDisable()
	{
		for(int taskid : fooledplayers.values())
		{
			Bukkit.getScheduler().cancelTask(taskid);
		}
		fooledplayers = null;
	}
	
	@Override
	public void onEnable() 
	{
		fooledplayers = new HashMap<String, Integer>();
		plugin = this;
		VERSION = getDescription().getVersion();
		isEnabled = true;
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
	@EventHandler()
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		if(isEnabled)
		{
			final String name = event.getPlayer().getName();
			scheduleSound(name, (long) (Math.random() * 100 + 120), 2);
		}
	}
	
	@EventHandler()
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();
		
		if(fooledplayers.containsKey(name))
		{
			fooledplayers.remove(name);
		}
	}
	
	public static FoolMe getSingleton()
	{
		return (FoolMe) plugin;
	}
	
	public static void scheduleSound(String name, long Delay, int volume)
	{
		FoolMe fm = getSingleton();
		
		if(fm.isEnabled)
		{
			BukkitTask bt = Bukkit.getScheduler().runTaskLater(plugin, new SoundPlayer(name, Delay, volume), Delay);
			
			if(fm.fooledplayers.containsKey(name))
			{
				int taskid = fm.fooledplayers.get(name);
				if(taskid != 0)
				{
					Bukkit.getScheduler().cancelTask(fm.fooledplayers.get(name));
				}
				fm.fooledplayers.remove(name);
			}
			fm.fooledplayers.put(name, bt.getTaskId());
		}
	}
}
