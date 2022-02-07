import java.awt.*;
import java.util.List;

public class SourceImage {
    private int width;
    private int height;
    private List<List<Color>> pixels;

    public SourceImage() {}

    public SourceImage(int width, int height, List<List<Color>> pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<List<Color>> getPixels() {
        return pixels;
    }

    public void setPixels(List<List<Color>> pixels) {
        this.pixels = pixels;
    }

    public boolean isSquare() {
        return height == width;
    }

}
