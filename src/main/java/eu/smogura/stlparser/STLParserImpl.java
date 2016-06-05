/*
 * Copyright (c) 2016 Radek Smogura <mail@smogura.eu>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package eu.smogura.stlparser;

import java.io.*;

/**
 * Implementation of parser
 * 
 * @author Radek Smogura
 * @since 1.0.0
 */
class STLParserImpl implements STLParser {

	/** Holds SOLID used to identify is STL is binary or ASCII model. */
	private static final String	SOLID	= "solid";

	private STLAsciiParser			asciiParser;

	private STLBinaryParser			binaryParser;

	public STLParserImpl() {
		asciiParser = new STLAsciiParser();
		binaryParser = new STLBinaryParser();
	}

	@Override
	public void parse(InputStream inStream, STLParserHandler handler) throws STLParserException {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			// Clone input stream for reading begin of file, we can't prove how much
			// bytes will be read from stream by reader (in theory) , so
			// InputStream.mark can be used. Additionaly, not all streams support
			// marking.
			CloneInputStream stIn = new CloneInputStream(inStream, buffer);
			Reader reader = new InputStreamReader(stIn);

			// Read buffer
			// TODO Is there Apache Commons method for this?
			int pos, read;
			char buff[] = new char[SOLID.length()];
			for (pos = 0; pos < buff.length && (read = reader.read(buff, pos, buff.length - pos)) != -1; pos += read)
				;
			if (pos != buff.length)
				throw new STLParserException("STLP_0021: Unexpected end of stream while trying to determine STL file format.");

			// Construct sequential stream from read data and rest of stream
			SequenceInputStream sin = new SequenceInputStream(new ByteArrayInputStream(buffer.toByteArray()), inStream);
			if ((new String(buff)).startsWith(SOLID)) {
				asciiParser.parse(sin, handler);
			} else {
				binaryParser.parse(sin, handler);
			}
		} catch (IOException ioe) {
			throw new STLParserException("STLP_0020: Unexpected IO exception while trying to determine STL file format.",
					ioe);
		}
	}

	/**
	 * This stream writes all read data to provided {@link OutputStream}.
	 * 
	 */
	private class CloneInputStream extends FilterInputStream {

		/** Holds output stream */
		private OutputStream outputStream;

		public CloneInputStream(final InputStream in, final OutputStream outputStream) {
			super(in);
			setOutputStream(outputStream);
		}

		@Override
		public int read() throws IOException {
			int result = super.read();
			if (result != -1)
				outputStream.write(result);
			return result;
		}

		@Override
		public int read(byte b[]) throws IOException {
			return read(b, 0, b.length);
		}

		@Override
		public int read(byte b[], int off, int len) throws IOException {
			int result = super.read(b, off, len);
			if (result != -1)
				outputStream.write(b, off, result);
			return result;
		}

		/**
		 * Returns output stream used to store read data.
		 * 
		 * @return output stream used to store read data.
		 */
		public OutputStream getOutputStream() {
			return outputStream;
		}

		/**
		 * Sets output stream used to store read data.
		 * 
		 * @param outputStream
		 *          the new output stream used to store data
		 */
		public void setOutputStream(OutputStream outputStream) {
			if (outputStream == null)
				throw new IllegalArgumentException("Parameter output stream should not be null");
			this.outputStream = outputStream;
		}

	}
}
