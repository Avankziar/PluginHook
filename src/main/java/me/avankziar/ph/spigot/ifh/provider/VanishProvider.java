package main.java.me.avankziar.ph.spigot.ifh.provider;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.myzelyam.api.vanish.VanishAPI;
import me.avankziar.ifh.spigot.interfaces.Vanish;

public class VanishProvider implements Vanish
{
	public ArrayList<UUID> getInvisiblePlayers()
	{
		return (ArrayList<UUID>) VanishAPI.getInvisiblePlayers();
	}
	
	public ArrayList<UUID> getAllInvisiblePlayers()
	{
		return (ArrayList<UUID>) VanishAPI.getAllInvisiblePlayers();
	}
	
	public boolean isInvisible(Player player)
	{
		return VanishAPI.isInvisible(player);
	}
	
	@Override
	public boolean isInvisibleOffline(UUID uuid)
	{
		return VanishAPI.isInvisibleOffline(uuid);
	}
	
	@Override
	public void hidePlayer(Player player)
	{
		VanishAPI.hidePlayer(player);
	}
	
	@Override
	public void showPlayer(Player player)
	{
		VanishAPI.showPlayer(player);
	}
	
	@Override
	public boolean canSee(Player viewer, Player viewed)
	{
		return VanishAPI.canSee(viewer, viewed);
	}
}