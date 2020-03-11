package model;

import utils.NamingUtils;

public class NameCasing {
	private String name;
	private String ucName;
	private String lcName;
	private String ccName;
	private String pcName;

	public NameCasing(String name){
		setName(name);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		setUcName(name.toUpperCase());
		setLcName(name.toLowerCase());
		setCcName(NamingUtils.convertUnderscoreNameToCamelcase(name));
		setPcName(NamingUtils.convertCamelcaseToPascalcase(getCcName()));
	}
	public String getUcName() {
		return ucName;
	}
	public void setUcName(String uppercaseName) {
		this.ucName = uppercaseName;
	}
	public String getLcName() {
		return lcName;
	}
	public void setLcName(String lowercaseName) {
		this.lcName = lowercaseName;
	}
	public String getCcName() {
		return ccName;
	}
	public void setCcName(String camelcaseName) {
		this.ccName = camelcaseName;
	}
	public String getPcName() {
		return pcName;
	}
	public void setPcName(String pascalcaseName) {
		this.pcName = pascalcaseName;
	}


}
