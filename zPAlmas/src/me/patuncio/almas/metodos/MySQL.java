package me.patuncio.almas.metodos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import me.patuncio.almas.Main;


public class MySQL {
	
	public static Connection con = null;

	public static FileConfiguration cfg = Main.getPlugin().getConfig();
	
	public static void abrirConexao() {
		
		try {
			String host = cfg.getString("MYSQL.HOST");
			int porta = 3306;
			String Database = cfg.getString("MYSQL.DATABASE");
			String user = cfg.getString("MYSQL.USUARIO");
			String password = cfg.getString("MYSQL.SENHA");
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + porta + "/" + Database + "?autoReconnect=true", user, password);
			System.out.println("§a§l[zPAlmas] Conexão com MySQL Aceita!");
		} catch (SQLException e) {
			System.out.println("§c§l[zPAlmas] Conexão com MySQL Não Aceita!");
			con = null;
		}
	}
	
	public static void fecharConexao() {
		if (con != null) {
			try {
				con.close();
				System.out.println("§c[zPAlmas] §fConexão com MySQL §a§lFechada");
			} catch (SQLException e) {
				System.out.println("§c[zPAlmas] §fOcorreu um erro ao desconectar do MySQL");
			}
		}
	}
	
	public static void criarTabela() {
		if (con != null) {
			try {
				PreparedStatement stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS zpalmas (player VARCHAR(20), quantia INT(64))");
				stm.executeUpdate();
				System.out.println("§c[zPAlmas] §fTabela criada com sucesso!");
			} catch (SQLException e) {
				System.out.println("§c[zPAlmas] §fOcorreu um erro ao criar a tabela!");
			}
		}
	}
}