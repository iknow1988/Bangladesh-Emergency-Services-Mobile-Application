package bd.com.elites.bes.model;

import bd.com.elites.bes.utils.Constants;

public class PoliceSubTwoUnit {

	public int id = -1;
	public String sub_two_unit_name = "";
	public String sub_two_unit_name_bn = "";
	public int parent_sub_unit_id = -1;

	public PoliceSubTwoUnit(int id, String sub_two_unit_name,
			int parent_sub_unit_id) {

		this.id = id;
		this.sub_two_unit_name = sub_two_unit_name;
		this.parent_sub_unit_id = parent_sub_unit_id;
	}
	
	public PoliceSubTwoUnit(int id, String sub_two_unit_name, String sub_two_unit_name_bn,
			int parent_sub_unit_id) {

		this.id = id;
		this.sub_two_unit_name = sub_two_unit_name;
		this.sub_two_unit_name_bn = sub_two_unit_name_bn;
		this.parent_sub_unit_id = parent_sub_unit_id;
	}
	
	public String getSubTwoUnitName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return sub_two_unit_name;

		case Constants.LANGUAGE.BANGLA:
			return sub_two_unit_name_bn;

		default:
			return sub_two_unit_name;
		}

	}

}
