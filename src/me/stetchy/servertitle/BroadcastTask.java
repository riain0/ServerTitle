package me.stetchy.servertitle;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BroadcastTask implements Runnable {
	private List<String> statements;
	private int index = 0;

	public BroadcastTask(List<String> statements) {

		this.statements = statements;
	}

	@Override
	public void run() {
		try {
			for (Player p : Bukkit.getOnlinePlayers())
				for (String str : statements) {
					TitleUtil.sendActionBar(p, str);
				}
		} catch (Exception e) {

			for (Player p : Bukkit.getOnlinePlayers())
				for (String str : statements) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							str));
				}
		}
		index++;
		if (index >= statements.size())
			index = 0;
	}
}
