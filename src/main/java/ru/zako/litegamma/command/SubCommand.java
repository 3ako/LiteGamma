package ru.zako.litegamma.command;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public interface SubCommand {
   List<String> getAliases();

   Permission getPermission();

   String getDescription();

   void onCommand(@NonNull CommandSender var1, @NonNull String[] var2);

   default List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
      return Collections.emptyList();
   }
}
