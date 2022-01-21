package fuzs.completionistsindex.util;

import com.sun.jna.platform.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtilsRecycler implements WorldRecycler {
    @Override
    public boolean isSupported() {
        return FileUtils.getInstance().hasTrash();
    }

    @Override
    public boolean moveToTrash(File file) throws IOException {
        FileUtils.getInstance().moveToTrash(file);
        return true;
    }
}
