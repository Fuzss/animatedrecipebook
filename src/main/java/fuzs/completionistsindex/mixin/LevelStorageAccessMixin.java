package fuzs.completionistsindex.mixin;

import fuzs.completionistsindex.util.PathSimpleFileVisitor;
import net.minecraft.util.DirectoryLock;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Path;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public class LevelStorageAccessMixin {
    @Shadow
    @Final
    DirectoryLock lock;
    @Shadow
    @Final
    Path levelPath;

    @Unique
    private static final Logger LOGGER = LogManager.getLogger();

    @Shadow
    private void checkLock() {
        throw new IllegalStateException();
    }

    @Inject(method = "deleteLevel", at = @At("HEAD"), cancellable = true)
    public void deleteLevel(CallbackInfo callbackInfo) throws IOException {
        extracted(callbackInfo);

//        callbackInfo.cancel();
//        this.checkLock();
//        final Path path = this.levelPath.resolve("session.lock");
//        for (int i = 1; i <= 5; ++i) {
//            LOGGER.info("Attempt {}...", i);
//            try {
//                Files.walkFileTree(this.levelPath, new PathSimpleFileVisitor(path, this.levelPath, this.lock));
//                break;
//            } catch (IOException ioexception) {
//                if (i >= 5) {
//                    throw ioexception;
//                }
//                LOGGER.warn("Failed to delete {}", this.levelPath, ioexception);
//                try {
//                    Thread.sleep(500L);
//                } catch (InterruptedException ignored) {
//                }
//            }
//        }
    }

    private void extracted(CallbackInfo callbackInfo) {
        PathSimpleFileVisitor.tryFileUtils(this::checkLock, lock, levelPath, callbackInfo);
    }
}
