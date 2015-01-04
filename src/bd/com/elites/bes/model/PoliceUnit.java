package bd.com.elites.bes.model;

import bd.com.elites.bes.utils.Constants;

public class PoliceUnit {

	public int id = -1;
	public String unit_name = "";
	public String unit_name_bn = "";

	public PoliceUnit(int id, String unit_name) {
		this.id = id;
		this.unit_name = unit_name;
	}

	public PoliceUnit(int id, String unit_name, String unit_name_bn) {
		this.id = id;
		this.unit_name = unit_name;
		this.unit_name_bn = unit_name_bn;
	}
	
	public String getUnitName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return unit_name;

		case Constants.LANGUAGE.BANGLA:
			return unit_name_bn;

		default:
			return unit_name;
		}

	}

}
