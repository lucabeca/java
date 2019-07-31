package me.patuncio.almas.comandos;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.patuncio.almas.Main;

public class GiveEspada implements CommandExecutor {
	
	private static FileConfiguration config = Main.getPlugin().getConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) {
		
		if (!(sender instanceof Player)) return true;
		
		if (cmd.getName().equalsIgnoreCase("giveespada")) {
			Player p =  (Player)sender;
			if (p.hasPermission("zPAlmas.admin")) {
				ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§6* §fMatadora de NPC");
				item.setItemMeta(meta);
				p.getInventory().addItem(item);
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.SEMPERMISSAO")));
			}
			return true;
		}
		return false;
	}

}
