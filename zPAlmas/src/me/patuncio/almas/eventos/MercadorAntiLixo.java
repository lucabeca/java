package me.patuncio.almas.eventos;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import me.patuncio.almas.Main;
import me.patuncio.almas.metodos.Almas;

public class MercadorAntiLixo implements Listener {
	
	@EventHandler
	public void fodasememo(EntityShootBowEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		if (e.getEntity().getName().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.NOME")))) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void fodasememo2(EntityTeleportEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		if (e.getEntity().getName().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.NOME")))) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void fodamemo3(EntityDamageEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		if (e.getEntity().getName().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.NOME")))) {
			e.setCancelled(true);
			e.getEntity().setFireTicks(0);
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void fodamemo4(EntityDamageByEntityEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		if (e.getEntity().getName().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.NOME")))) {
			if (!(e.getDamager() instanceof Player)) {
				e.setCancelled(true);
				e.getEntity().setFireTicks(0);
				return;
			}
			
			Player p = (Player) e.getDamager();
			if ((p.getItemInHand() == null) || (p.getItemInHand().getType().equals(Material.AIR))) {
				e.setCancelled(true);
				return;
			}
			
			if ((p.getItemInHand().getItemMeta().getDisplayName() != null) && (p.getItemInHand().getItemMeta().getDisplayName().equals("§6* §fMatadora de NPC"))) {
				e.setCancelled(true);
				Almas.removeMercador(e.getEntity().getLocation());
				e.getEntity().remove();
				return;
			}
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		if (e.getEntity().getName().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.NOME")))) {
			e.setCancelled(false);
			LivingEntity entity = (LivingEntity)e.getEntity();
			entity.getEquipment().clear();
			entity.setFireTicks(0);
			return;
		}
	}
	
	@EventHandler
	public void onRemove(ItemDespawnEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		if (e.getEntity().getName().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.NOME")))) {
		}
	}
}