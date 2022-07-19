package fuzs.completionistsindex.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;

@Mixin(ServerLifecycleHooks.class)
public class ServerLifecycleHooksMixin {

    @Inject(method = "getServerConfigPath", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getServerConfigPath(final MinecraftServer server, CallbackInfoReturnable<Path> callback) {
        callback.setReturnValue(FMLPaths.CONFIGDIR.get());
    }
}
