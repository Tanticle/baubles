package tld.unknown.baubles;

import com.mojang.logging.LogUtils;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import org.slf4j.Logger;
import tld.unknown.baubles.api.BaublesData;

import java.util.function.Consumer;

@Mod(BaublesData.MOD_ID)
public class Baubles {

    private static final Logger LOGGER = LogUtils.getLogger();

    public Baubles(IEventBus modEventBus) {
        Registries.init(modEventBus);
    }

    //TODO: Issues with stackables in slots and shift click.
    //TODO: No unequip on shiftlick out of slot
    //TODO: Click Swapping

    //TODO: Client Equip fires on first open (Sync issue?)
    //TODO: Stop cursor from re-centering when opening baubles inv
}
