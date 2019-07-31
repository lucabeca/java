package me.patuncio.almas.comandos;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.patuncio.almas.Main;
import me.patuncio.almas.metodos.Almas;
import me.patuncio.almas.metodos.Config;
import me.patuncio.almas.metodos.MySQL;

public class CommandAlmas implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		
		if (!(sender instanceof Player)) return true;
		
		if (cmd.getName().equalsIgnoreCase("almas")) {
			Player p = (Player)sender;
			FileConfiguration config = Main.getPlugin().getConfig();
			if (args.length == 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ALMASVER")).replace("{almas}", String.valueOf(Almas.getAlmas(p))));
				return true;
			}
			
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) {
					sendHelp(p);
					return true;
				}
				
				if (args[0].equalsIgnoreCase("mercador")) {
					if (config.getBoolean("SONS.ATIVAR")) {
						String[] som = config.getString("SONS.ABRIR").split(";");
						try {
							p.playSound(p.getLocation(), Sound.valueOf(som[0]), Float.parseFloat(som[1]), Float.parseFloat(som[2]));
						} catch (Exception e) {
							System.out.println("§c[zPAlmas] Ocorreu um erro ao tocar o som " + som[0]);
						}
					}
					p.openInventory(Almas.getInventory());
					return true;
				}
				
				if (args[0].equalsIgnoreCase("top")) {
					if (MySQL.con != null) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("FORMATO.ACIMA")));
						p.sendMessage(" ");
						List<String> s = Almas.getListTop();
						if (!s.isEmpty()) {
							if (s.size() >= 1) {
								p.sendMessage(s.get(0));
							}
							
							if (s.size() >= 2) {
								p.sendMessage(s.get(1));
							}
							
							if (s.size() >= 3) {
								p.sendMessage(s.get(2));
							}
							
							if (s.size() >= 4) {
								p.sendMessage(s.get(3));
							}
							
							if (s.size() >= 5) {
								p.sendMessage(s.get(4));
							}
							
							if (s.size() >= 6) {
								p.sendMessage(s.get(5));
							}
							
							if (s.size() >= 7) {
								p.sendMessage(s.get(6));
							}
							
							if (s.size() >= 8) {
								p.sendMessage(s.get(7));
							}
							
							if (s.size() >= 9) {
								p.sendMessage(s.get(8));
							}
							
							if (s.size() >= 10) {
								p.sendMessage(s.get(9));
							}
						}
						p.sendMessage(" ");
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ATIVEMYSQL")));
					}
					return true;
				}
				
				if (args[0].equalsIgnoreCase("setmercador")) {
					if (p.hasPermission("zPAlmas.admin")) {
						Almas.spawnMercador(p.getLocation(), true);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.MERCADORSETADO")));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.SEMPERMISSAO")));
					}
					return true;
				}
				
				if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("zPAlmas.admin")) {
						Main.getPlugin().reloadConfig();
						Config.reloadDataConfig();
						Almas.config = Config.getDataConfig();
						Almas.mconfig = Main.getPlugin().getConfig();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.CONFIGRECARREGADA")));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.SEMPERMISSAO")));
					}
					return true;
				}
				
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.NAOENCONTRADO")));
					return true;
				}
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ALMASVEROUTRO")).replace("{almas}", String.valueOf(Almas.getAlmas(player))).replace("{jogador}", player.getName()));
				return true;
			}
			
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setar")) {
					Player player = Bukkit.getPlayer(args[1]);
					if (player == null) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.NAOENCONTRADO")));
						return true;
					}
					
					Integer value;
					try {
						value = Integer.parseInt(args[2]);
					} catch (Exception e) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.NUMEROSVALIDOS")));
						return true;
					}
					
					if (p.hasPermission("zPAlmas.admin")) {
						Almas.setPlayer(player, value);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ALMASMODIFICOU")).replace("{jogador}", player.getName()).replace("{valor}", String.valueOf(value)));
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ALMASMODIFICADAS")).replace("{valor}", String.valueOf(value)));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.SEMPERMISSAO")));
					}
					return true;
				}

				sendHelp(p);
				return true;
			}
			
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.NAOENCONTRADO")));
				return true;
			}
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("MENSAGENS.ALMASVEROUTRO")).replace("{almas}", String.valueOf(Almas.getAlmas(player))).replace("{jogador}", player.getName()));
			return true;
		}
		return false;
	}
	
	private void sendHelp(Player p) {
		if (p.hasPermission("zPAlmas.admin")) {
			for (String s : Main.getPlugin().getConfig().getStringList("MENSAGENS.HELPADMIN")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			}
		} else {
			for (String s : Main.getPlugin().getConfig().getStringList("MENSAGENS.HELP")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			}
		}
	}
}