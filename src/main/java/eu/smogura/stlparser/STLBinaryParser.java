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

import java.io.IOException;
import java.io.InputStream;

/**
 * The parser for binary models.
 * 
 * @author Radek Smogura
 * @since 1.0.0
 */
class STLBinaryParser implements STLParser {

	public static final int	BINARY_STL_HEADER_LENGTH	= 80;

	public static final int	FLOAT_LENGTH							= 4;

	public static final int	INTEGER_LENGTH						= 4;

	public static final int	VERTEX_LENGTH							= 12;

	public static final int	FACET_ATTRIBUTES					= 2;

	public static final int	NORMAL_LENGTH							= 12;

	public static final int	TRIANGLE_LENGTH						= 36;

	public static final int	FACET_LENGTH							= 50;

	STLBinaryParser() {
	}

	protected int readRequiredLength(InputStream in, byte buffer[], int requiredLength) throws IOException {
		int read = 0;
		int totalRead;
		for (totalRead = 0; (read = in.read(buffer, read, requiredLength)) != -1 && requiredLength > 0; totalRead += read)
			requiredLength -= read;

		return totalRead;
	}

	protected float[] bytesToVertex(byte buffer[], int offset) {
		float v[] = new float[3];
		v[0] = Float.intBitsToFloat(bytesToInt(buffer, offset + 0));
		v[1] = Float.intBitsToFloat(bytesToInt(buffer, offset + 4));
		v[2] = Float.intBitsToFloat(bytesToInt(buffer, offset + 8));
		return v;
	}

	protected int bytesToInt(byte buffer[], int offset) {
		return (buffer[offset + 3] & 0xff) << 24 | (buffer[offset + 2] & 0xff) << 16 | (buffer[offset + 1] & 0xff) << 8
				| buffer[offset + 0] & 0xff;
	}

	@Override
	public void parse(InputStream in, STLParserHandler handler) throws STLParserException {
		byte buffer[] = new byte[80];
		int readLen;
		try {
			readLen = readRequiredLength(in, buffer, 80);
		} catch (IOException ioe) {
			throw new STLParserException("STLP_0002: Unexpected IO exception while reading binary STL header.", ioe);
		}
		if (readLen != 80)
			throw new STLParserException((new StringBuilder())
					.append("STLP_0001: Unexpected end of stream while reading binary STL header, expected 80, read ")
					.append(readLen).append(".").toString());
		handler.beginBinary(buffer);
		try {
			readLen = readRequiredLength(in, buffer, 4);
		} catch (IOException ioe) {
			throw new STLParserException("STLP_0003: Unexpected IO exception while reading number of triangles.", ioe);
		}
		if (readLen != 4)
			throw new STLParserException((new StringBuilder())
					.append("STLP_0004: Unexpected end of stream while reading number of triangles., expected 4, read ")
					.append(readLen).append(".").toString());
		int numberOfTriangles = bytesToInt(buffer, 0);
		for (int t = 0; t < numberOfTriangles; t++) {
			try {
				readLen = readRequiredLength(in, buffer, 50);
			} catch (IOException ioe) {
				throw new STLParserException(
						(new StringBuilder()).append("STLP_0005: Unexpected IO exception while reading triangle's ").append(t + 1)
								.append(" of ").append(numberOfTriangles).append(" data").toString(),
						ioe);
			}
			if (readLen != 50)
				throw new STLParserException(
						(new StringBuilder()).append("STLP_0005: Unexpected end of stream while reading triangle's ").append(t + 1)
								.append(" of ").append(numberOfTriangles).append(" normal. Read ").append(readLen).append(", expected ")
								.append(50).append(".").toString());
			float normal[] = bytesToVertex(buffer, 0);
			handler.beginFacet(normal);
			float v1[] = bytesToVertex(buffer, 12);
			float v2[] = bytesToVertex(buffer, 24);
			float v3[] = bytesToVertex(buffer, 36);
			handler.triangle(v1, v2, v3);
			handler.endFacet();
		}

		handler.endSolid();
	}
}
