package org.areasy.common.data.type.iterator;

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

import org.areasy.common.data.type.OrderedMapIterator;
import org.areasy.common.data.type.Unmodifiable;

/**
 * Decorates an ordered map iterator such that it cannot be modified.
 *
 * @version $Id: UnmodifiableOrderedMapIterator.java,v 1.2 2008/05/14 09:32:40 swd\stefan.damian Exp $
 */
public final class UnmodifiableOrderedMapIterator implements OrderedMapIterator, Unmodifiable
{

	/**
	 * The iterator being decorated
	 */
	private OrderedMapIterator iterator;

	/**
	 * Decorates the specified iterator such that it cannot be modified.
	 *
	 * @param iterator the iterator to decorate
	 * @throws IllegalArgumentException if the iterator is null
	 */
	public static OrderedMapIterator decorate(OrderedMapIterator iterator)
	{
		if (iterator == null)
		{
			throw new IllegalArgumentException("OrderedMapIterator must not be null");
		}
		if (iterator instanceof Unmodifiable)
		{
			return iterator;
		}
		return new UnmodifiableOrderedMapIterator(iterator);
	}

	/**
	 * Constructor.
	 *
	 * @param iterator the iterator to decorate
	 */
	private UnmodifiableOrderedMapIterator(OrderedMapIterator iterator)
	{
		super();
		this.iterator = iterator;
	}

	public boolean hasNext()
	{
		return iterator.hasNext();
	}

	public Object next()
	{
		return iterator.next();
	}

	public boolean hasPrevious()
	{
		return iterator.hasPrevious();
	}

	public Object previous()
	{
		return iterator.previous();
	}

	public Object getKey()
	{
		return iterator.getKey();
	}

	public Object getValue()
	{
		return iterator.getValue();
	}

	public Object setValue(Object value)
	{
		throw new UnsupportedOperationException("setValue() is not supported");
	}

	public void remove()
	{
		throw new UnsupportedOperationException("remove() is not supported");
	}

}
