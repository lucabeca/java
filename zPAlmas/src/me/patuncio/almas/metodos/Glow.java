package me.patuncio.almas.metodos;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Glow {
	
	public static void add(ItemMeta item) {
		item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	}
	
	public static void remove(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
	}
}