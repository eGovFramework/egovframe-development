/**
 * 
 */
package egovframework.bdev.imp.batch.wizards.com;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;
import org.jdom2.output.support.Walker;
import org.jdom2.util.NamespaceStack;

/**
 *  * JDOM으로 XML 생성시 하위 node의 xmlns를 초기화
 * 
 * @author 배치개발환경 개발팀 이도형
 * @since 2012.08.05
 * @version 1.0
 * @see <pre>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.08.05  이도형          최초 생성
 * 
 * 
 * </pre>
 */
public class CustomXMLOutputProcessor extends AbstractXMLOutputProcessor { 
    protected void printNamespace(Writer out, FormatStack fstack, Namespace ns) 
            throws java.io.IOException { 
        if (ns == Namespace.NO_NAMESPACE) { 
            return; 
        } else { 
        	final String prefix = ns.getPrefix();
    		final String uri = ns.getURI();

    		if(prefix.equals("aop") || prefix.equals("context") || prefix.equals("p") ||  prefix.equals("tx") 
					|| prefix.equals("util") || prefix.equals("xsi") || uri.endsWith("beans")) {
    			write(out, "\n\t xmlns");
    		} else {
    			write(out, " xmlns");
    		}
    		if (!prefix.equals("")) {
    			write(out, ":");
    			write(out, prefix);
    		}
    		write(out, "=\"");
    		write(out, uri);
    		write(out, "\"");
        } 
    }
    
    /* (non-Javadoc)
     * @see org.jdom2.output.support.AbstractXMLOutputProcessor#printElement(java.io.Writer, org.jdom2.output.support.FormatStack, org.jdom2.util.NamespaceStack, org.jdom2.Element)
     */
    @Override
    protected void printElement(Writer out, FormatStack fstack,
    		NamespaceStack nstack, Element element) throws IOException {
    	nstack.push(element);
		try {
			final List<Content> content = element.getContent();

			// Print the beginning of the tag plus attributes and any
			// necessary namespace declarations
			write(out, "<");

			write(out, element.getQualifiedName());
			
			// Print out attributes
			if (element.hasAttributes()) {
				for (final Attribute attribute : element.getAttributes()) {
					printAttribute(out, fstack, attribute);
				}
			}
			
			// Print the element's namespace, if appropriate
			for (final Namespace ns : nstack.addedForward()) {
				printNamespace(out, fstack, ns);
			}

			if (content.isEmpty()) {
				// Case content is empty
				if (fstack.isExpandEmptyElements()) {
					write(out, "></");
					write(out, element.getQualifiedName());
					write(out, ">");
				}
				else {
					write(out, " />");
				}
				// nothing more to do.
				return;
			}
			
			// OK, we have real content to push.
			fstack.push();
			try {

				// Check for xml:space and adjust format settings
				final String space = element.getAttributeValue("space",
						Namespace.XML_NAMESPACE);

				if ("default".equals(space)) {
					fstack.setTextMode(fstack.getDefaultMode());
				}
				else if ("preserve".equals(space)) {
					fstack.setTextMode(TextMode.PRESERVE);
				}
				
				// note we ensure the FStack is right before creating the walker
				Walker walker = buildWalker(fstack, content, true);
				
				if (!walker.hasNext()) {
					// the walker has formatted out whatever content we had
					if (fstack.isExpandEmptyElements()) {
						write(out, "></");
						write(out, element.getQualifiedName());
						write(out, ">");
					}
					else {
						write(out, " />");
					}
					// nothing more to do.
					return;
				}
				// we have some content.
				write(out, ">");
				if (!walker.isAllText()) {
					// we need to newline/indent
					textRaw(out, fstack.getPadBetween());
				}

				printContent(out, fstack, nstack, walker);

				if (!walker.isAllText()) {
					// we need to newline/indent
					textRaw(out, fstack.getPadLast());
				}
				write(out, "</");
				write(out, element.getQualifiedName());
				write(out, ">");
				
			} finally {
				fstack.pop();
			}
		} finally {
			nstack.pop();
		}
    }
    
    /* (non-Javadoc)
     * @see org.jdom2.output.support.AbstractXMLOutputProcessor#printAttribute(java.io.Writer, org.jdom2.output.support.FormatStack, org.jdom2.Attribute)
     */
    @Override
    protected void printAttribute(Writer out, FormatStack fstack,
    		Attribute attribute) throws IOException {
    	if (!attribute.isSpecified() && fstack.isSpecifiedAttributesOnly()) {
			return;
		}
		write(out, " ");
		write(out, attribute.getQualifiedName());
		write(out, "=");

		write(out, "\"");
		attributeEscapedEntitiesFilter(out, fstack, attribute.getValue());
		write(out, "\"");
    }
    
    protected void attributeEscapedEntitiesFilter(final Writer out,
			final FormatStack fstack, final String value) throws IOException {
		
		//if (!fstack.getEscapeOutput()) {
			// no escaping...
			write(out, value);
			//return;
		//}
		
		//write(out, Format.escapeAttribute(fstack.getEscapeStrategy(), value));

	}
} 