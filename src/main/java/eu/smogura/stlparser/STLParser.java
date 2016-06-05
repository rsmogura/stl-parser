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

import java.io.InputStream;

/**
 * The parser interface used to parse streams.
 * <p>
 * Parser is created with {@link }
 * </p>
 * <p>
 * This parser is streaming parser, it means that as parser moves forward with
 * {@code modelStream}, the caller is notified about events by callback methods
 * from {@link STLParserHandler}. Thus caller should extend
 * {@link STLParserAbstractHandler} or implement {@link STLParser} to get
 * callback notifications when file is parsed.
 * </p>
 * 
 * @author Radek Smogura
 * @since 1.0.0
 */
public interface STLParser {

	void parse(InputStream inputstream, STLParserHandler stlparserhandler) throws STLParserException;
}
