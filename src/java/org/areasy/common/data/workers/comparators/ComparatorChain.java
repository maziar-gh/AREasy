package org.areasy.common.data.workers.comparators;

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

import java.io.Serializable;
import java.util.*;

/**
 * <p>A ComparatorChain is a Comparator that wraps one or
 * more Comparators in sequence.  The ComparatorChain
 * calls each Comparator in sequence until either 1)
 * any single Comparator returns a non-zero result
 * (and that result is then returned),
 * or 2) the ComparatorChain is exhausted (and zero is
 * returned).  This type of sorting is very similar
 * to multi-column sorting in SQL, and this class
 * allows Java classes to emulate that kind of behaviour
 * when sorting a List.</p>
 * <p/>
 * <p>To further facilitate SQL-like sorting, the order of
 * any single Comparator in the list can be reversed.</p>
 * <p/>
 * <p>Calling a method that adds new Comparators or
 * changes the ascend/descend sort <i>after compare(Object,
 * Object) has been called</i> will result in an
 * UnsupportedOperationException.  However, <i>take care</i>
 * to not alter the underlying List of Comparators
 * or the BitSet that defines the sort order.</p>
 * <p/>
 * <p>Instances of ComparatorChain are not synchronized.
 * The class is not thread-safe at construction time, but
 * it <i>is</i> thread-safe to perform multiple comparisons
 * after all the setup operations are complete.</p>
 *
 * @version $Id: ComparatorChain.java,v 1.2 2008/05/14 09:32:40 swd\stefan.damian Exp $
 */
public class ComparatorChain implements Comparator, Serializable
{
	/**
	 * The list of comparators in the chain.
	 */
	protected List comparatorChain = null;
	/**
	 * Order - false (clear) = ascend; true (set) = descend.
	 */
	protected BitSet orderingBits = null;
	/**
	 * Whether the chain has been "locked".
	 */
	protected boolean isLocked = false;

	/**
	 * Construct a ComparatorChain with no Comparators.
	 * You must add at least one Comparator before calling
	 * the compare(Object,Object) method, or an
	 * UnsupportedOperationException is thrown
	 */
	public ComparatorChain()
	{
		this(new ArrayList(), new BitSet());
	}

	/**
	 * Construct a ComparatorChain with a single Comparator,
	 * sorting in the forward order
	 *
	 * @param comparator First comparator in the Comparator chain
	 */
	public ComparatorChain(Comparator comparator)
	{
		this(comparator, false);
	}

	/**
	 * Construct a Comparator chain with a single Comparator,
	 * sorting in the given order
	 *
	 * @param comparator First Comparator in the ComparatorChain
	 * @param reverse    false = forward sort; true = reverse sort
	 */
	public ComparatorChain(Comparator comparator, boolean reverse)
	{
		comparatorChain = new ArrayList();
		comparatorChain.add(comparator);
		orderingBits = new BitSet(1);
		if (reverse == true)
		{
			orderingBits.set(0);
		}
	}

	/**
	 * Construct a ComparatorChain from the Comparators in the
	 * List.  All Comparators will default to the forward
	 * sort order.
	 *
	 * @param list List of Comparators
	 * @see #ComparatorChain(List,BitSet)
	 */
	public ComparatorChain(List list)
	{
		this(list, new BitSet(list.size()));
	}

	/**
	 * Construct a ComparatorChain from the Comparators in the
	 * given List.  The sort order of each column will be
	 * drawn from the given BitSet.  When determining the sort
	 * order for Comparator at index <i>i</i> in the List,
	 * the ComparatorChain will call BitSet.get(<i>i</i>).
	 * If that method returns <i>false</i>, the forward
	 * sort order is used; a return value of <i>true</i>
	 * indicates reverse sort order.
	 *
	 * @param list List of Comparators.  NOTE: This constructor does not perform a
	 *             defensive copy of the list
	 * @param bits Sort order for each Comparator.  Extra bits are ignored,
	 *             unless extra Comparators are added by another method.
	 */
	public ComparatorChain(List list, BitSet bits)
	{
		comparatorChain = list;
		orderingBits = bits;
	}

	/**
	 * Add a Comparator to the end of the chain using the
	 * forward sort order
	 *
	 * @param comparator Comparator with the forward sort order
	 */
	public void addComparator(Comparator comparator)
	{
		addComparator(comparator, false);
	}

	/**
	 * Add a Comparator to the end of the chain using the
	 * given sort order
	 *
	 * @param comparator Comparator to add to the end of the chain
	 * @param reverse    false = forward sort order; true = reverse sort order
	 */
	public void addComparator(Comparator comparator, boolean reverse)
	{
		checkLocked();

		comparatorChain.add(comparator);
		if (reverse == true)
		{
			orderingBits.set(comparatorChain.size() - 1);
		}
	}

	/**
	 * Replace the Comparator at the given index, maintaining
	 * the existing sort order.
	 *
	 * @param index      index of the Comparator to replace
	 * @param comparator Comparator to place at the given index
	 * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size()
	 */
	public void setComparator(int index, Comparator comparator)
			throws IndexOutOfBoundsException
	{
		setComparator(index, comparator, false);
	}

	/**
	 * Replace the Comparator at the given index in the
	 * ComparatorChain, using the given sort order
	 *
	 * @param index      index of the Comparator to replace
	 * @param comparator Comparator to set
	 * @param reverse    false = forward sort order; true = reverse sort order
	 */
	public void setComparator(int index, Comparator comparator, boolean reverse)
	{
		checkLocked();

		comparatorChain.set(index, comparator);
		if (reverse == true)
		{
			orderingBits.set(index);
		}
		else
		{
			orderingBits.clear(index);
		}
	}


	/**
	 * Change the sort order at the given index in the
	 * ComparatorChain to a forward sort.
	 *
	 * @param index Index of the ComparatorChain
	 */
	public void setForwardSort(int index)
	{
		checkLocked();
		orderingBits.clear(index);
	}

	/**
	 * Change the sort order at the given index in the
	 * ComparatorChain to a reverse sort.
	 *
	 * @param index Index of the ComparatorChain
	 */
	public void setReverseSort(int index)
	{
		checkLocked();
		orderingBits.set(index);
	}

	/**
	 * Number of Comparators in the current ComparatorChain.
	 *
	 * @return Comparator count
	 */
	public int size()
	{
		return comparatorChain.size();
	}

	/**
	 * Determine if modifications can still be made to the
	 * ComparatorChain.  ComparatorChains cannot be modified
	 * once they have performed a comparison.
	 *
	 * @return true = ComparatorChain cannot be modified; false =
	 *         ComparatorChain can still be modified.
	 */
	public boolean isLocked()
	{
		return isLocked;
	}

	// throw an exception if the ComparatorChain is locked
	private void checkLocked()
	{
		if (isLocked == true)
		{
			throw new UnsupportedOperationException("Comparator ordering cannot be changed after the first comparison is performed");
		}
	}

	private void checkChainIntegrity()
	{
		if (comparatorChain.size() == 0)
		{
			throw new UnsupportedOperationException("ComparatorChains must contain at least one Comparator");
		}
	}

	/**
	 * Perform comparisons on the Objects as per
	 * Comparator.compare(o1,o2).
	 *
	 * @param o1 the first object to compare
	 * @param o2 the second object to compare
	 * @return -1, 0, or 1
	 * @throws UnsupportedOperationException if the ComparatorChain does not contain at least one
	 *                                       Comparator
	 */
	public int compare(Object o1, Object o2) throws UnsupportedOperationException
	{
		if (isLocked == false)
		{
			checkChainIntegrity();
			isLocked = true;
		}

		// iterate over all comparators in the chain
		Iterator comparators = comparatorChain.iterator();
		for (int comparatorIndex = 0; comparators.hasNext(); ++comparatorIndex)
		{

			Comparator comparator = (Comparator) comparators.next();
			int retval = comparator.compare(o1, o2);
			if (retval != 0)
			{
				// invert the order if it is a reverse sort
				if (orderingBits.get(comparatorIndex) == true)
				{
					if (Integer.MIN_VALUE == retval)
					{
						retval = Integer.MAX_VALUE;
					}
					else
					{
						retval *= -1;
					}
				}

				return retval;
			}

		}

		// if comparators are exhausted, return 0
		return 0;
	}

	/**
	 * Implement a hash code for this comparator that is consistent with
	 * {@link #equals(Object) equals}.
	 *
	 * @return a suitable hash code
	 */
	public int hashCode()
	{
		int hash = 0;
		if (null != comparatorChain)
		{
			hash ^= comparatorChain.hashCode();
		}
		if (null != orderingBits)
		{
			hash ^= orderingBits.hashCode();
		}
		return hash;
	}

	/**
	 * Returns <code>true</code> iff <i>that</i> Object is
	 * is a {@link Comparator} whose ordering is known to be
	 * equivalent to mine.
	 * <p/>
	 * This implementation returns <code>true</code>
	 * iff <code><i>object</i>.{@link Object#getClass() getClass()}</code>
	 * equals <code>this.getClass()</code>, and the underlying
	 * comparators and order bits are equal.
	 * Subclasses may want to override this behavior to remain consistent
	 * with the {@link Comparator#equals(Object)} contract.
	 *
	 * @param object the object to compare with
	 * @return true if equal
	 */
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}
		else if (null == object)
		{
			return false;
		}
		else if (object.getClass().equals(this.getClass()))
		{
			ComparatorChain chain = (ComparatorChain) object;
			return ((null == orderingBits ? null == chain.orderingBits : orderingBits.equals(chain.orderingBits))
					&& (null == comparatorChain ? null == chain.comparatorChain : comparatorChain.equals(chain.comparatorChain)));
		}
		else
		{
			return false;
		}
	}

}
