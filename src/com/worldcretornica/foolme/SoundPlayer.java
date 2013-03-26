package com.worldcretornica.foolme;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundPlayer implements Runnable 
{
	private String name = "";
	private long currentdelay = 0;
	private int intensity = 0;
	
	public SoundPlayer(String name, long NewDelay, int volume)
	{
		this.name = name;
		this.currentdelay = NewDelay;
		this.intensity = volume;
	}
	
	@Override
	public void run() 
	{
		if(FoolMe.getSingleton().isEnabled)
		{
			Player p = Bukkit.getPlayer(name);
			
			//int intbuffer = intensity;
			//int ctr = 0;
			
			//while(intbuffer > 0 && ctr <= 3)
			//{
				Location loc = p.getLocation().clone();
				
				loc = loc.add(Math.random() * 12 - 6, Math.random() * 8 - 4, Math.random() * 12 - 6);
				
		    	p.playSound(loc, getSound(), intensity, 2);
		    	
		    	//intbuffer = intbuffer - 2;
		    	//ctr++;
			//}
	    	
	    	if(currentdelay > 3 && intensity < 20)
	    	{
	    		FoolMe.scheduleSound(name, (long) (Math.floor(Math.random() * 10 + (currentdelay / 2))), intensity + 1);
	    	} 
	    	else 
	    	{
	    		p.playSound(p.getLocation(), Sound.GHAST_DEATH, intensity, 2);
	    	}
		}
	}
	
	private Sound getSound()
	{
		return Sound.GHAST_SCREAM;
		/*switch((int) (Math.random() * 1))
		{
		case 0:
			return Sound.GHAST_SCREAM;
		case 1:
			return Sound.GHAST_SCREAM;
		case 2:
			return Sound.GHAST_MOAN;
		case 3:
			return Sound.STEP_GRASS;
		case 4:
			return Sound.STEP_STONE;
		default:
			return Sound.STEP_GRAVEL;
		}*/
	}

}
