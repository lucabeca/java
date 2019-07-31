package me.patuncio.almas.metodos;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import me.patuncio.almas.Main;

public class Config {
	
	private static FileConfiguration config = null;
	private static File file = null;
	private static Plugin plugin = Main.getPlugin();

	public static void saveDataConfig() {
		if (file == null) {
			reloadDataConfig();
		}
		
		try {
			config.save(file);
		} catch (IOException e) {
			System.out.println("§c(zPAlmas) Ocorreu um erro ao salvar a config data.yml!");
		}
	}
	
	public static void saveDefaultConfig() {
		if (file == null) {
			reloadDataConfig();
		}
		
		if (!file.exists()) {
			plugin.saveResource("data.yml", false);
		}
	}
	public static void reloadDataConfig() {
		file = new File(plugin.getDataFolder(), "data.yml");
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static FileConfiguration getDataConfig() {
		if ((config == null) || (file == null)) {
			reloadDataConfig();
		}
		return config;
	}

}
