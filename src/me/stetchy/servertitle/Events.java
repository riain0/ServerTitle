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
	private List<String> actbartext;
	private int npfadeIn, npfadeOut, npstay, fadeIn, stay, fadeOut;
	private static int time, amount;
	int scheduler = -1;
	ChatColor npcolour, colour;

	private void resetScheduler() {
		if (scheduler >= 0)
			this.getServer().getScheduler().cancelTask(scheduler);
		this.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(this, new ActionBarTask(actbartext),
						0L, 20);
		this.getServer().getScheduler()
				.scheduleSyncRepeatingTask(this, new Runnable() {

					@Override
					public void run() {
						ActionBarTask.setCalled(ActionBarTask.getCalled() + 1);
					}

				}, 0L, 20);
	}

	@Override
	public void onEnable() {
		onStart();
		Main.addSubPlugin(this);
		resetScheduler();
	}

	@Override
	public boolean onStart() {
		setupConfig();
		if (!this.getConfig().getBoolean("servertitle.enabled")) {
			return false;
		}
		actbartext = this.getConfig().getStringList(
				"servertitle.actionbar.text");
		amount = this.getConfig().getInt("servertitle.actionbar.amount");
		time = this.getConfig().getInt("servertitle.actionbar.time");
		Bukkit.getPluginManager().registerEvents(this, this);
		return true;
	}

	private void setupConfig() {
		FileConfiguration fc = this.getConfig();
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
		List<String> configA = new ArrayList<String>() {
			{
				this.add("&dDefault text");
				this.add("&aDefault text 2");
				this.add("&cDefault text 3");
			}
		};
		fc.addDefault("servertitle.actionbar.text", configA);
		fc.addDefault("servertitle.actionbar.time", 100);
		fc.addDefault("servertitle.actionbar.amount", 3);
		fc.options().copyDefaults(true);
		this.saveConfig();
	}

	@Override
	public void onDisable() {
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPlayedBefore()) {
			npconfigText = validate(this.getConfig().getString(
					"servertitle.onJoin.newplayer.nptext"));
			npconfigsubText = validate(this.getConfig().getString(
					"servertitle.onJoin.newplayer.npsubtext"));
			npfadeOut = this.getConfig().getInt(
					"servertitle.onJoin.newplayer.npfadeout");
			npstay = this.getConfig().getInt(
					"servertitle.onJoin.newplayer.npstay");
			npfadeIn = this.getConfig().getInt(
					"servertitle.onJoin.newplayer.npfadein");
			npcolour = ChatColor.getByChar(this.getConfig().getString(
					"servertitle.onJoin.newplayer.colour"));
			TitleUtil.sendTitle(p, npfadeIn, npstay, npfadeOut, npconfigText,
					npconfigsubText, npcolour);
		} else {
			configText = validate(this.getConfig().getString(
					"servertitle.onJoin.text"));
			configsubText = validate(this.getConfig().getString(
					"servertitle.onJoin.subtext"));
			fadeOut = this.getConfig().getInt("servertitle.onJoin.fadeout");
			stay = this.getConfig().getInt("servertitle.onJoin.stay");
			fadeIn = this.getConfig().getInt("servertitle.onJoin.fadein");
			colour = ChatColor.getByChar(this.getConfig().getString(
					"servertitle.onJoin.colour"));
			TitleUtil.sendTitle(p, fadeIn, stay, fadeOut, configText,
					configsubText, colour);
		}
	}

	private String validate(String val) {
		return val.length() > 5 ? val : "";
	}

	public static int getTime() {
		return time;
	}

	public static void setTime(int time) {
		Events.time = time;
	}

	public static int getAmount() {
		return amount;
	}

	public static int setAmount(int amount) {
		Events.amount = amount;
		return amount;
	}
}
