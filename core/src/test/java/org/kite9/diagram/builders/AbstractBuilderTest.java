package org.kite9.diagram.builders;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import junit.framework.Assert;

import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.primitives.AbstractIdentifiableDiagramElement;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.alias.PropertyAliaser;
import org.kite9.framework.common.HelpMethods;
import org.kite9.framework.common.StackHelp;
import org.kite9.framework.model.ProjectModelImpl;
import org.kite9.framework.serialization.XMLHelper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class AbstractBuilderTest extends HelpMethods {
	
	public void renderXML(String d) throws IOException {
		
		Method m = StackHelp.getAnnotatedMethod(Test.class);
		String xml = d;
		DiagramTestingEngine.writeOutput(this.getClass(), m.getName(), "diagram.xml", xml);
		
		if (!DiagramTestingEngine.checkOutputs(this.getClass(), m.getName(), "diagram.xml")) {
			Assert.fail("No comparison with correct output");
		}
		
	}

	public void renderDiagram(Diagram d) throws IOException {
		Method m = StackHelp.getAnnotatedMethod(Test.class);

		XMLHelper helper = new XMLHelper();
		String xml = helper.toXML(d);
		DiagramTestingEngine.writeOutput(this.getClass(), m.getName(), "diagram.xml", xml);
		
		System.out.println(xml);

		try {
			// validate the xml against the schema
			InputSource is = new InputSource(new StringReader(xml));

			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

			// load a WXS schema, represented by a Schema instance
			Source schemaFile = new StreamSource(Diagram.class.getResourceAsStream("/adl_1.0.xsd"));
			Schema schema = factory.newSchema(schemaFile);

			Validator validator = schema.newValidator();

			SAXSource source = new SAXSource(is);
			validator.validate(source);
			
			helper.fromXML(xml);
		} catch (SAXParseException e) {
			e.printStackTrace();
			Assert.fail("Line: "+ e.getLineNumber()+" Failed validation: " + e.getMessage() + "\n" + xml);
		} catch (SAXException e) {
			e.printStackTrace();
			Assert.fail("Failed validation: " + e.getMessage() + "\n" + xml);
		} 

		if (!DiagramTestingEngine.checkOutputs(this.getClass(), m.getName(), "diagram.xml")) {
			Assert.fail("No comparison with correct output");
		}

	}

	protected ProjectModelImpl pmi = new ProjectModelImpl();

	public DiagramBuilder createBuilder() {
		AbstractIdentifiableDiagramElement.resetCounter();
		Method m = StackHelp.getKite9Item();
		Aliaser a = new PropertyAliaser();

		return new DiagramBuilder(m, pmi);
	}
	
	public XMLRepresentationBuilder createXMLBuilder() {
		AbstractIdentifiableDiagramElement.resetCounter();
		Method m = StackHelp.getKite9Item();
		Aliaser a = new PropertyAliaser();

		return new XMLRepresentationBuilder(m, pmi);
	}


	protected String convertClassName(Class<?> c) {
		return c.getName().replace(".", "/");
	}

	protected String convertPackageName(Package p) {
		return p.getName().replace(".", "/");
	}

}
