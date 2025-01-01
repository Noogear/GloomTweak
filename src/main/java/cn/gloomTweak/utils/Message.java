package cn.gloomTweak.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Message {

    public static Message instance;

    public Message() {
        instance = this;
        load();
    }

    public static void load() {
    }

    public static Component buildMsg(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component buildMsg(List<String> messages) {
        StringBuilder msg = new StringBuilder();
        for (String message : messages) {
            msg.append(message).append("\n");
        }
        int length = msg.length();
        if (length > 0 && msg.charAt(length - 1) == '\n') {
            msg.deleteCharAt(length - 1);
        }
        return MiniMessage.miniMessage().deserialize(msg.toString());
    }

    public static List<Component> buildComponentList(List<String> messages) {
        return messages.stream()
                .map(s->{
                    try {
                        return MiniMessage.miniMessage().deserialize(s);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static void sendMsg(CommandSender sender, Component message) {
        sender.sendMessage(message);
    }

    public static void sendMsg(CommandSender sender, String message) {
        Scheduler.runTaskAsync(()->{
            sender.sendMessage(buildMsg(message));
        });

    }

    public static void sendMsg(CommandSender sender, String message, Object... args) {
        Scheduler.runTaskAsync(()->{
            sender.sendMessage(buildMsg(String.format(message, args)));
        });

    }

    public static void showHelp(CommandSender sender) {
    }
}
