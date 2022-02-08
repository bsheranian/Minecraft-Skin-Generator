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

    private BufferedImage generatedSkin;
    private SourceImage faceImage;
    private SourceImage headLeftImage;
    private SourceImage headRightImage;
    private SourceImage headBackImage;
    private SourceImage headBottomImage;
    private SourceImage headTopImage;

    public SkinGenerator(String faceImageFilePath, String headBackImageFilePath, String headRightSideImageFilePath,
                         String headLeftSideImageFilePath, String headTopImageFilePath, String headBottomImageFilePath)
                         throws IOException {
        this.generatedSkin = ImageIO.read(new File("blank-skin-64x64.png"));
        this.faceImage = unpackSquareImage(faceImageFilePath);
        this.headBackImage = unpackSquareImage(headBackImageFilePath);
        Pair<SourceImage, SourceImage> pair = unpackPossiblyMirroredSourceImages(headLeftSideImageFilePath, headRightSideImageFilePath);
        this.headLeftImage = pair.getValue0();
        this.headRightImage = pair.getValue1();
        this.headTopImage = unpackSquareImage(headTopImageFilePath);
        this.headBottomImage = unpackSquareImage(headBottomImageFilePath);
    }

    public void generateSkin() throws IOException {
        generateArea(faceImage, 8, 8, Offsets.HEAD_FRONT_X_OFFSET, Offsets.HEAD_FRONT_Y_OFFSET);
        generateArea(headBackImage, 8, 8, Offsets.HEAD_BACK_X_OFFSET, Offsets.HEAD_BACK_Y_OFFSET);
        generateArea(headLeftImage, 8, 8, Offsets.HEAD_LEFT_X_OFFSET, Offsets.HEAD_LEFT_Y_OFFSET);
        generateArea(headRightImage, 8, 8, Offsets.HEAD_RIGHT_X_OFFSET, Offsets.HEAD_RIGHT_Y_OFFSET);
        generateArea(headTopImage, 8, 8, Offsets.HEAD_TOP_X_OFFSET, Offsets.HEAD_TOP_Y_OFFSET);
        generateArea(headBottomImage, 8, 8, Offsets.HEAD_BOTTOM_X_OFFSET, Offsets.HEAD_BOTTOM_Y_OFFSET);

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
            List<List<Color>> rightSideImage = ImageFilters.mirrorLeftRight(headLeftImage.getPixels());
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
        int sideLength = image.getHeight();
        if (sideLength > image.getWidth()) {
            sideLength = image.getWidth();
        }

        List<List<Color>> pixelMap = new ArrayList<>();
        for (int x = 0; x < sideLength; x++) {
            pixelMap.add(new ArrayList<>());
            for (int y = 0; y < sideLength; y++) {
                int pixel = image.getRGB(x,y);
                pixelMap.get(x).add(new Color(pixel, true));
            }
        }
        return pixelMap;
    }
}
