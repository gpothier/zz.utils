/*
 * Created on 22-oct-2004
 */
package zz.utils;

/**
 * Simplifies the implementation of {@link Formatter}
 * @author gpothier
 */
public abstract class AbstractFormatter implements Formatter
{
	
	public String getPlainText(Object aObject)
	{
		return getText(aObject, false);
	}

	public String getHtmlText(Object aObject)
	{
		return getText(aObject, true);
	}

	protected abstract String getText (Object aObject, boolean aHtml);
	
	/**
	 * Permits to write text in either html or plain mode.
	 * @author gpothier
	 */
	protected static class HtmlWriter
	{
		private boolean itsHtml;
		
		private StringBuffer itsBuffer = new StringBuffer();
		
		public HtmlWriter(boolean aHtml)
		{
			itsHtml = aHtml;
			if (itsHtml) itsBuffer.append("<html>");
		}
		
		public void startFont(String aColor)
		{
			if (itsHtml) itsBuffer.append("<font color='"+aColor+"'>");
		}
		
		public void startFont(int aSize)
		{
			if (itsHtml) itsBuffer.append("<font size='"+aSize+"'>");
		}
		
		public void startFont(int aSize, String aColor)
		{
			if (itsHtml) itsBuffer.append("<font size='"+aSize+"' color='"+aColor+"'>");
		}
		
		public void endFont()
		{
			if (itsHtml) itsBuffer.append("</font>");
		}
		
		public void write (String aString)
		{
			itsBuffer.append(aString);
		}
		
		public void startBold()
		{
			if (itsHtml) itsBuffer.append("<b>");
		}
		
		public void endBold()
		{
			if (itsHtml) itsBuffer.append("</b>");
		}
		
		public void startItalic()
		{
			if (itsHtml) itsBuffer.append("<i>");
		}
		
		public void endItalic()
		{
			if (itsHtml) itsBuffer.append("</i>");
		}
		
		public void startUnderlined()
		{
			if (itsHtml) itsBuffer.append("<u>");
		}
		
		public void endUnderlined()
		{
			if (itsHtml) itsBuffer.append("</u>");
		}
		
		public String toString()
		{
			return itsBuffer.toString();
		}
	}
}
