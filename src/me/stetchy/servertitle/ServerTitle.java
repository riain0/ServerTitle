package me.stetchy.servertitle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

/* @author hamgooof */

public class ServerTitle extends JavaPlugin {

	private static ServerTitle serverTitle;

	List<SubPlugin> subPluginList = new ArrayList<SubPlugin>();

	public static ServerTitle getServerTitle() {
		return serverTitle;
	}

	public SubPlugin getSubPlugin(Class<?> class1) {
		for (SubPlugin sp : subPluginList)
			if (sp.getClass() == class1) {
				return sp;
			}
		return null;
	}

	@Override
	public void onDisable() {
		for (Iterator<SubPlugin> it = subPluginList.iterator(); it.hasNext();) {
			it.next().onDisable();
			it.remove();
		}
	}

	@Override
	public void onEnable() {
		ServerTitle.serverTitle = this;
		setupSubPlugins();
		loadSubPlugins();
	}

	private void loadSubPlugins() {
		for (Iterator<SubPlugin> iter = subPluginList.iterator(); iter
				.hasNext();) {
			try {
				if (!iter.next().onEnable())
					iter.remove();
			} catch (Exception e) {
			}
		}
	}

	private void setupSubPlugins() {
		subPluginList.add(new Events(this));
	}

}
