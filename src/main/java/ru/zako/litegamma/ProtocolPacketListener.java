package ru.zako.litegamma;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import ru.zako.LightUpdateWrapperProxy;
import ru.zako.litegamma.users.User;
import ru.zako.litegamma.users.UserManager;

@Getter
public class ProtocolPacketListener extends PacketAdapter {

    private final ProtocolManager protocolManager;
    private final UserManager userManager;

    public ProtocolPacketListener(Plugin plugin, ProtocolManager protocolManager, UserManager userManager) {
        super(plugin, ListenerPriority.HIGH, WrapperProvider.PROTOCOL_LIGHT_PACKETS);
        this.plugin = plugin;
        this.protocolManager = protocolManager;
        this.userManager = userManager;
    }
    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        if (!Config.WORLDS.contains(packetEvent.getPlayer().getWorld().getName()))
            return;

        final var packet = packetEvent.getPacket();
        final LightUpdateWrapperProxy proxy = WrapperProvider.create(packet);
        if (!proxy.isContainsLightData()) return;

        String playerName = packetEvent.getPlayer().getName();
        User user = this.userManager.getUser(playerName);
        byte level = this.userManager.getLevel(user);

        if (!user.isEnable()) return;

        proxy.fillMasks();
        proxy.clearEmptyMasks();
        proxy.setLightLevel(level);
    }
}
