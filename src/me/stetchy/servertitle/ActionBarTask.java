package me.stetchy.servertitle;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ActionBarTask implements Runnable {
	private List<String> statements;
	private static int index = 1;
	private static int called = 0;
	JavaPlugin jp;

	public ActionBarTask(List<String> statements) {

		this.statements = statements;
	}

	@Override
	public void run() {
		try {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (index == 1) {
					TitleUtil.sendActionBar(p, statements.get(0));
				}
				if (index == 2) {
					TitleUtil.sendActionBar(p, statements.get(1));
				}
				/*
				 * if (Events.getAmount() == index) { for (Iterator<String> it =
				 * statements.iterator(); it .hasNext(); index++) { sendA(); } }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (called < Events.getTime()) {
			index = 1;
		}
		if (called >= Events.getTime() && called < Events.getTime() * 2) {
			index = 2;
		}
		if (called >= Events.getTime() * 2) {
			called = 0;
		}
	}

	/*
	 * public void sendA() { for (Player p : Bukkit.getOnlinePlayers()) {
	 * 
	 * TitleUtil.sendActionBar(p, statements.get(index)); } }
	 */

	public static int getCalled() {
		return called;
	}

	public static void setCalled(int called) {
		ActionBarTask.called = called;
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		ActionBarTask.index = index;
	}
}
