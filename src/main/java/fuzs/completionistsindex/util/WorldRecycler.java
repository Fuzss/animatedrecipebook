package fuzs.completionistsindex.util;

import java.io.File;
import java.io.IOException;

public interface WorldRecycler {
    boolean isSupported();

    boolean moveToTrash(File file) throws IOException;
}
