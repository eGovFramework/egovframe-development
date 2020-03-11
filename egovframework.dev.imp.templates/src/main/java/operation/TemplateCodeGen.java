package operation;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class TemplateCodeGen {
	public String generate(Map<?, ?> map, String templateFile) throws Exception{
		StringWriter sw = new StringWriter();
		generate(map, templateFile, sw);
		
		System.out.println(sw.toString());
		
		return sw.toString();		
	}
	
	private void generate(Map<?, ?> map, String templateFile, Writer writer) throws Exception{
		String templateEncoding = "UTF-8";
		
        Properties p = new Properties() ;
        p.setProperty("resource.loader", "class");  
        p.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");  
         
        p.setProperty("file.resource.loader.cache", "false");
        p.setProperty("file.resource.loader.modificationCheckInterval", "0");

        Velocity.init(p);        

		VelocityContext context = new VelocityContext(map);
		
		Template template = null;

		try
		{
			template = Velocity.getTemplate(templateFile, templateEncoding);
		}
		catch( ResourceNotFoundException rnfe )
		{
			rnfe.printStackTrace();
		}
		catch( ParseErrorException pee )
		{
			// syntax error: problem parsing the template
		}
		catch( MethodInvocationException mie )
		{
			// something invoked in the template
			// threw an exception
		}
		catch( Exception e )
		{}

		template.merge( context, writer );
				
		
	}
	

	
}
