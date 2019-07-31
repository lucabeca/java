package me.patuncio.almas.eventos;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.patuncio.almas.Main;
import me.patuncio.almas.metodos.Almas;
import me.patuncio.almas.metodos.Glow;

public class InventoryClick implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		
		if (!(e.getWhoClicked() instanceof Player)) return;
		
		if (e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.INVENTARIO.NOME")))) {
			e.setCancelled(true);
			
			ItemStack item = e.getCurrentItem();
			if (item == null) return;
			if (item.getType().equals(Material.AIR)) return;
			if (item.getItemMeta().getDisplayName() == null) return;
			
			String nome = item.getItemMeta().getDisplayName();
			Player p = (Player)e.getWhoClicked();
			if (nome.equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.INVENTARIO.ITENS.RESGATAR.NOME")))) {
				if (config.getBoolean("SONS.ATIVAR")) {
					String[] som = config.getString("SONS.ABRIROUTRO").split(";");
					try {
						p.playSound(p.getLocation(), Sound.valueOf(som[0]), Float.parseFloat(som[1]), Float.parseFloat(som[2]));
					} catch (Exception ex) {
						System.out.println("§c[zPAlmas] Ocorreu um erro ao tocar o som " + som[0]);
					}
				}
				p.openInventory(Almas.getResgatar());
				return;
			}
			
			if (nome.equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.INVENTARIO.ITENS.MERCADONEGRO.NOME")))) {
				if (config.getBoolean("SONS.ATIVAR")) {
					String[] som = config.getString("SONS.ABRIROUTRO").split(";");
					try {
						p.playSound(p.getLocation(), Sound.valueOf(som[0]), Float.parseFloat(som[1]), Float.parseFloat(som[2]));
					} catch (Exception ex) {
						System.out.println("§c[zPAlmas] Ocorreu um erro ao tocar o som " + som[0]);
					}
				}
				p.openInventory(Almas.getInventoryItems());
				return;
			}
			return;
		}
		
		if (e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.INVENTARIO.ITENS.RESGATAR.NOME")))) {
			
			ItemStack item = e.getCurrentItem();
			Player p = (Player)e.getWhoClicked();
			
			if ((item != null) && (item.getTypeId() == 101)) {
				e.setCancelled(true);
				return;
			}
			
			if (e.getRawSlot() == 0) {
				e.setCancelled(true);
				p.closeInventory();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.RESGATECANCELADO")));
				if (e.getInventory().getItem(4) != null) {
					p.getInventory().addItem(e.getInventory().getItem(4));
				}
				return;
			}
			
			if (e.getRawSlot() == 8) {
				e.setCancelled(true);
				if (e.getInventory().getItem(4) != null) {
					ItemStack skull = e.getInventory().getItem(4);
					if ((skull.getTypeId() == 397) && (skull.getData().getData() == 3)) {
						Almas.addAlmas(p, skull.getAmount());
						e.getInventory().removeItem(skull);
						if (config.getBoolean("SONS.ATIVAR")) {
							String[] som = config.getString("SONS.RESGATAR").split(";");
							try {
								p.playSound(p.getLocation(), Sound.valueOf(som[0]), Float.parseFloat(som[1]), Float.parseFloat(som[2]));
							} catch (Exception ex) {
								System.out.println("§c[zPAlmas] Ocorreu um erro ao tocar o som " + som[0]);
							}
						}
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ALMASRESGATADAS")).replace("{quantidade}", String.valueOf(skull.getAmount())));
					} else {
						p.closeInventory();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ITEMINVALIDO")));
						p.getInventory().addItem(skull);
					}
				} else {
					p.closeInventory();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.COLOQUEITEM")));
				}
				return;
			}
			return;
		}
		
		if (e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', config.getString("INVENTARIO.NOME")))) {
			e.setCancelled(true);
			
			ItemStack item = e.getCurrentItem();
			if ((item == null) || (item.getType().equals(Material.AIR)) || (item.getItemMeta().getDisplayName() == null)) {
				return;
			}
			
			String nome = item.getItemMeta().getDisplayName();
			Player p = (Player)e.getWhoClicked();
			for (String s : config.getConfigurationSection("INVENTARIO.ITENS").getKeys(false)) {
				if (s == null) return;
				
				String value = "INVENTARIO.ITENS." + s + ".";
				if ((config.getInt(value + "ID") == item.getTypeId()) && (ChatColor.translateAlternateColorCodes('&', config.getString(value + "NOME")).equals(nome)) && (config.getInt(value + "DATA") == item.getDurability())) {
					if (Almas.getAlmas(p) >= config.getInt(value + "VALOR")) {
						Almas.removeAlmas(p, config.getInt(value + "VALOR"));
						if (config.getBoolean("SONS.ATIVAR")) {
							String[] som = config.getString("SONS.COMPROU").split(";");
							try {
								p.playSound(p.getLocation(), Sound.valueOf(som[0]), Float.parseFloat(som[1]), Float.parseFloat(som[2]));
							} catch (Exception ex) {
								System.out.println("§c[zPAlmas] Ocorreu um erro ao tocar o som " + som[0]);
							}
						}
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.TROCAEFETUADA")));
						Glow.remove(item);
						if (config.getBoolean("CONFIGURACOES.REMOVERLORE")) {
							ItemMeta meta = item.getItemMeta();
							meta.setLore(null);
							item.setItemMeta(meta);
						}
						p.getInventory().addItem(item);
						p.closeInventory();
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ALMASINSUFICIENTES")));
					}
				}
			}
			return;
		}
	}
}