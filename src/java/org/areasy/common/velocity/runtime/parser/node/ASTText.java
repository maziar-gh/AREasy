package org.areasy.common.velocity.runtime.parser.node;

/*
 * Copyright (c) 2007-2015 AREasy Runtime
 *
 * This library, AREasy Runtime and API for BMC Remedy AR System, is free software ("Licensed Software");
 * you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * including but not limited to, the implied warranty of MERCHANTABILITY, NONINFRINGEMENT,
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */

import org.areasy.common.velocity.context.InternalContextAdapter;
import org.areasy.common.velocity.runtime.parser.Parser;
import org.areasy.common.velocity.runtime.parser.Token;

import java.io.IOException;
import java.io.Writer;

public class ASTText extends SimpleNode
{
	private char[] ctext;

	public ASTText(int id)
	{
		super(id);
	}

	public ASTText(Parser p, int id)
	{
		super(p, id);
	}

	/**
	 * Accept the visitor. *
	 */
	public Object jjtAccept(ParserVisitor visitor, Object data)
	{
		return visitor.visit(this, data);
	}

	public Object init(InternalContextAdapter context, Object data)
			throws Exception
	{
		Token t = getFirstToken();

		String text = NodeUtils.tokenLiteral(t);

		ctext = text.toCharArray();

		return data;
	}

	public boolean render(InternalContextAdapter context, Writer writer)
			throws IOException
	{
		writer.write(ctext);
		return true;
	}
}







