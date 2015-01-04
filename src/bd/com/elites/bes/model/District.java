package bd.com.elites.bes.model;

import bd.com.elites.bes.utils.Constants;

public class District {

	public int id = -1;
	public String district_name = "";
	public String district_name_bn = "";
	public String division = "";
	public String division_bn = "";

	public District(int id, String district_name, String division) {
		this.id = id;
		this.district_name = district_name;
		this.division = division;
	}

	public District(int id, String district_name, String district_name_bn,
			String division, String division_bn) {
		this.id = id;
		this.district_name = district_name;
		this.district_name_bn = district_name_bn;
		this.division = division;
		this.division_bn = division_bn;
	}

	public String getName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return district_name;

		case Constants.LANGUAGE.BANGLA:
			return district_name_bn;

		default:
			return district_name;
		}

	}
	
	

}
