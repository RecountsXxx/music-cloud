package org.deus.src.services.converters;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class ConverterService {
    private static final Logger logger = LoggerFactory.getLogger(ConverterService.class);

    public byte[] convert(int targetWidth, int targetHeight, String type, ByteArrayOutputStream outputStream, byte[] originalBytes) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));

        if (originalImage == null) {
            throw new IOException("Failed to read image");
        }

        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(type);
        if (!writers.hasNext()) {
            throw new IOException("No writer found for type [" + type + "]");
        }

        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(resizedImage, null, null), param);
        }
        catch (Exception e) {
            logger.error("Some error occurred while writing to output stream");
        }

        return outputStream.toByteArray();
    }
}
