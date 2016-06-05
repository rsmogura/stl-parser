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
 * The ASCII implementation of {@link STLParser} capable to handle ASCII files
 * 
 * @author Radek Smogura
 * @since 1.0.0
 */
class STLAsciiParser implements STLParser {

	public static final String	SOLID_HEADER	= "solid";

	public static final String	FACET_NORMAL	= "facet normal";

	public static final String	OUTER_LOOP		= "outer loop";

	public static final String	VERTEX				= "vertex";

	public static final String	END_LOOP			= "endloop";

	public static final String	END_FACET			= "endfacet";

	public static final String	END_SOLID			= "endsolid";

	STLAsciiParser() {
	}

	protected void ensureNotEOF(String line) throws STLParserException {
		if (line == null) {
			throw new STLParserException("STLP_0012: Unexpected end of file while parsing ASCII STL.");
		} else {
			return;
		}
	}

	protected String readNonEmptyLine(LineNumberReader lnr) throws STLParserException {
		String line;
		do {
			try {
				line = lnr.readLine().trim();
			} catch (IOException ioe) {
				throw new STLParserException(
						(new StringBuilder()).append("STLP_0008: Unexpected IO exception while parsing ASCII STL at line ")
								.append(lnr.getLineNumber()).append(".").toString(),
						ioe);
			}
		} while (line != null && line.isEmpty());
		return line;
	}

	protected float[] readVector(String str) throws NumberFormatException {
		float result[] = new float[3];
		int space = -1;
		int i;
		for (i = 0; i < result.length; i++) {
			str = str.substring(space + 1).trim();
			space = str.indexOf(' ');
			if (space == -1) {
				space = str.length();
			}
			result[i] = Float.parseFloat(str.substring(0, space));
		}

		if (i != result.length) {
			throw new NumberFormatException("STLP_0011: Could not read all vector values.");
		} else {
			return result;
		}
	}

	protected boolean readFacet(LineNumberReader lnr, STLParserHandler handler) throws STLParserException {
		String line = readNonEmptyLine(lnr);
		ensureNotEOF(line);
		if (line.startsWith("endsolid")) {
			return false;
		}
		if (!line.startsWith("facet normal")) {
			throw new STLParserException("STLP_0009: Wrong format of ASCII STL file: facet line has wrong format.");
		}
		float normal[];
		try {
			normal = readVector(line.substring("facet normal".length() + 1));
		} catch (NumberFormatException nfe) {
			throw new STLParserException((new StringBuilder()).append("STLP_0010: Wrong format of ASCII STL file at line ")
					.append(lnr.getLineNumber()).append(" while reading facet normal.").toString(), nfe);
		}
		handler.beginFacet(normal);
		line = readNonEmptyLine(lnr);
		ensureNotEOF(line);
		if (!line.startsWith("outer loop")) {
			throw new STLParserException((new StringBuilder()).append("STLP_0014: Wrong format of ASCII STL file at line ")
					.append(lnr.getLineNumber()).append(" expected 'outer loop'.").toString());
		}
		float verts[][] = new float[3][];
		int vIdx;
		for (vIdx = 0; vIdx < verts.length; vIdx++) {
			line = readNonEmptyLine(lnr);
			ensureNotEOF(line);
			if (!line.startsWith("vertex")) {
				throw new STLParserException((new StringBuilder()).append("STLP_0017: Wrong format of ASCII STL file at line ")
						.append(lnr.getLineNumber()).append(" expected 'vertex'.").toString());
			}
			try {
				verts[vIdx] = readVector(line.substring("vertex".length() + 1));
			} catch (NumberFormatException nfe) {
				throw new STLParserException((new StringBuilder()).append("STLP_0018: Wrong format of ASCII STL file at line ")
						.append(lnr.getLineNumber()).append(" while reading vertex data.").toString(), nfe);
			}
		}

		if (vIdx != verts.length) {
			throw new NumberFormatException((new StringBuilder())
					.append("STLP_0019: Wrong format of ASCII STL file: could not read all vertexes, at line")
					.append(lnr.getLineNumber()).append(".").toString());
		}
		handler.triangle(verts[0], verts[1], verts[2]);
		line = readNonEmptyLine(lnr);
		ensureNotEOF(line);
		if (!line.startsWith("endloop")) {
			throw new STLParserException((new StringBuilder()).append("STLP_0015: Wrong format of ASCII STL file at line ")
					.append(lnr.getLineNumber()).append(" expected 'endloop'.").toString());
		}
		line = readNonEmptyLine(lnr);
		ensureNotEOF(line);
		if (!line.startsWith("endfacet")) {
			throw new STLParserException((new StringBuilder()).append("STLP_0016: Wrong format of ASCII STL file at line ")
					.append(lnr.getLineNumber()).append(" expected 'endfacet'.").toString());
		} else {
			handler.endFacet();
			return true;
		}
	}

	public void parse(InputStream inStream, STLParserHandler handler) throws STLParserException {
		java.io.Reader in = new InputStreamReader(inStream);
		LineNumberReader lnr = new LineNumberReader(in);
		String line;
		try {
			line = lnr.readLine().trim();
		} catch (IOException ioe) {
			throw new STLParserException("STLP_0006: Unexpected IO exception while reading ASCII STL header.", ioe);
		}
		if (line == null || !line.startsWith("solid")) {
			throw new STLParserException(
					"STLP_0007: Unexpected end of stream or wrong STL header while reading ASCII STL header.");
		}
		handler.beginAscii(line.substring("solid".length() + 1));
		while (readFacet(lnr, handler))
			;
		handler.endSolid();
	}
}
