package me.stetchy.servertitle;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarTask implements Runnable {
	private List<String> statements;
	private static int index = 1;
	private static int called = 0;

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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (called < ServerTitle.getTime()) {
			index = 1;
		}
		if (called >= ServerTitle.getTime()
				&& called < ServerTitle.getTime() * 2) {
			index = 2;
		}
		if (called >= ServerTitle.getTime() * 2) {
			called = 0;
		}
	}

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
