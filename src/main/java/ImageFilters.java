import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageFilters {

    public static List<List<Color>> mirrorTopBottom(List<List<Color>> pixelMap) {
        List<List<Color>> reversedPixelMap = new ArrayList<>();

        for (int i = 0; i < pixelMap.size(); i++) {
            List<Color> copy = new ArrayList<>(pixelMap.get(i));
            Collections.reverse(copy);
            reversedPixelMap.add(copy);
        }
        return reversedPixelMap;
    }

    public static List<List<Color>> mirrorLeftRight(List<List<Color>> pixelMap) {
        List<List<Color>> reversedPixelMap = new ArrayList<>(pixelMap);
        Collections.reverse(reversedPixelMap);
        return reversedPixelMap;
    }

}
