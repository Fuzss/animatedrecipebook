package fuzs.completionistsindex.util;

import com.sun.jna.platform.FileUtils;
import net.minecraft.util.DirectoryLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class PathSimpleFileVisitor extends SimpleFileVisitor<Path> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path path;
    private final Path levelPath;
    private final DirectoryLock lock;

    public PathSimpleFileVisitor(Path path, Path levelPath, DirectoryLock lock) {
        this.path = path;
        this.levelPath = levelPath;
        this.lock = lock;
    }

    @Override
    public FileVisitResult visitFile(Path p_78323_, BasicFileAttributes p_78324_) throws IOException {
        if (!p_78323_.equals(this.path)) {
            LOGGER.info("Deleting {}", p_78323_);
            Files.delete(p_78323_);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path p_78320_, IOException p_78321_) throws IOException {
        if (p_78321_ != null) {
            throw p_78321_;
        } else {
            if (p_78320_.equals(this.levelPath)) {
                this.lock.close();
                Files.deleteIfExists(this.path);
            }
            Files.delete(p_78320_);
            return FileVisitResult.CONTINUE;
        }
    }

    public static boolean tryFileUtils(Runnable checkLock, DirectoryLock lock, Path levelPath, CallbackInfo callbackInfo) {
        FileUtils fileUtils = FileUtils.getInstance();
        if (fileUtils.hasTrash()) {
            checkLock.run();
            for (int i = 1; i <= 5; ++i) {
                LOGGER.info("My Attempt {}...", i);
                try {
                    lock.close();
                    fileUtils.moveToTrash(levelPath.toFile());
                    callbackInfo.cancel();
                    break;
                } catch (Exception e) {
                    LOGGER.warn("Failed to move to trash {}", levelPath, e);
                }
            }
        }
    }

    public static boolean tryDesktop(Runnable checkLock, DirectoryLock lock, Path levelPath, CallbackInfo callbackInfo) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH)) {
            checkLock.run();
            for (int i = 1; i <= 5; ++i) {
                LOGGER.info("Attempt {} moving world to trash...", i);
                try {
                    lock.close();
                    Desktop.getDesktop().moveToTrash(levelPath.toFile());
                    return true;
                } catch (Exception e) {
                    LOGGER.warn("Failed to move world to trash {}", levelPath, e);
                }
            }
        }
        return false;
    }
}
