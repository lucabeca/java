package me.patuncio.almas.eventos;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import me.patuncio.almas.Main;
import me.patuncio.almas.metodos.Almas;

public class Interact implements Listener {
	
	@EventHandler
	public void interact(PlayerInteractAtEntityEvent e) {
		FileConfiguration config = Main.getPlugin().getConfig();
		if ((e.getPlayer() instanceof Player) && (e.getRightClicked().getName().equals(ChatColor.translateAlternateColorCodes('&', config.getString("MERCADOR.NOME"))))) {
			Player p = e.getPlayer();
			p.openInventory(Almas.getInventory());
			if (config.getBoolean("SONS.ATIVAR")) {
				String[] som = config.getString("SONS.ABRIR").split(";");
				try {
					p.playSound(p.getLocation(), Sound.valueOf(som[0]), Float.parseFloat(som[1]), Float.parseFloat(som[2]));
				} catch (Exception ex) {
					System.out.println("§c[zPAlmas] Ocorreu um erro ao tocar o som " + som[0]);
				}
			}
			return;
		}
	}
}