/**
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Sep 20, 2003
 * Time: 7:15:08 PM
 * To change this template use Options | File Templates.
 */
package zz.utils.xml;

import org.jdom.Element;

import java.lang.reflect.Method;
import java.util.*;

import com.redcrea.ina.core.code.loader.Tag;
import zz.utils.ListMap;

/**
 * This abstract class can be used by singleton subclasses to automatize
 * the processing of a DOM {@link Element}.
 * <p>
 * Subclasses, in their constructor, define a set of bindings, that will permit
 * the {@link #process(Element) } method to dispatch the element to a competent method.
 */
public abstract class ElementProcessor
{
	private ListMap itsBindings = new ListMap ();

	private static final Class[] PARAMS = {Element.class};

	/**
	 * Binds a tag name to a method, identified by its name.
	 * The method should be public and take one {@link Element} argument.
	 */
	protected void addBinding (String aTagName, String aMethodName)
	{
		try
		{
			Method theMethod = getClass().getDeclaredMethod(aMethodName, PARAMS);
			theMethod.setAccessible(true);
			addBinding(new Binding(aTagName, theMethod));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void addBinding (Binding aBinding)
	{
		itsBindings.add (aBinding.getTagName(), aBinding);
	}

	protected void process (Element aElement)
	{
		for (Iterator theIterator = itsBindings.iterator(aElement.getName()); theIterator.hasNext();)
		{
			Binding theBinding = (Binding) theIterator.next();
			if (theBinding.process(this, aElement)) break;
		}
	}

	protected void processChildren (Element aElement)
	{
		for (Iterator theIterator = aElement.getChildren().iterator(); theIterator.hasNext();)
		{
			Element theElement = (Element) theIterator.next();
			process(theElement);
		}
	}

	protected static Element createNullElement ()
	{
		return new Element (Tag.NULL);
	}

	protected static boolean isNullElement (Element aElement)
	{
		return Tag.NULL.equals(aElement.getName());
	}

	protected static class Binding
	{
		private String itsTagName;
		private Map itsAttributesMap;
		private Method itsMethod;

		public Binding(String aTagName, Method aMethod)
		{
			itsTagName = aTagName;
			itsMethod = aMethod;
		}

		public String getTagName()
		{
			return itsTagName;
		}

		public void setAttributesMap(Map aAttributesMap)
		{
			itsAttributesMap = aAttributesMap;
		}

		public boolean process (ElementProcessor aScratch, Element aElement)
		{
			if (matches(aElement))
			{
				try
				{
					itsMethod.invoke(aScratch, new Object[] {aElement});
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return true;
			}
			else return false;
		}

		private boolean matches (Element aElement)
		{
			if (itsAttributesMap != null) for (Iterator theIterator = itsAttributesMap.entrySet().iterator(); theIterator.hasNext();)
			{
				Map.Entry theEntry = (Map.Entry) theIterator.next();

				String theAttributeName = (String) theEntry.getKey();
				String theRequiredAttributeValue = (String) theEntry.getValue();

				String theActualAttributeValue = aElement.getAttributeValue(theAttributeName);
				if (! theRequiredAttributeValue.equals(theActualAttributeValue)) return false;
			}
			return true;
		}
	}
}
