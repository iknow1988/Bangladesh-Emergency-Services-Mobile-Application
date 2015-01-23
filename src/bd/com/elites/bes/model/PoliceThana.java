package bd.com.elites.bes.model;

import bd.com.elites.bes.utils.AES;
import bd.com.elites.bes.utils.Constants;

public class PoliceThana {

	public int id = -1;
	public String thana_name = "";
	public String thana_name_bn = "";
	public String phone_number_1 = "";
	public String phone_number_2 = "";
	public String district = "";
	public String district_bn = "";
	public String second_level_region = "";
	public String second_level_region_bn = "";
	public String address = "";
	public String address_bn = "";
	public String latitude = "";
	public String longitude = "";

	public PoliceThana(int id, String thana_name, String phone_number_1,
			String phone_number_2, String district, String second_level_region,
			String address, String latitude, String longitude) {

		this.id = id;
		this.thana_name = thana_name;
		this.phone_number_1 = AES.decryptIt(phone_number_1);
		this.phone_number_2 = AES.decryptIt(phone_number_2);
		this.district = district;
		this.second_level_region = second_level_region;
		this.address = address;
		this.latitude = AES.decryptIt(latitude);
		this.longitude = AES.decryptIt(longitude);
	}

	public PoliceThana(int id, String thana_name, String thana_name_bn,
			String phone_number_1, String phone_number_2, String district,
			String district_bn, String second_level_region,
			String second_level_region_bn, String address, String address_bn,
			String latitude, String longitude) {

		this.id = id;
		this.thana_name = thana_name;
		this.thana_name_bn = thana_name_bn;
		this.phone_number_1 = AES.decryptIt(phone_number_1);
		this.phone_number_2 = AES.decryptIt(phone_number_2);
		this.district = district;
		this.district_bn = district_bn;
		this.second_level_region = second_level_region;
		this.second_level_region_bn = second_level_region_bn;
		this.address = address;
		this.address_bn = address_bn;
		this.latitude = AES.decryptIt(latitude);
		this.longitude = AES.decryptIt(longitude);
	}

	public String getName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return thana_name;

		case Constants.LANGUAGE.BANGLA:
			return thana_name_bn;

		default:
			return thana_name;
		}

	}

	public String getDistrictName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return district;

		case Constants.LANGUAGE.BANGLA:
			return district_bn;

		default:
			return district;
		}

	}

	public String getSecondLevelRegionName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return second_level_region;

		case Constants.LANGUAGE.BANGLA:
			return second_level_region_bn;

		default:
			return second_level_region;
		}

	}

	public String getAddress(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return address;

		case Constants.LANGUAGE.BANGLA:
			return address_bn;

		default:
			return address;
		}

	}

	public String getQuickContactNumber() {

		String quick_num = "";
		if (!phone_number_1.trim().equals("")) {
			quick_num = phone_number_1.trim();
		} else {
			quick_num = phone_number_2.trim();
		}

		return quick_num;
	}

	public String getCombinedAddress(int language_preference) {
		String combinedAddress = getAddress(language_preference).trim();
		if (combinedAddress.length() == 0) {
			combinedAddress += getSecondLevelRegionName(language_preference);
			if (combinedAddress.length() > 0)
				combinedAddress += ",";
			combinedAddress += getDistrictName(language_preference);
		}
		return combinedAddress;

	}
}
