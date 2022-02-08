import java.io.IOException;

public class Main {
    public static void main(String args[]) {

        String faceImage = "images/face.jpg";
        String headBackImage = "images/back.png";
        String headRightImage = "images/side.png";
        String headTopImage = "images/top.png";
        String headBottomImage = "images/bottom.png";

        try {
            SkinGenerator skinGenerator = new SkinGenerator(faceImage, headBackImage, headRightImage,
                    null, headTopImage, headBottomImage);
            skinGenerator.generateSkin();
            SkinCombiner skinCombiner = new SkinCombiner("images/steve/128x128.png", "images/steve/128x128.png");
            skinCombiner.combineSkins();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
