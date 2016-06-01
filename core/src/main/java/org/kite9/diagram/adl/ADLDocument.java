package org.kite9.diagram.adl;

import org.apache.batik.dom.GenericDocument;
import org.kite9.framework.serialization.ADLExtensibleDOMImplementation;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class ADLDocument extends GenericDocument {

	public ADLDocument() {
		this(new ADLExtensibleDOMImplementation());
	}

	public ADLDocument(ADLExtensibleDOMImplementation impl) {
		super(null, impl);
	}

	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		return ((ADLExtensibleDOMImplementation)implementation).createElementNS(this, namespaceURI, qualifiedName);
	}
	
	
	
}
