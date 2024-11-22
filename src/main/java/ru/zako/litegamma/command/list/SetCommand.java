package ru.zako.litegamma.command.list;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import ru.zako.litegamma.command.SubCommand;
import ru.zako.litegamma.users.User;
import ru.zako.litegamma.users.UserManager;

@RequiredArgsConstructor
public class SetCommand implements SubCommand {
   private final UserManager userManager;
   @Getter
   private final List<String> aliases = List.of("set");
   @Getter
   private final Permission permission = new Permission("litegamma.set");
   @Getter
   private final String description = """
            &e/litegamma set &7- &fВключить или выключить освещение\s
           
            &e/litegamma set [value] &7- &fУстановить значение освещения &8от 0 до 15\s
           """;

   public void onCommand(@NonNull CommandSender sender, @NotNull @NonNull String[] args) {
      if (!(sender instanceof Player player)) return;

      User user = userManager.getUser(player.getName());
      if (user == null) return;

      if (args.length == 1) {
         if (user.isEnable()) player.sendMessage(ChatColor.RED + " Освещение выключено");
         else player.sendMessage(ChatColor.GREEN + " Освещение включено");

         user.setEnable(!user.isEnable());
         return;
      }

      if (args.length == 2) {
         String levelName = args[1];

         int level;
         try {
            level = Integer.parseInt(levelName);
         } catch (NumberFormatException var8) {
            player.sendMessage(ChatColor.RED + " Неправильный синтаксис!");
            return;
         }

         if (level < 0 || level > 15) {
            player.sendMessage(ChatColor.RED + " Введите число от 0 до 15");
            return;
         }

         int setlevel = level * 17;
         user.setLevel(setlevel);
         player.sendMessage(ChatColor.GREEN + " Уровень освещения -> " + ChatColor.WHITE + level);
      }

   }
}
