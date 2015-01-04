package bd.com.elites.bes.model;

import bd.com.elites.bes.utils.Constants;

public class SecondLevelRegion {

	public int id = -1;
	public String region_name = "";
	public String region_name_bn = "";
	public String parent_district = "";
	public String parent_district_bn = "";
	public int region_type = -1;

	public SecondLevelRegion(int id, String region_name,
			String parent_district, int region_type) {

		this.id = id;
		this.region_name = region_name;
		this.parent_district = parent_district;
		this.region_type = region_type;

	}

	public SecondLevelRegion(int id, String region_name, String region_name_bn,
			String parent_district, String parent_district_bn, int region_type) {

		this.id = id;
		this.region_name = region_name;
		this.parent_district = parent_district;
		this.region_name_bn = region_name_bn;
		this.parent_district_bn = parent_district_bn;
		this.region_type = region_type;

	}

	public String getRegionName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return region_name;

		case Constants.LANGUAGE.BANGLA:
			return region_name_bn;

		default:
			return region_name;
		}

	}

	public String getParentDistrictName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return parent_district;

		case Constants.LANGUAGE.BANGLA:
			return parent_district_bn;

		default:
			return parent_district;
		}

	}

}
