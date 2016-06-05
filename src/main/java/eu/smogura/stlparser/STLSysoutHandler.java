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

import java.io.PrintStream;

/**
 * The handler used to debug STL models.
 * 
 * @author Radek Smogura
 * @since 1.0.0
 */
public class STLSysoutHandler extends STLParserAbstractHandler {

	// FIXME Convert me to Java 8 String.format and friends
	public STLSysoutHandler() {
	}

	@Override
	public void beginBinary(byte header[]) {
		System.out.println("Binary STL started");
	}

	@Override
	public void beginFacet(float normal[]) {
		System.out.println("Facet");
		System.out.println(" Normal");
		System.out.println((new StringBuilder()).append("    [").append(normal[0]).append(",").append(normal[1]).append(",")
				.append(normal[2]).append("]").toString());
	}

	@Override
	public void triangle(float v1[], float v2[], float v3[]) {
		System.out.println(" Traingle");
		System.out.println((new StringBuilder()).append("    [").append(v1[0]).append(",").append(v1[1]).append(",")
				.append(v1[2]).append("]").toString());
		System.out.println((new StringBuilder()).append("    [").append(v2[0]).append(",").append(v2[1]).append(",")
				.append(v2[2]).append("]").toString());
		System.out.println((new StringBuilder()).append("    [").append(v3[0]).append(",").append(v3[1]).append(",")
				.append(v3[2]).append("]").toString());
	}
}
