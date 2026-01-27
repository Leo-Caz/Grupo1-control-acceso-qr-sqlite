package QR;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ZXingDecoder {

    public static Optional<String> decode(BufferedImage img) {
        if (img == null) return Optional.empty();

        // 1) Intento normal
        Optional<String> r1 = decodeWithHints(img);
        if (r1.isPresent()) return r1;

        // 2) Intento con padding (simula “zona silenciosa”)
        BufferedImage padded = addWhitePadding(img, 80);
        return decodeWithHints(padded);
    }

    private static Optional<String> decodeWithHints(BufferedImage img) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(img);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, Collections.singletonList(BarcodeFormat.QR_CODE));
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            Result result = new MultiFormatReader().decode(bitmap, hints);
            return Optional.ofNullable(result.getText());
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    private static BufferedImage addWhitePadding(BufferedImage img, int pad) {
        BufferedImage out = new BufferedImage(
                img.getWidth() + 2 * pad,
                img.getHeight() + 2 * pad,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = out.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, out.getWidth(), out.getHeight());
        g.drawImage(img, pad, pad, null);
        g.dispose();
        return out;
    }
}

