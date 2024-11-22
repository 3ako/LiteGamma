package ru.zako.litegamma.command.list;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.zako.litegamma.Config;
import ru.zako.litegamma.command.SubCommand;

@RequiredArgsConstructor
public class ReloadCommand implements SubCommand {
   private final Plugin plugin;

   @Getter
   private final List<String> aliases = List.of("reload");

   @Getter
   private final Permission permission = new Permission("litegamma.reload");

   @Getter
   private final String description = " &e/litegamma reload &7- &fПерезагрузить конфиг \n";

   public void onCommand(@NonNull CommandSender sender, @NotNull @NonNull String[] args) {
      plugin.reloadConfig();
      Config.load(plugin.getConfig());
      sender.sendMessage(ChatColor.GREEN + "Config has been reloaded!");
   }
}
