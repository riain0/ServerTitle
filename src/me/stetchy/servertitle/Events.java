package me.stetchy.servertitle;

import java.util.ArrayList;
import java.util.List;

import me.stetchy.Main;
import me.stetchy.SubPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Events extends JavaPlugin implements Listener, SubPlugin {
	private String configText = "Default player text";
	private String configsubText = "Default player subtext";
	private String npconfigText = "Default new player text";
	private String npconfigsubText = "Default new player subtext";
	private String actbartext;
	private int npfadeIn, npfadeOut, npstay, fadeIn, stay, fadeOut,
			timeinMinutes;
	int scheduler = -1;
	ChatColor npcolour, colour;
	JavaPlugin jp;

	private void resetScheduler() {
		if (scheduler >= 0) {
			jp.getServer().getScheduler().cancelTask(scheduler);
		}
		jp.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(jp, new ActionBarTask(actbartext),
						0L, 1200 * timeinMinutes);
	}

	@Override
	public boolean onStart() {
		setupConfig();
		if (!jp.getConfig().getBoolean("servertitle.enabled")) {
			return false;
		}
		actbartext = jp.getConfig().getString(
				"servertitle.actionbar.text");
		timeinMinutes = jp.getConfig().getInt(
				"servertitle.actionbar.constant");
		Bukkit.getPluginManager().registerEvents(this, jp);
		resetScheduler();
		Main.addSubPlugin(this);
		return true;
	}

	private void setupConfig() {
		FileConfiguration fc = jp.getConfig();
		fc.addDefault("servertitle.enabled", true);
		fc.addDefault("servertitle.onJoin.newplayer.nptext", npconfigText);
		fc.addDefault("servertitle.onJoin.newplayer.npsubtext", npconfigsubText);
		fc.addDefault("servertitle.onJoin.newplayer.npfadein", 40);
		fc.addDefault("servertitle.onJoin.newplayer.npstay", 240);
		fc.addDefault("servertitle.onJoin.newplayer.npfadeout", 40);
		fc.addDefault("servertitle.onJoin.newplayer.colour",
				ChatColor.DARK_PURPLE.getChar() + "");
		fc.addDefault("servertitle.onJoin.text", configText);
		fc.addDefault("servertitle.onJoin.subtext", configsubText);
		fc.addDefault("servertitle.onJoin.fadein", 40);
		fc.addDefault("servertitle.onJoin.stay", 240);
		fc.addDefault("servertitle.onJoin.fadeout", 40);
		fc.addDefault("servertitle.onJoin.colour",
				ChatColor.DARK_PURPLE.getChar() + "");
		fc.addDefault("servertitle.actionbar.constant", 10);
		List<String> configB = new ArrayList() {
			{
				this.add("&dDefault message");
			}
		};
		fc.addDefault("servertitle.announcer.announcement", configB);
		fc.options().copyDefaults(true);
		jp.saveConfig();
	}
	
	@Override
	public void onDisable() {
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPlayedBefore()) {
			npconfigText = validate(jp.getConfig().getString(
					"servertitle.onJoin.newplayer.nptext"));
			npconfigsubText = validate(jp.getConfig().getString(
					"servertitle.onJoin.newplayer.npsubtext"));
			npfadeOut = jp.getConfig().getInt(
					"servertitle.onJoin.newplayer.npfadeout");
			npstay = jp.getConfig().getInt(
					"servertitle.onJoin.newplayer.npstay");
			npfadeIn = jp.getConfig().getInt(
					"servertitle.onJoin.newplayer.npfadein");
			npcolour = ChatColor.getByChar(jp.getConfig().getString(
					"servertitle.onJoin.newplayer.colour"));
			TitleUtil.sendTitle(p, npfadeIn, npstay, npfadeOut, npconfigText,
					npconfigsubText, npcolour);
		} else {
			configText = validate(jp.getConfig().getString(
					"servertitle.onJoin.text"));
			configsubText = validate(jp.getConfig().getString(
					"servertitle.onJoin.subtext"));
			fadeOut = jp.getConfig().getInt("servertitle.onJoin.fadeout");
			stay = jp.getConfig().getInt("servertitle.onJoin.stay");
			fadeIn = jp.getConfig().getInt("servertitle.onJoin.fadein");
			colour = ChatColor.getByChar(jp.getConfig().getString(
					"servertitle.onJoin.colour"));
			TitleUtil.sendTitle(p, fadeIn, stay, fadeOut, configText,
					configsubText, colour);
		}
	}

	private String validate(String val) {
		return val.length() > 5 ? val : "";
	}
}
