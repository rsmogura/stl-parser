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

/**
 * Callback interface for parsing STL models.
 * <p>
 * This method uses {@code float[]} arrays to pass vertices, thus each vertex is
 * encoded as 3-elements array {@code {x,y,z}}. This notion allows fast and
 * memory efficient passing of vertices.
 * <p>
 * Unless needed, the caller should use {@link STLParserAbstractHandler} as it
 * provides default functionality.
 * </p>
 * 
 * @author Radek Smogura
 */
public interface STLParserHandler {

	/**
	 * Callback method used to notify caller that ASCII model is parsed.
	 * 
	 * @param name
	 *          the name of solid from file
	 */
	void beginAscii(String name);

	/**
	 * Callback method used to notify caller that binary model is parsed.
	 * 
	 * @param header
	 *          the header data from file, typically 80 bytes long
	 */
	void beginBinary(byte[] header);

	/**
	 * Callback method used to notify caller about triangles. This method may not
	 * be always called (ie. for ASCII models it will not be called).
	 * 
	 * @param count
	 *          number of triangles
	 */
	void numberOfTrinagles(int count);

	/**
	 * Notifies caller about facet begin. For ASCII files after call to this
	 * method one or more triangles can follow. For binary files only one triangle
	 * will follow. In both cases facet is ended with {@link #endFacet()}.
	 * 
	 * @param n
	 *          the normal vector coordinates
	 */
	void beginFacet(float[] n);

	/**
	 * Callback method used to notify void triangle(float v1[], float v2[], float
	 * v3[]);
	 * 
	 * /** Callback method used to notify about the end of facet
	 * 
	 */
	void endFacet();

	/**
	 * Callback method called for each triangle.
	 * 
	 * @param v1
	 *          the 1st vertex
	 * @param v2
	 *          the 2nd vertex
	 * @param v3
	 *          the 3rd vertex
	 */
	void triangle(float[] v1, float v2[], float v3[]);

	/**
	 * Callback method used to notify caller about end of solid (end of processing
	 * as well).
	 */
	void endSolid();
}
