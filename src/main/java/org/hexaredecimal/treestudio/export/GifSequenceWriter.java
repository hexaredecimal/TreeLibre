package org.hexaredecimal.treestudio.export;

/**
 *
 * @author hexaredecimal
 */
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.util.Iterator;
import java.io.IOException;

public class GifSequenceWriter {

	private ImageWriter gifWriter;
	private ImageWriteParam imageWriteParam;
	private IIOMetadata imageMetaData;

	public GifSequenceWriter(ImageOutputStream outputStream, int imageType, int timeBetweenFramesMS, boolean loopContinuously) throws IOException {
		gifWriter = getWriter();
		imageWriteParam = gifWriter.getDefaultWriteParam();
		ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
		imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
		String metaFormatName = imageMetaData.getNativeMetadataFormatName();
		IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
		IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
		graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
		graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(timeBetweenFramesMS / 10));
		graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");
		IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
		IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
		child.setAttribute("applicationID", "NETSCAPE");
		child.setAttribute("authenticationCode", "2.0");
		int loop = loopContinuously ? 0 : 1;
		child.setUserObject(new byte[]{0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF)});
		appExtensionsNode.appendChild(child);
		imageMetaData.setFromTree(metaFormatName, root);
		gifWriter.setOutput(outputStream);
		gifWriter.prepareWriteSequence(null);
	}

	public void writeToSequence(java.awt.image.RenderedImage img) throws IOException {
		gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
	}

	public void close() throws IOException {
		gifWriter.endWriteSequence();
	}

	private static ImageWriter getWriter() throws IIOException {
		Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
		if (!iter.hasNext()) {
			throw new IIOException("No GIF Image Writers Exist");
		}
		return iter.next();
	}

	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
		for (int i = 0; i < rootNode.getLength(); i++) {
			if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
				return (IIOMetadataNode) rootNode.item(i);
			}
		}
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return node;
	}
}
