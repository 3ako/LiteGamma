package ru.zako.litegamma.command;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

import java.util.Collection;
import javax.annotation.Nullable;
import org.bukkit.plugin.Plugin;
import ru.zako.litegamma.command.list.ReloadCommand;
import ru.zako.litegamma.command.list.SetCommand;
import ru.zako.litegamma.users.UserManager;

public class SubCommands {
   private final ClassToInstanceMap<SubCommand> commands;

   public SubCommands(Plugin plugin, UserManager userManager) {
      commands = new ImmutableClassToInstanceMap.Builder<SubCommand>()
              .put(ReloadCommand.class, new ReloadCommand(plugin))
              .put(SetCommand.class, new SetCommand(userManager))
              .build();
   }

   @Nullable
   public SubCommand getCommand(Class<? extends SubCommand> clazz) {
      return this.commands.get(clazz);
   }

   public Collection<SubCommand> getCommands() {
      return this.commands.values();
   }
}
