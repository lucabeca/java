package me.patuncio.almas.eventos;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.patuncio.almas.metodos.Almas;

public class Join implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!Almas.contem(e.getPlayer())) {
			Almas.criarPlayer(e.getPlayer());
		}
	}
}
