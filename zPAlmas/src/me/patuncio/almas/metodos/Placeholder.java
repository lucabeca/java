package me.patuncio.almas.metodos;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.patuncio.almas.Main;

public class Placeholder {

	public static boolean register() {
		if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") == null) {
			return false;
		}
			PlaceholderAPI.registerPlaceholder(Main.getPlugin(), "zpalmas_almas", new PlaceholderReplacer() {
				
				@Override
				public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
					Player player = e.getPlayer();
					if (player == null) {
						return "";
					}
					
					return String.valueOf(Almas.getAlmas(player));
				}
			});
			return true;
	}
}
