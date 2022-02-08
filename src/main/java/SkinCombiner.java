import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SkinCombiner {

    private final BufferedImage body;
    private final BufferedImage outerLayer;

    public SkinCombiner(String bodyFilePath, String outerLayerFilePath) throws IOException {
        this.body = ImageIO.read(new File(bodyFilePath));
        this.outerLayer = ImageIO.read(new File(outerLayerFilePath));
    }

    public void combineSkins() throws IOException {
        System.out.println("Combining skins...");
        if (body.getWidth() == 64) {
            combine64x64Skins();
        } else if (body.getWidth() == 128) {
            combine128x128Skins();
        }
        ImageIO.write(body, "png", new File("generated-skins/combined" + UUID.randomUUID() + ".png"));
        System.out.println("Done...");
    }

    public void combine64x64Skins() throws IOException {
        copyAndPasteArea(0, 0, 32, 16, 32, 0); // head
        copyAndPasteArea(0, 16, 56, 16, 0, 32); // body, right arm, and right leg
        copyAndPasteArea(16, 48, 16, 16, 0, 48); // left leg
        copyAndPasteArea(32, 48, 16, 16, 48, 48); // left arm
    }

    public void combine128x128Skins() throws IOException {
        copyAndPasteArea(0, 0, 32*2, 16*2, 32*2, 0); // head
        copyAndPasteArea(0, 16*2, 56*2, 16*2, 0, 32*2); // body, right arm, and right leg
        copyAndPasteArea(16*2, 48*2, 16*2, 16*2, 0, 48*2); // left leg
        copyAndPasteArea(32*2, 48*2, 16*2, 16*2, 48*2, 48*2); // left arm
    }

    private void copyAndPasteArea(int from_x, int from_y, int width, int height, int to_x, int to_y) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = outerLayer.getRGB(from_x + x, from_y + y);
                body.setRGB(to_x + x, to_y + y, pixel);
            }
        }
    }
}
