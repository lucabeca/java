package me.patuncio.almas;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.patuncio.almas.comandos.CommandAlmas;
import me.patuncio.almas.comandos.GiveEspada;
import me.patuncio.almas.eventos.Death;
import me.patuncio.almas.eventos.Interact;
import me.patuncio.almas.eventos.InventoryClick;
import me.patuncio.almas.eventos.Join;
import me.patuncio.almas.eventos.MercadorAntiLixo;
import me.patuncio.almas.metodos.Almas;
import me.patuncio.almas.metodos.Config;
import me.patuncio.almas.metodos.MySQL;
import me.patuncio.almas.metodos.Placeholder;

public class Main extends JavaPlugin {
	
	
	@Override
	public void onEnable() {
		System.out.println("§c[zPAlmas] Plugin sendo habilitado...");
		System.out.println("§c[zPAlmas] Desenvolvido por zPatuncio_");
		System.out.println("§c[zPAlmas] Versão: " + getDescription().getVersion());
		saveDefaultConfig();
		Config.saveDataConfig();
		if (getConfig().getBoolean("MYSQL.USAR")) {
			MySQL.abrirConexao();
			MySQL.criarTabela();
		}
		
		getCommand("almas").setExecutor(new CommandAlmas());
		getCommand("giveespada").setExecutor(new GiveEspada());
		getServer().getPluginManager().registerEvents(new Interact(), this);
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		getServer().getPluginManager().registerEvents(new MercadorAntiLixo(), this);
		getServer().getPluginManager().registerEvents(new Join(), this);
		getServer().getPluginManager().registerEvents(new Death(), this);
		System.out.println("§c[zPAlmas] Plugin habilitado!");
		System.out.println("§c[zPAlmas] Desenvolvido por zPatuncio_");
		System.out.println("§c[zPAlmas] Versão: " + getDescription().getVersion());
		if (getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI") != null) {
			Placeholder.register();
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Almas.loadTop();
			}
		}.runTaskTimer(this, 0L, getConfig().getInt("CONFIGURACOES.DELAYTOP")*1200L);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (getConfig().getBoolean("CONFIGURACOES.SPAWNONLOAD")) {
					List<String> list = Config.getDataConfig().getStringList("Locais");
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							String[] split = list.get(i).split(";");
							Almas.spawnMercador(new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])), false);
						}
					}
				}
			}
		}.runTaskLater(this, 25*20L);
	}

	@Override
	public void onDisable() {
		if (getConfig().getBoolean("MYSQL.USAR")) {
			MySQL.fecharConexao();
		}
	}
	
	public static Plugin getPlugin() {
		return Main.getPlugin(Main.class);
	}
}
