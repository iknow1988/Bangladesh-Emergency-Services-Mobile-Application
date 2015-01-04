package bd.com.elites.bes.model;

import bd.com.elites.bes.utils.Constants;

public class PoliceSubUnit {

	public int id = -1;
	public String sub_unit_name = "";
	public String sub_unit_name_bn = "";
	public int parent_unit_id = -1;

	public PoliceSubUnit(int id, String sub_unit_name, int parent_unit_id) {

		this.id = id;
		this.sub_unit_name = sub_unit_name;
		this.parent_unit_id = parent_unit_id;

	}

	public PoliceSubUnit(int id, String sub_unit_name, String sub_unit_name_bn,
			int parent_unit_id) {

		this.id = id;
		this.sub_unit_name = sub_unit_name;
		this.sub_unit_name_bn = sub_unit_name_bn;
		this.parent_unit_id = parent_unit_id;

	}

	public String getSubUnitName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return sub_unit_name;

		case Constants.LANGUAGE.BANGLA:
			return sub_unit_name_bn;

		default:
			return sub_unit_name;
		}

	}

}
