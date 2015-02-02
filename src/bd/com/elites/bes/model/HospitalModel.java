package bd.com.elites.bes.model;

import bd.com.elites.bes.utils.AES;
import bd.com.elites.bes.utils.Constants;

public class HospitalModel {

	public int id = -1;
	public String station_name = "";
	public String station_name_bn = "";

	public String telephone_number = "";
	public String mobile_number = "";

	public String district = "";
	public String district_bn = "";

	public String second_level_region = "";
	public String second_level_region_bn = "";

	public String address = "";
	public String address_bn = "";

	public String latitude = "";
	public String longitude = "";
	private boolean isEncrypted = true;

	public HospitalModel(int id, String station_name, String telephone_number,
			String mobile_number, String district, String second_level_region,
			String address, String latitude, String longitude) {

		this.id = id;
		this.station_name = station_name;
		this.telephone_number = telephone_number;
		this.mobile_number = mobile_number;
		this.district = district;
		this.second_level_region = second_level_region;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;

	}

	public HospitalModel(int id, String station_name, String station_name_bn,
			String telephone_number, String mobile_number, String district,
			String district_bn, String second_level_region,
			String second_level_region_bn, String address, String address_bn,
			String latitude, String longitude) {

		this.id = id;
		this.station_name = station_name;
		this.station_name_bn = station_name_bn;
		this.telephone_number = telephone_number;
		this.mobile_number = mobile_number;
		this.district = district;
		this.district_bn = district_bn;
		this.second_level_region = second_level_region;
		this.second_level_region_bn = second_level_region_bn;
		this.address = address;
		this.address_bn = address_bn;
		this.latitude = latitude;
		this.longitude = longitude;

	}

	public String getStationName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return station_name;

		case Constants.LANGUAGE.BANGLA:
			return station_name_bn;

		default:
			return station_name;
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

	public void decrypt() {
		if (isEncrypted) {
			this.latitude = AES.decryptIt(latitude);
			this.longitude = AES.decryptIt(longitude);
			this.telephone_number = AES.decryptIt(telephone_number);
			this.mobile_number = AES.decryptIt(mobile_number);
			isEncrypted = false;
		}
	}

}
