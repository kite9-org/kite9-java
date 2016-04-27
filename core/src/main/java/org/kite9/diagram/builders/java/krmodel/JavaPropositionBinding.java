/**
 * 
 */
package org.kite9.diagram.builders.java.krmodel;

import java.util.LinkedList;
import java.util.List;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.id.HasAddress;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.ComplexProposition;
import org.kite9.diagram.builders.krmodel.verb.TypedVerb;
import org.kite9.diagram.builders.krmodel.verb.Verb;

/**
 * Connections are often bound to a whole propostion. e.g. John Eats Cake is a
 * proposition. The connection between John and Cake (with label "eats")
 * represents this whole proposition.
 * 
 * So this 
 * 
 * @author robmoffat
 * 
 */
public class JavaPropositionBinding implements ComplexProposition {

	public JavaPropositionBinding(NounPart subject, Verb r) {
		super();
		this.subject = subject;
		this.verb = r;
		this.object = null;
	}
	
	public JavaPropositionBinding(NounPart subject, Verb r, NounPart object) {
		super();
		this.subject = subject;
		this.verb = r;
		this.object = object;
	}

	private final NounPart subject, object;

	private final Verb verb;

	public NounPart getSubject() {
		return subject;
	}

	public NounPart getObject() {
		return object;
	}

	public Verb getVerb() {
		return verb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((verb == null) ? 0 : verb.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JavaPropositionBinding other = (JavaPropositionBinding) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (verb == null) {
			if (other.verb != null)
				return false;
		} else if (!verb.equals(other.verb))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "["+getSubject()+","+getVerb()+","+getObject()+"]";
	}

	public Address getID() {
		Address out = new AddressImpl(true);
		return extend(out);
	}
	
	public String getStringID() {
		return getID().toString();
	}

	public Address extend(Address out) {
		return createRelationId(out, getSubject(), getObject(), getVerb());
	}

	public static Address createRelationId(Address out, HasAddress subject, HasAddress object, Verb v) {
		String subjectHash = hash(subject);
		String objectHash = hash(object);
		if (subjectHash != null) {
			out = out.extend("subject", ""+subjectHash);
		}
		
		if (v!=null) {
			out = v.extend(out);
		}
		
		if (object != null) {
			out = out.extend("object", ""+objectHash);
		}
		
		return out;
	}

	private static String hash(HasAddress addressable) {
		return addressable == null ? null : "#"+addressable.getID().hashCode();
	}

	public List<SimpleJavaRelationship> decompose() {
		SimpleJavaRelationship type1 = null;
		SimpleJavaRelationship type2 = null;
		
		if (verb instanceof TypedVerb) {
			type1 = createTypeRelationship(subject, ((TypedVerb) verb).getSubjectType());
			type2 = createTypeRelationship(object, ((TypedVerb) verb).getObjectType());
		}
		
		SimpleJavaRelationship main = createSimpleRelationship();
		List<SimpleJavaRelationship> out = new LinkedList<SimpleJavaRelationship>();
		addIfNotNull(out, main);		// most significant relationship first
		addIfNotNull(out, type1);
		addIfNotNull(out, type2);
		return out;
	}

	private SimpleJavaRelationship createTypeRelationship(final NounPart subject, final NounPart type) {
		if ((subject != null) && (type != null)) {	
			return new SimpleJavaRelationship(subject, JavaRelationships.HAS_TYPE, type);
		}
		
		return null;
	}

	private void addIfNotNull(List<SimpleJavaRelationship> out, SimpleJavaRelationship r) {
		if (r != null) {
			out.add(r);
		}
	}

	private SimpleJavaRelationship createSimpleRelationship() {
		if ((subject != null) && (object != null) && (verb != null)) {
			return new SimpleJavaRelationship(subject, verb, object);
		}
		
		return null;
	}

	public String getLabel() {
		return getSubject().getLabel()+" "+getVerb().getLabel()+
			(getObject() != null ? " "+getObject().getLabel():  "");
	}
	
	
}