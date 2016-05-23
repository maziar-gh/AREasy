package org.areasy.common.doclet.document.tags;

/*
 * Copyright (c) 2007-2016 AREasy Runtime
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

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;


/**
 * Implements the DD tag.
 *
 * @version $Id: TagDD.java,v 1.2 2008/05/14 09:36:47 swd\stefan.damian Exp $
 */
public class TagDD extends HtmlTag
{
	/**
	 * Stores a definition.
	 */
	private Paragraph definition = null;

	/**
	 * Creates a DD tag object.
	 *
	 * @param parent The parent HTML tag object.
	 * @param type   The type of this HTML tag.
	 */
	public TagDD(HtmlTag parent, int type)
	{
		super(parent, type);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.areasy.common.doclet.document.tags.HTMLTag#openTagElements()
	 */
	public Element[] openTagElements()
	{
		Element[] elements = new Element[1];
		Paragraph p1 = createParagraph("");
		p1.setIndentationLeft((float) 20.0);
		elements[0] = p1;
		return elements;
	}

}
