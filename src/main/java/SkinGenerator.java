import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.javatuples.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkinGenerator {

    final int HEAD_TOP_X_OFFSET = 40;
    final int HEAD_TOP_Y_OFFSET = 0;
    final int HEAD_BOTTOM_X_OFFSET = 48;
    final int HEAD_BOTTOM_Y_OFFSET = 0;
    final int HEAD_RIGHT_X_OFFSET = 32;
    final int HEAD_RIGHT_Y_OFFSET = 8;
    final int HEAD_FRONT_X_OFFSET = 40;
    final int HEAD_FRONT_Y_OFFSET = 8;
    final int HEAD_LEFT_X_OFFSET = 48;
    final int HEAD_LEFT_Y_OFFSET = 8;
    final int HEAD_BACK_X_OFFSET = 56;
    final int HEAD_BACK_Y_OFFSET = 8;

    final int RIGHT_ARM_HAND_X_OFFSET = 44;
    final int RIGHT_ARM_HAND_Y_OFFSET = 32;
    final int RIGHT_ARM_SHOULDER_X_OFFSET = 48;
    final int RIGHT_ARM_SHOULDER_Y_OFFSET = 32;
    final int RIGHT_ARM_OUTSIDE_X_OFFSET = 40;
    final int RIGHT_ARM_OUTSIDE_Y_OFFSET = 36;
    final int RIGHT_ARM_FRONT_X_OFFSET = 44;
    final int RIGHT_ARM_FRONT_Y_OFFSET = 36;
    final int RIGHT_ARM_INSIDE_X_OFFSET = 48;
    final int RIGHT_ARM_INSIDE_Y_OFFSET = 36;
    final int RIGHT_ARM_BACK_X_OFFSET = 52;
    final int RIGHT_ARM_BACK_Y_OFFSET = 36;




    private BufferedImage generatedSkin;
    private SourceImage faceImage;
    private SourceImage headLeftSideImage;
    private SourceImage headRightSideImage;
    private SourceImage headBackImage;
    private SourceImage headBottomImage;
    private SourceImage headTopImage;

    public SkinGenerator(String faceImageFilePath, String headBackImageFilePath, String headRightSideImageFilePath,
                         String headLeftSideImageFilePath, String headTopImageFilePath, String headBottomImageFilePath) throws IOException {
        this.generatedSkin = ImageIO.read(new File("blackout.png"));
        this.faceImage = unpackSquareImage(faceImageFilePath);
        this.headBackImage = unpackSquareImage(headBackImageFilePath);
        Pair<SourceImage, SourceImage> pair = unpackPossiblyMirroredSourceImages(headLeftSideImageFilePath, headRightSideImageFilePath);
        this.headLeftSideImage = pair.getValue0();
        this.headRightSideImage = pair.getValue1();
        this.headTopImage = unpackSquareImage(headTopImageFilePath);
        this.headBottomImage = unpackSquareImage(headBottomImageFilePath);
    }

    public void generateSkin() throws IOException {
        generateArea(faceImage, 8, 8, HEAD_FRONT_X_OFFSET, HEAD_FRONT_Y_OFFSET);
        generateArea(headBackImage, 8, 8, HEAD_BACK_X_OFFSET, HEAD_BACK_Y_OFFSET);
        generateArea(headLeftSideImage, 8, 8, HEAD_LEFT_X_OFFSET, HEAD_LEFT_Y_OFFSET);
        generateArea(headRightSideImage, 8, 8, HEAD_RIGHT_X_OFFSET, HEAD_RIGHT_Y_OFFSET);
        generateArea(headTopImage, 8, 8, HEAD_TOP_X_OFFSET, HEAD_TOP_Y_OFFSET);
        generateArea(headBottomImage, 8, 8, HEAD_BOTTOM_X_OFFSET, HEAD_BOTTOM_Y_OFFSET);

        ImageIO.write(generatedSkin, "png", new File("generated-skins/" + UUID.randomUUID() + ".png"));
        System.out.println("Done...");
    }


    private void generateArea(SourceImage srcImage, int width, int height, int xOffset, int yOffset) {
        int chunkWidth = srcImage.getWidth() / width;
        System.out.println("Chunk width: " + chunkWidth);
        List<Color> chunkPixels = new ArrayList<>();
        Color averageColor;

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                for (int y = i * chunkWidth; y < chunkWidth * (i + 1); y++) {
                    for (int x = k * chunkWidth; x < chunkWidth * (k + 1); x++) {
                        chunkPixels.add(srcImage.getPixels().get(y).get(x));
                    }
                }
                averageColor = averageColorFromPixels(chunkPixels);
                chunkPixels.clear();
                generatedSkin.setRGB(i + xOffset,k + yOffset, averageColor.getRGB());
            }
        }
    }



    private Color averageColorFromPixels(List<Color> pixels) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int i = 0; i < pixels.size(); i++) {
            red += pixels.get(i).getRed();
            green += pixels.get(i).getGreen();
            blue += pixels.get(i).getBlue();
        }
        red /= pixels.size();
        green /= pixels.size();
        blue /= pixels.size();

        return new Color(red, green, blue);
    }

    private Pair<SourceImage, SourceImage> unpackPossiblyMirroredSourceImages(String pathA, String pathB) throws IOException {

        SourceImage imageA;
        SourceImage imageB;

        if (pathB == null && pathA != null) {
            imageA = unpackSquareImage(pathA);
            List<List<Color>> rightSideImage = ImageFilters.mirrorLeftRight(headLeftSideImage.getPixels());
            int sideLength = imageA.getWidth();
            imageB = new SourceImage(sideLength, sideLength, rightSideImage);
        } else if (pathB != null && pathA == null) {
            imageB = unpackSquareImage(pathB);
            List<List<Color>> leftSideImage = ImageFilters.mirrorLeftRight(imageB.getPixels());
            int sideLength = imageB.getWidth();
            imageA = new SourceImage(sideLength, sideLength, leftSideImage);
        } else {
            imageA = unpackSquareImage(pathA);
            imageB = unpackSquareImage(pathB);
        }

        return new Pair<>(imageA, imageB);
    }


    private SourceImage unpackSquareImage(String filePath) throws IOException {
        File image = new File(filePath);
        BufferedImage bufferedImage = ImageIO.read(image);
        List<List<Color>> pixels = getSquarePixelMap(bufferedImage);
        return new SourceImage(pixels.size(), pixels.size(), pixels);
    }

    private List<List<Color>> getSquarePixelMap(BufferedImage image) {
        int side = image.getHeight();
        if (side > image.getWidth()) {
            side = image.getWidth();
        }

        List<List<Color>> pixelMap = new ArrayList<>();
        for (int x = 0; x < side; x++) {
            pixelMap.add(new ArrayList<>());
            for (int y = 0; y < side; y++) {
                int pixel = image.getRGB(x,y);
                pixelMap.get(x).add(new Color(pixel, true));
            }
        }
        return pixelMap;
    }
}
