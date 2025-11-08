package org.hexaredecimal.treestudio.utils;

/**
 *
 * @author hexaredecimal
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Binary implements Closeable {

	private final File file;
	private DataOutputStream out;
	private DataInputStream in;
	private boolean compressed;

	/**
	 * Open for writing (overwrite existing file)
	 */
	public Binary(String path) throws IOException {
		this(path, false, false);
	}

	/**
	 * @param path File path
	 * @param readMode true to read, false to write
	 * @param compress true to enable GZIP compression
	 */
	public Binary(String path, boolean readMode, boolean compress) throws IOException {
		this.file = new File(path);
		this.compressed = compress;

		if (readMode) {
			InputStream fis = new FileInputStream(file);
			if (compress) {
				fis = new GZIPInputStream(fis);
			}
			this.in = new DataInputStream(fis);
		} else {
			OutputStream fos = new FileOutputStream(file, false);
			if (compress) {
				fos = new GZIPOutputStream(fos);
			}
			this.out = new DataOutputStream(fos);
		}
	}

	// --- WRITE METHODS ---
	public void write(int value) throws IOException {
		writeText(String.valueOf(value));
	}

	public void write(long value) throws IOException {
		writeText(String.valueOf(value));
	}

	public void write(short value) throws IOException {
		writeText(String.valueOf(value));
	}

	public void write(byte value) throws IOException {
		writeText(String.valueOf(value));
	}

	public void write(char value) throws IOException {
		writeText(String.valueOf(value));
	}

	public void write(String value) throws IOException {
		writeText(value);
	}

	private void writeText(String text) throws IOException {
		out.write(text.getBytes(StandardCharsets.UTF_8));
		out.write(0); // null terminator
	}

	// --- READ METHODS ---
	private String readUntilNull() throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int b;
		while ((b = in.read()) != -1) {
			if (b == 0) {
				break;
			}
			buf.write(b);
		}
		return buf.toString(StandardCharsets.UTF_8);
	}

	public String readString() throws IOException {
		return readUntilNull();
	}

	public int readInt() throws IOException {
		return Integer.parseInt(readUntilNull());
	}

	public long readLong() throws IOException {
		return Long.parseLong(readUntilNull());
	}

	public short readShort() throws IOException {
		return Short.parseShort(readUntilNull());
	}

	public byte readByte() throws IOException {
		return Byte.parseByte(readUntilNull());
	}

	public char readChar() throws IOException {
		String s = readUntilNull();
		return s.isEmpty() ? '\0' : s.charAt(0);
	}

	@Override
	public void close() throws IOException {
		if (out != null) {
			out.flush();
			out.close(); // this safely finalizes GZIP streams too
		}
		if (in != null) {
			in.close();
		}
	}

	public File getFile() {
		return file;
	}
}
