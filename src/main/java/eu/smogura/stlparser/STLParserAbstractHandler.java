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
 * The abstract no-op handler. Should be extended with methods needed by caller.
 * 
 * @author Radek Smogura
 * @since 1.0.0
 */
public class STLParserAbstractHandler implements STLParserHandler {

	public STLParserAbstractHandler() {
	}

	@Override
	public void beginAscii(String name) {
	}

	@Override
	public void beginBinary(byte[] header) {
	}

	@Override
	public void numberOfTrinagles(int count) {
	}

	@Override
	public void beginFacet(float[] n) {
	}

	@Override
	public void endFacet() {
	}

	@Override
	public void triangle(float[] v1, float[] v2, float[] v3) {
	}

	@Override
	public void endSolid() {
	}
}
