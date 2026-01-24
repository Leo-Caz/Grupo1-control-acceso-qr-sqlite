package qr;

import java.awt.image.BufferedImage;
import java.util.Optional;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ZXingDecoder {

    public static Optional<String> decode(BufferedImage img) {
        if (img == null) return Optional.empty();
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(img);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return Optional.of(result.getText());
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }
}
