package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.kite9.diagram.primitives.AbstractDiagramElement;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.framework.common.Kite9ProcessingException;
import org.w3c.dom.Node;

/**
 * Eventually, this will be for all containers.  But for now, it's just a few things.
 * Originally, contents were held in a List.  This is not a list, but hopefully it will suffice
 * being an {@link Iterable}.
 * 
 * @author robmoffat
 *
 */
public class ContainerProperty<E extends DiagramElement> extends AbstractDiagramElement implements Iterable<E> {

	public ContainerProperty(String part, ADLDocument d) {
		super(part, d);
	}
	
	public ContainerProperty(String part, ADLDocument d, Collection<E> contents) {
		this(part, d);
		for (E e : contents) {
			appendChild(e);
		}
	}

	@Override
	protected Node newNode() {
		return new ContainerProperty<DiagramElement>(tagName, (ADLDocument) ownerDocument);
	}

	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		List<E> elems = new ArrayList<E>(getChildNodes().getLength());
		for (int i = 0; i < getChildNodes().getLength(); i++) {
			Node n = getChildNodes().item(i);
			if (n instanceof DiagramElement) {
				elems.add((E) n);
			}
		}
		
		return elems.iterator();
	}

	public int size() {
		return getChildElementCount();
	}

	public int compareTo(DiagramElement o) {
		throw new Kite9ProcessingException("not implemented");
	}

	public void clear() {
		while (getChildNodes().getLength() > 0) {
			removeChild(getChildNodes().item(0));
		}
	}
	
}
