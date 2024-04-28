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

    //TODO: Stop cursor from re-centering when opening baubles inv
}
