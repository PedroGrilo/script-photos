package file;

import org.opencv.core.Mat;

import java.io.File;
import java.time.LocalDateTime;

public class FilePropertiesClass implements FileProperties {
    private final File file;
    private final LocalDateTime date;
    private final float proportion;
    private boolean toDelete;
    private boolean seen;
    private Mat hash;

    public FilePropertiesClass(File file, boolean toDelete, boolean seen, float proportion, LocalDateTime date, Mat hash) {
        this.seen = seen;
        this.toDelete = toDelete;
        this.file = file;
        this.date = date;
        this.hash = hash;
        this.proportion = proportion;
    }

    @Override
    public boolean getToDelete() {
        return toDelete;
    }

    @Override
    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    @Override
    public boolean getSeen() {
        return seen;
    }

    @Override
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public float getProportion() {
        return proportion;
    }

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public Mat getHash() {
        return hash;
    }

    @Override
    public void setHash(Mat hash) {
        this.hash = hash;
    }
}
