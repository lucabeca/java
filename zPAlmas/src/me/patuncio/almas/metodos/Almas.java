package me.patuncio.almas.metodos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.patuncio.almas.Main;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

@SuppressWarnings("deprecation")
public class Almas extends MySQL {

	public static FileConfiguration config = Config.getDataConfig();
	public static FileConfiguration mconfig = Main.getPlugin().getConfig();
	public static HashMap<Player, String> delay = new HashMap<Player, String>();
	public static List<String> tops = new ArrayList<String>();

	public static void removeMercador(Location loc) {
		List<String> list = config.getStringList("Locais");
		if (list.contains(loc.getWorld() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ())) {
			list.remove(loc.getWorld() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ());
		}
		config.set("Locais", list);
		Config.saveDataConfig();
	}

	public static void spawnMercador(Location loc, boolean save) {
		LivingEntity entity = null;

		if (save) {
			List<String> list = config.getStringList("Locais");
			list.add(loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ());
			config.set("Locais", list);
			Config.saveDataConfig();
		}

		try {
			entity = (LivingEntity) Bukkit.getWorld(loc.getWorld().getName()).spawnEntity(loc,
					EntityType.valueOf(mconfig.getString("CONFIGURACOES.NPC").toUpperCase()));
		} catch (Exception e) {
			System.out.println("§c[zPAlmas] Ocorreu um erro ao criar o Mercador, este entity não é válido!");
			return;
		}

		entity.setCustomName(ChatColor.translateAlternateColorCodes('&', mconfig.getString("MERCADOR.NOME")));
		entity.setCustomNameVisible(true);
		entity.setFireTicks(0);
		entity.getEquipment().clear();
		entity.setCanPickupItems(false);
		String a = Bukkit.getServer().getClass().getPackage().getName();
		String version = a.substring(a.lastIndexOf('.') + 1);

		if ((version.contains("1_8"))) {
			net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
			NBTTagCompound tag = nmsEntity.getNBTTag();
			if (tag == null) {
				tag = new NBTTagCompound();
			}
			nmsEntity.c(tag);
			tag.setInt("NoAI", 1);
			nmsEntity.f(tag);
		}

		if ((version.contains("1_9")) || (version.contains("1_11")) || (version.contains("1_12"))) {
			entity.setAI(true);
		}
	}

	public static void putDelay(Player p, Player outro) {
		delay.put(p, outro.getName() + ";" + mconfig.getInt("DELAY.TEMPO"));
		new BukkitRunnable() {
			@Override
			public void run() {
				if (delay.containsKey(p)) {
					if (Integer.valueOf(delay.get(p).split(";")[1]) != 0) {
						delay.put(p, outro.getName() + ";" + (Integer.valueOf(delay.get(p).split(";")[1]) - 1));
					} else {
						delay.remove(p, outro.getName() + ";" + 0);
					}
				}
			}
		}.runTaskTimer(Main.getPlugin(), 20L, 20L);
	}

	public static Boolean constainsDelay(Player p) {
		if (delay.containsKey(p)) {
			return true;
		}
		return false;
	}

	public static boolean newRandom() {
		Random random = new Random();
		if (random.nextInt(100) <= mconfig.getInt("CONFIGURACOES.CHANCE")) {
			return true;
		} else {
			return false;
		}
	}

	public static Inventory getResgatar() {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&',
				mconfig.getString("MERCADOR.INVENTARIO.ITENS.RESGATAR.NOME")));

		ItemStack recusar = new ItemStack(mconfig.getInt("MERCADOR.INVENTARIO.ITENS.RECUSAR.ID"), 1,
				(short) mconfig.getInt("MERCADOR.INVENTARIO.ITENS.RECUSAR.DATA"));
		ItemMeta rmeta = recusar.getItemMeta();
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				mconfig.getString("MERCADOR.INVENTARIO.ITENS.RECUSAR.NOME")));
		recusar.setItemMeta(rmeta);
		ItemStack aceitar = new ItemStack(mconfig.getInt("MERCADOR.INVENTARIO.ITENS.ACEITAR.ID"), 1,
				(short) mconfig.getInt("MERCADOR.INVENTARIO.ITENS.ACEITAR.DATA"));
		ItemMeta ameta = aceitar.getItemMeta();
		ameta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				mconfig.getString("MERCADOR.INVENTARIO.ITENS.ACEITAR.NOME")));
		aceitar.setItemMeta(ameta);
		ItemStack item = new ItemStack(
				Material.valueOf(mconfig.getString("MERCADOR.INVENTARIO.ITENS.MATERIALRESTANTE")));

		inv.setItem(0, recusar);
		inv.setItem(1, item);
		inv.setItem(2, item);
		inv.setItem(3, item);
		inv.setItem(5, item);
		inv.setItem(6, item);
		inv.setItem(7, item);
		inv.setItem(8, aceitar);
		return inv;
	}

	public static Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(null, 27,
				ChatColor.translateAlternateColorCodes('&', mconfig.getString("MERCADOR.INVENTARIO.NOME")));

		ItemStack resgatar = new ItemStack(
				Material.getMaterial(mconfig.getInt("MERCADOR.INVENTARIO.ITENS.RESGATAR.ID")), 1,
				(short) mconfig.getInt("MERCADOR.INVENTARIO.ITENS.RESGATAR.DATA"));
		ItemMeta rmeta = resgatar.getItemMeta();
		rmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				mconfig.getString("MERCADOR.INVENTARIO.ITENS.RESGATAR.NOME")));
		List<String> rlore = new ArrayList<String>();
		for (String s : mconfig.getStringList("MERCADOR.INVENTARIO.ITENS.RESGATAR.LORE")) {
			rlore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		rmeta.setLore(rlore);
		resgatar.setItemMeta(rmeta);

		ItemStack negro = new ItemStack(
				Material.getMaterial(mconfig.getInt("MERCADOR.INVENTARIO.ITENS.MERCADONEGRO.ID")), 1,
				(short) mconfig.getInt("MERCADOR.INVENTARIO.ITENS.MERCADONEGRO.DATA"));
		ItemMeta nmeta = negro.getItemMeta();
		nmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				mconfig.getString("MERCADOR.INVENTARIO.ITENS.MERCADONEGRO.NOME")));
		List<String> nlore = new ArrayList<String>();
		for (String s : mconfig.getStringList("MERCADOR.INVENTARIO.ITENS.MERCADONEGRO.LORE")) {
			nlore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		nmeta.setLore(nlore);
		negro.setItemMeta(nmeta);

		inv.setItem(mconfig.getInt("MERCADOR.INVENTARIO.ITENS.RESGATAR.SLOT") - 1, resgatar);
		inv.setItem(mconfig.getInt("MERCADOR.INVENTARIO.ITENS.MERCADONEGRO.SLOT") - 1, negro);
		return inv;
	}

	public static Inventory getInventoryItems() {
		Inventory inv = Bukkit.createInventory(null, mconfig.getInt("INVENTARIO.TAMANHO"),
				ChatColor.translateAlternateColorCodes('&', mconfig.getString("INVENTARIO.NOME")));
		for (String s : mconfig.getConfigurationSection("INVENTARIO.ITENS").getKeys(false)) {
			ItemStack item = new ItemStack(mconfig.getInt("INVENTARIO.ITENS." + s + ".ID"),
					mconfig.getInt("INVENTARIO.ITENS." + s + ".QUANTIDADE"),
					(short) mconfig.getInt("INVENTARIO.ITENS." + s + ".DATA"));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(
					ChatColor.translateAlternateColorCodes('&', mconfig.getString("INVENTARIO.ITENS." + s + ".NOME")));
			List<String> lore = new ArrayList<String>();
			for (String l : mconfig.getStringList("INVENTARIO.ITENS." + s + ".LORE")) {
				lore.add(ChatColor.translateAlternateColorCodes('&', l));
			}

			if (mconfig.getBoolean("INVENTARIO.ITENS." + s + ".GLOW")) {
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			} else {
				meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
			}

			meta.setLore(lore);
			item.setItemMeta(meta);

			for (String e : mconfig.getString("INVENTARIO.ITENS." + s + ".ENCANTAMENTOS").split("-")) {
				if ((e != null) || (e != "") || (e != " ")) {
					try {
						item.addUnsafeEnchantment(traduzir(e.split(":")[0]), Integer.valueOf(e.split(":")[1]));
					} catch (Exception ex) {
						System.out.println(
								"§c[zPAlmas] Ocorreu um erro ao adicionar encantamentos ao item. ( " + s + " )");
					}
				}
			}

			inv.setItem(mconfig.getInt("INVENTARIO.ITENS." + s + ".SLOT") - 1, item);
		}
		return inv;
	}

	public static Enchantment traduzir(String e) {
		if (e.equalsIgnoreCase("PROTECTION")) {
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		}
		if (e.equalsIgnoreCase("FIREPROTECTION")) {
			return Enchantment.PROTECTION_FIRE;
		}
		if (e.equalsIgnoreCase("FEATHERFALLING")) {
			return Enchantment.PROTECTION_FALL;
		}
		if (e.equalsIgnoreCase("BLASTPROTECTION")) {
			return Enchantment.PROTECTION_EXPLOSIONS;
		}
		if (e.equalsIgnoreCase("PROJECTILEPROTECTION")) {
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		}
		if (e.equalsIgnoreCase("SHARPNESS")) {
			return Enchantment.DAMAGE_ALL;
		}
		if (e.equalsIgnoreCase("KNOCKBACK")) {
			return Enchantment.KNOCKBACK;
		}
		if (e.equalsIgnoreCase("FIREASPECT")) {
			return Enchantment.FIRE_ASPECT;
		}
		if (e.equalsIgnoreCase("LOOTING")) {
			return Enchantment.LOOT_BONUS_MOBS;
		}
		if (e.equalsIgnoreCase("EFFICIENCY")) {
			return Enchantment.DIG_SPEED;
		}
		if (e.equalsIgnoreCase("SILKTOUCH")) {
			return Enchantment.SILK_TOUCH;
		}
		if (e.equalsIgnoreCase("UNBREAKING")) {
			return Enchantment.DURABILITY;
		}
		if (e.equalsIgnoreCase("FORTUNE")) {
			return Enchantment.LOOT_BONUS_BLOCKS;
		}
		return null;
	}

	public static void criarPlayer(Player p) {
		if (con != null) {
			try {
				PreparedStatement stm = con.prepareStatement("INSERT INTO zpalmas(player, quantia) VALUES(?,?)");
				stm.setString(1, p.getName());
				stm.setInt(2, 0);
				stm.executeUpdate();
			} catch (SQLException e) {
				System.out.println("§c[zPAlmas] §fOcorreu um erro ao criar o jogador " + p.getName());
			}
		} else {
			config.set("Jogadores." + p.getName(), 0);
			Config.saveDataConfig();
		}
	}

	public static boolean contem(Player p) {

		if (con != null) {
			try {
				PreparedStatement stm = con.prepareStatement("SELECT * FROM zpalmas WHERE player = ?");
				stm.setString(1, p.getName());
				ResultSet rs = stm.executeQuery();
				while (rs.next()) {
					return true;
				}
				return false;
			} catch (SQLException e) {
				return false;
			}
		} else {
			if (config.contains("Jogadores." + p.getName())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static void resetPlayer(Player p) {
		if (con != null) {
			if (!contem(p)) {
				criarPlayer(p);
				return;
			}

			try {
				PreparedStatement stm = con.prepareStatement("UPDATE zpalmas SET quantia = ? WHERE player = ?");
				stm.setInt(1, 0);
				stm.setString(2, p.getName());
				stm.executeUpdate();
			} catch (SQLException e) {
				System.out.println("§c[zPAlmas] §fNão foi possivel resetar as almas do player " + p.getName());
			}
		} else {
			config.set("Jogadores." + p.getName(), 0);
			Config.saveDataConfig();
		}
	}

	public static void setPlayer(Player p, Integer quantidade) {
		if (con != null) {
			if (!contem(p)) {
				criarPlayer(p);
				return;
			}

			try {
				PreparedStatement stm = con.prepareStatement("UPDATE zpalmas SET quantia = ? WHERE player = ?");
				stm.setInt(1, quantidade);
				stm.setString(2, p.getName());
				stm.executeUpdate();
			} catch (SQLException e) {
				System.out.println("§c[zPAlmas] §fNão foi possivel resetar as almas do player " + p.getName());
			}
		} else {
			config.set("Jogadores." + p.getName(), quantidade);
			Config.saveDataConfig();
		}
	}

	public static int getAlmas(Player p) {
		if (con != null) {
			if (contem(p)) {
				try {
					PreparedStatement stm = con.prepareStatement("SELECT * FROM zpalmas WHERE player = ?");
					stm.setString(1, p.getName());
					ResultSet rs = stm.executeQuery();
					while (rs.next()) {
						return rs.getInt("quantia");
					}
					return 0;
				} catch (SQLException e) {
					return 0;
				}
			} else {
				criarPlayer(p);
			}
			return 0;
		} else {
			if (contem(p)) {
				return config.getInt("Jogadores." + p.getName());
			} else {
				criarPlayer(p);
				return 0;
			}
		}
	}

	public static void addAlmas(Player p, Integer quantidade) {
		if (con != null) {
			if (contem(p)) {
				try {
					PreparedStatement stm = con.prepareStatement("UPDATE zpalmas SET quantia = ? WHERE player = ?");
					stm.setInt(1, getAlmas(p) + quantidade);
					stm.setString(2, p.getName());
					stm.executeUpdate();
				} catch (SQLException e) {
					System.out
							.println("§c§l[zPAlmas] §fOcorreu um erro ao atualizar as almas do player " + p.getName());
				}
			} else {
				criarPlayer(p);
			}
		} else {
			if (contem(p)) {
				config.set("Jogadores." + p.getName(), config.getInt("Jogadores." + p.getName()) + quantidade);
				Config.saveDataConfig();
			} else {
				criarPlayer(p);
				config.set("Jogadores." + p.getName(), quantidade);
				Config.saveDataConfig();
			}
		}
	}

	public static void removeAlmas(Player p, Integer quantidade) {
		if (con != null) {
			if (contem(p)) {
				try {
					PreparedStatement stm = con.prepareStatement("UPDATE zpalmas SET quantia = ? WHERE player = ?");
					stm.setInt(1, getAlmas(p) - quantidade);
					stm.setString(2, p.getName());
					stm.executeUpdate();
				} catch (SQLException e) {
					System.out
							.println("§c§l[zPAlmas] §fOcorreu um erro ao atualizar as almas do player " + p.getName());
				}
			} else {
				criarPlayer(p);
			}
		} else {
			if (contem(p)) {
				config.set("Jogadores." + p.getName(), config.getInt("Jogadores." + p.getName()) - quantidade);
				Config.saveDataConfig();
			} else {
				criarPlayer(p);
				config.set("Jogadores." + p.getName(), 0);
				Config.saveDataConfig();
			}
		}
	}

	public static List<String> getListTop() {
		return tops;
	}

	public static List<String> loadTop() {
		if (con != null) {
			try {
				tops.clear();
				PreparedStatement stm = con.prepareStatement("SELECT * FROM zpalmas ORDER BY quantia DESC");
				ResultSet rs = stm.executeQuery();
				int i = 0;
				while (rs.next()) {
					i++;
					tops.add(ChatColor
							.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("FORMATO.ABAIXO"))
							.replace("%player", rs.getString("player"))
							.replace("%quantia", String.valueOf(rs.getInt("quantia")))
							.replace("%lugar", String.valueOf(i)));
				}
			} catch (SQLException e) {
				System.out.println("§c[zPAlmas] §fOcorreu um erro ao carregar o almas top!");
			}
			return tops;
		} else {
			return null;
		}
	}
}