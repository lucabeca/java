package me.patuncio.almas.eventos;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.patuncio.almas.Main;
import me.patuncio.almas.metodos.Almas;
import net.md_5.bungee.api.ChatColor;

public class Death implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		if ((e.getEntity() instanceof Player) && (e.getEntity().getKiller() instanceof Player)) {
			Player p = e.getEntity();
			Player k = e.getEntity().getKiller();
			FileConfiguration config = Main.getPlugin().getConfig();
			
			if ((Almas.delay.containsKey(k)) && (config.getBoolean("DELAY.USAR"))) {
				return;
			}
			
			if (Almas.newRandom()) {
				ItemStack item = new ItemStack(397, 1, (short) 3);
				SkullMeta meta = (SkullMeta)item.getItemMeta();
				meta.setOwner(p.getName());
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("ALMA.NOME")).replace("{jogador}", p.getName()));
				List<String> lore = new ArrayList<String>();
				for (String s : config.getStringList("ALMA.LORE")) {
					lore.add(ChatColor.translateAlternateColorCodes('&', s));
				}
				meta.setLore(lore);
				if (config.getBoolean("ALMA.GLOW")) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				item.setItemMeta(meta);
				k.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.RESGATOUALMA")).replace("{jogador}", p.getName()));
				k.getInventory().addItem(item);
				if (config.getBoolean("SONS.ATIVAR")) {
					String[] som = config.getString("SONS.RESGATOU").split(";");
					try {
						k.playSound(k.getLocation(), Sound.valueOf(som[0]), Float.parseFloat(som[1]), Float.parseFloat(som[2]));
					} catch (Exception e2) {
						System.out.println("§c[zPAlmas] Ocorreu um erro ao tocar um som " + som[0]);
					}
				}
				if (config.getBoolean("DELAY.USAR")) {
					Almas.putDelay(k, p);
				}
				return;
			}
		}
	}
	
}
