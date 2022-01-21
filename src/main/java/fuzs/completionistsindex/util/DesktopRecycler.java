package fuzs.completionistsindex.util;

import java.awt.*;
import java.io.File;

public class DesktopRecycler implements WorldRecycler {
    @Override
    public boolean isSupported() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH);
    }

    @Override
    public boolean moveToTrash(File file) {
        return Desktop.getDesktop().moveToTrash(file);
    }
}
