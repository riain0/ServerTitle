package me.stetchy.servertitle;


import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R1.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleUtil {

	public static void sendTitle(Player player, Integer fadeIn, Integer stay,
			Integer fadeOut, String title, String subtitle, ChatColor colour) {

		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

		PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(
				EnumTitleAction.TIMES, null, fadeIn.intValue(),
				stay.intValue(), fadeOut.intValue());
		connection.sendPacket(packetPlayOutTimes);

		if (subtitle != null) {
			IChatBaseComponent titleSub = ChatSerializer.a("{text:\""
					+ ChatColor.translateAlternateColorCodes('&', subtitle)
					+ "\",color:" + colour.name().toLowerCase() + "}");
			PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(
					EnumTitleAction.SUBTITLE, titleSub);
			connection.sendPacket(packetPlayOutSubTitle);
		}
		if (title != null) {
			IChatBaseComponent titleMain = ChatSerializer.a("{text:\""
					+ ChatColor.translateAlternateColorCodes('&', title)
					+ "\",color:" + colour.name().toLowerCase() + "}");
			PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(
					EnumTitleAction.TITLE, titleMain);
			connection.sendPacket(packetPlayOutTitle);
		}
	}

	public static void sendTabTitle(Player player, String header, String footer) {
		if (header == null) {
			header = "";
		}
		header = ChatColor.translateAlternateColorCodes('&', header);
		if (footer == null) {
			footer = "";
		}
		footer = ChatColor.translateAlternateColorCodes('&', footer);

		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		IChatBaseComponent tabTitle = ChatSerializer.a("{\"text\": \"" + header
				+ "\"}");
		IChatBaseComponent tabFoot = ChatSerializer.a("{\"text\": \"" + footer
				+ "\"}");
		PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(
				tabTitle);
		try {
			Field field = headerPacket.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(headerPacket, tabFoot);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.sendPacket(headerPacket);
		}
	}

	public static void sendActionBar(Player p, String msg) {
		if (msg == null) {
			msg = "";
		}
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		IChatBaseComponent cbc = ChatSerializer
				.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}
}