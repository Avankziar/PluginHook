package main.java.me.avankziar.ph.spigot.ifh.provider;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Unmodifiable;

import me.avankziar.ifh.spigot.permission.Permission;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;

public class PermissionProvider implements Permission
{
	private LuckPerms lp;
	
	public PermissionProvider()
	{
		lp = LuckPermsProvider.get();
	}
	
	//Eine Methode um Permission, welche direkt beim Spieler liegen, von LP zu holen, auf dem Server zwischen zu speichern.
	//Beim Joinen werden die Temporären Perms gesetzt und beim leaven wird das in einer mysql zwischen gespeichert.
	private void dodo(User user, Player player)
	{
		Set<PermissionNode> pnode = user.getNodes(NodeType.PERMISSION).stream()
			    .filter(Node::hasExpiry)
			    .collect(Collectors.toSet());
		NodeMap data = user.data();
		for(PermissionNode pn : pnode)
		{
			pn.getKey();
			pn.getExpiryDuration().get(ChronoUnit.MILLIS);
			pn.getValue();
			data.remove(pn);
			lp.getUserManager().saveUser(user);
			
		}
		
		//Hinzufügen beim joinen.
		Node node = Node.builder("some.node.key")
		        .value(false)
		        .expiry(Duration.ofMillis(0))
		        .withContext(DefaultContextKeys.SERVER_KEY, "survival")
		        .build();
		data.add(node);
		lp.getUserManager().saveUser(user);
	}
	
	private User getOnlineUser(Player player)
	{
		return lp.getPlayerAdapter(Player.class).getUser(player);
	}
	
	private CompletableFuture<User> getOfflineUser(UUID uuid)
	{
		return lp.getUserManager().loadUser(uuid);
	}
	
	private long expireIn(Collection<Node> node, String permission)
	{
		for(Node n : node)
		{
			if(n.getKey().equalsIgnoreCase(permission))
			{
				if(n.hasExpiry())
				{
					return n.getExpiryDuration().toMillis();
				} else
				{
					return -1;
				}
			}
		}
		return 0;
	}
	
	public boolean hasPermission(Player player, String permission)
	{
		return getOnlineUser(player).getCachedData().getPermissionData().checkPermission(permission).asBoolean();
	}
	
	public CompletableFuture<Boolean> hasPermission(UUID uuid, String permission)
	{
		return getOfflineUser(uuid)
				.thenApplyAsync(user -> {
					Collection<Node> permnode = user.data().toCollection();
					return permnode.stream().anyMatch(x -> x.getKey().equals(permission));
				});
	}
	
	public long expireIn(Player player, String permission)
	{
		return expireIn(getOnlineUser(player).data().toCollection(), permission);
	}
	
	public long expireIn(UUID uuid, String permission)
	{
		return expireIn(lp.getUserManager().getUser(uuid).getNodes(), permission); 
	}
	
	public void addPermissionAtPlayer(UUID uuid, String permission, Long duration, Map<String, String> additionalContext)
	{
		lp.getUserManager().modifyUser(uuid, u -> 
		{
			if(duration != null && additionalContext != null)
			{
				ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
				additionalContext.forEach(builder::add);
				ImmutableContextSet set = builder.build();
				u.data().add(Node.builder(permission).context(set).expiry(duration).build());
			} else if(duration != null)
			{
				u.data().add(Node.builder(permission).expiry(duration).build());
			} else if(additionalContext != null)
			{
				ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
				additionalContext.forEach(builder::add);
				ImmutableContextSet set = builder.build();
				u.data().add(Node.builder(permission).context(set).build());
			} 
		});
	}
	
	public void removePermissionAtPlayer(UUID uuid, String permission)
	{
		lp.getUserManager().modifyUser(uuid, u -> 
		{
			u.data().remove(Node.builder(permission).build());
		});
	}
	
	public boolean isInGroup(Player player, String group)
	{
		User user = getOnlineUser(player);
		@NonNull @Unmodifiable Collection<net.luckperms.api.model.group.Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
	    return inheritedGroups.stream().anyMatch(g -> g.getName().equals(group));
	}
	
	public boolean hasPermission(String group, String permission)
	{
		return lp.getGroupManager().getGroup(group).data().toCollection().stream().anyMatch(x -> x.getKey().equals(permission));
	}
	
	public ArrayList<String> getGroups()
	{
		ArrayList<String> groups = new ArrayList<>();
		for(net.luckperms.api.model.group.Group g : lp.getGroupManager().getLoadedGroups())
		{
			groups.add(g.getName());
		}		
		return groups;
	}
	
	public long expireIn(String group, String permission)
	{
		return expireIn(lp.getGroupManager().getGroup(group).getNodes(), permission);
	}
	
	public void addPermissionAtGroup(String group, String permission, Long duration, Map<String, String> additionalContext)
	{
		lp.getGroupManager().modifyGroup(group, g -> 
		{
			if(duration != null && additionalContext != null)
			{
				ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
				additionalContext.forEach(builder::add);
				ImmutableContextSet set = builder.build();
				g.data().add(Node.builder(permission).context(set).expiry(duration).build());
			} else if(duration != null)
			{
				g.data().add(Node.builder(permission).expiry(duration).build());
			} else if(additionalContext != null)
			{
				ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
				additionalContext.forEach(builder::add);
				ImmutableContextSet set = builder.build();
				g.data().add(Node.builder(permission).context(set).build());
			} 
		});
	}
	
	public void removePermissionAtGroup(String group, String permission)
	{
		lp.getGroupManager().modifyGroup(group, g -> 
		{
			g.data().remove(Node.builder(permission).build());
		});
	}
}