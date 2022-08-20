package main.java.me.avankziar.ph.spigot.ifh.provider;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.myzelyam.api.vanish.VanishAPI;
import main.java.me.avankziar.ifh.spigot.interfaces.Vanish;

public class VanishProvider implements Vanish
{
	@Override
	public ArrayList<UUID> getInvisiblePlayers()
	{
		return (ArrayList<UUID>) VanishAPI.getInvisiblePlayers();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ArrayList<UUID> getAllInvisiblePlayers()
	{
		return (ArrayList<UUID>) VanishAPI.getAllInvisiblePlayers();
	}
	
	@Override
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