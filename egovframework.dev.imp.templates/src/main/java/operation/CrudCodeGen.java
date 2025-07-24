package operation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import egovframework.dev.imp.codegen.template.model.DataModelContext;

public class CrudCodeGen {
	public String generate(DataModelContext dataModel, String templateFile, CrudWizard crudWizard) {
		StringWriter sw = new StringWriter();
		generate(dataModel, templateFile, sw, crudWizard);

		System.out.println(sw.toString());

		return sw.toString();
	}

	private void generate(DataModelContext dataModel, String templateFile, Writer writer, CrudWizard crudWizard) {
		String templateEncoding = "UTF-8";

		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		p.setProperty("file.resource.loader.cache", "false");
		p.setProperty("file.resource.loader.modificationCheckInterval", "0");

		try {
			Velocity.init(p);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		VelocityContext context = new VelocityContext();

//		context.put("package", dataModel.getPackageName());
//		context.put("entity", dataModel.getEntity());
//		context.put("attributes", dataModel.getAttributes());
//		context.put("primaryKeys", dataModel.getPrimaryKeys());
//		context.put("createDate", dataModel.getCreateDate());
//		context.put("author", dataModel.getAuthor());

		context.put("model", dataModel);

		context.put("author", crudWizard.getAuthor());
		context.put("createDate", crudWizard.getCreateDate());
		context.put("mapperFolder", crudWizard.getMapperFolder());
		context.put("daoPackage", crudWizard.getDaoPackage());
		context.put("mapperPackage", crudWizard.getMapperPackage());
		context.put("voPackage", crudWizard.getVoPackage());
		context.put("servicePackage", crudWizard.getServicePackage());
		context.put("implPackage", crudWizard.getImplPackage());
		context.put("controllerPackage", crudWizard.getControllerPackage());
		context.put("jspFolder", crudWizard.getJspFolder());

		Template template = null;

		try {
			template = Velocity.getTemplate(templateFile, templateEncoding);
		} catch (ResourceNotFoundException rnfe) {
			rnfe.printStackTrace();
		} catch (ParseErrorException pee) {
			// syntax error: problem parsing the template
		} catch (MethodInvocationException mie) {
			// something invoked in the template
			// threw an exception
		} catch (Exception e) {
		}

		try {
			template.merge(context, writer);
		} catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException | IOException e) {
			throw new IllegalArgumentException(e);
		}

	}

	public static class CrudWizard {

		private String author;

		private String createDate;

		private String mapperFolder;

		private String daoPackage;

		private String mapperPackage;

		private String voPackage;

		private String servicePackage;

		private String implPackage;

		private String controllerPackage;

		private String jspFolder;

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getMapperFolder() {
			return mapperFolder;
		}

		public void setMapperFolder(String mapperFolder) {
			this.mapperFolder = mapperFolder;
		}

		public String getDaoPackage() {
			return daoPackage;
		}

		public void setDaoPackage(String daoPackage) {
			this.daoPackage = daoPackage;
		}

		public String getMapperPackage() {
			return mapperPackage;
		}

		public void setMapperPackage(String mapperPackage) {
			this.mapperPackage = mapperPackage;
		}

		public String getVoPackage() {
			return voPackage;
		}

		public void setVoPackage(String voPackage) {
			this.voPackage = voPackage;
		}

		public String getServicePackage() {
			return servicePackage;
		}

		public void setServicePackage(String servicePackage) {
			this.servicePackage = servicePackage;
		}

		public String getImplPackage() {
			return implPackage;
		}

		public void setImplPackage(String implPackage) {
			this.implPackage = implPackage;
		}

		public String getControllerPackage() {
			return controllerPackage;
		}

		public void setControllerPackage(String controllerPackage) {
			this.controllerPackage = controllerPackage;
		}

		public String getJspFolder() {
			return jspFolder;
		}

		public void setJspFolder(String jspFolder) {
			this.jspFolder = jspFolder;
		}

	}

}
