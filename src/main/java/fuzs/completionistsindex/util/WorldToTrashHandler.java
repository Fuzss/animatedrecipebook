package fuzs.completionistsindex.util;

import com.google.common.collect.ImmutableList;
import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.util.recycler.DesktopRecycler;
import fuzs.completionistsindex.util.recycler.FileUtilsRecycler;
import fuzs.completionistsindex.util.recycler.WorldRecycler;
import net.minecraft.util.DirectoryLock;

import java.nio.file.Path;
import java.util.List;

public class WorldToTrashHandler {
    private static final List<WorldRecycler> SUPPORTED_RECYCLERS;

    static {
        SUPPORTED_RECYCLERS = new ImmutableList.Builder<WorldRecycler>()
                .add(new FileUtilsRecycler(), new DesktopRecycler())
                .build();
    }

    public static boolean tryMoveToTrash(DirectoryLock lock, Path levelPath, Runnable checkLock) {
        for (WorldRecycler recycler : SUPPORTED_RECYCLERS) {
            if (recycler.isSupported()) {
                checkLock.run();
                for (int i = 1; i <= 5; ++i) {
                    CompletionistsIndex.LOGGER.info("Attempt {} moving world to trash...", i);
                    try {
                        lock.close();
                        if (recycler.moveToTrash(levelPath.toFile())) {
                            return true;
                        }
                    } catch (Exception e) {
                        CompletionistsIndex.LOGGER.warn("Failed to move world to trash {}", levelPath, e);
                    }
                }
            }
        }
        return false;
    }
}
