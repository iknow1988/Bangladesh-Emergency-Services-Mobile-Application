package bd.com.elites.bes.map;

import android.location.Location;
import bd.com.elites.bes.utils.AES;
import bd.com.elites.bes.utils.Constants;

import com.google.android.gms.maps.model.LatLng;

public class StationInfoForMap {
	public static final int POLICE_STATION = 1, FIRE_STATION = 2, HOSPITAL = 3;
	private String Name;
	private String Name_bn;
	private int type;
	private Location location;
	private LatLng latlng;
	private String phoneNo1;
	private String phoneNo2;
	private String address;
	private String address_bn;

	public String district = "";
	public String district_bn = "";
	public String second_level_region = "";
	public String second_level_region_bn = "";

	public StationInfoForMap(String name, String name_bn, int type, String lat,
			String lng, String tnt, String mobile, String address,
			String address_bn, String district, String distric_bn,
			String second_level_region, String second_level_region_bn) {
		this.Name = isNull(name) ? "" : name;
		this.Name_bn = isNull(name_bn) ? "" : name_bn;
		String latitude="-1.0",longitude="-1.0";
		try {
			if(lat!=null)
			{
				latitude=AES.decryptIt(lat);
				longitude=AES.decryptIt(lng);
			}
			Double lati = Double.parseDouble(latitude);
			Double lngi = Double.parseDouble(longitude);
			setLocation(name, lati, lngi);
		} catch (Exception e) {
			setLocation(name, -1.0, -1.0);
		}
		this.phoneNo1 = isNull(tnt) ? "" : AES.decryptIt(tnt);
		this.phoneNo2 = isNull(mobile) ? "" : AES.decryptIt(mobile);
		this.type = isNull(type) ? -1 : type;
		this.district = isNull(district) ? "" : district;
		this.district_bn = isNull(distric_bn) ? "" : distric_bn;
		this.second_level_region = isNull(second_level_region) ? ""
				: second_level_region;
		this.second_level_region_bn = isNull(second_level_region_bn) ? ""
				: second_level_region_bn;
		this.setAddress(address);
		this.setAddress_bn(address_bn);
	}

	private boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		return false;
	}

	public String getName(int language_preference) {
		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return Name;

		case Constants.LANGUAGE.BANGLA:
			return Name_bn;

		default:
			return Name;
		}

	}

	public void setName(String name) {
		Name = name;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(String name, double lat, double lng) {

		this.location = new Location(isNull(name) ? "" : name);
		this.location.setLatitude(isNull(lat) ? -1 : lat);
		this.location.setLongitude(isNull(lng) ? -1 : lng);
		this.latlng = new LatLng(lat, lng);
	}

	public LatLng getLatlng() {
		return latlng;
	}

	public String getPhoneNo1() {
		return phoneNo1;
	}

	public void setPhoneNo1(String phoneNo1) {
		this.phoneNo1 = phoneNo1;
	}

	public String getPhoneNo2() {
		return phoneNo2;
	}

	public void setPhoneNo2(String phoneNo2) {
		this.phoneNo2 = phoneNo2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = isNull(address) ? "" : address;
	}

	public String getAddress_bn() {

		return address_bn;
	}

	public void setAddress_bn(String address_bn) {
		this.address_bn = address_bn;
	}

	public String getAddress(int language_preference) {

		String combinedAddress = "";
		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			combinedAddress = address.trim();
			if (combinedAddress.length() == 0) {
				combinedAddress += second_level_region;
				if (combinedAddress.length() > 0)
					combinedAddress += ", ";
				combinedAddress += district;
			}
			return combinedAddress;
		case Constants.LANGUAGE.BANGLA:
			combinedAddress = address_bn.trim();
			if (combinedAddress.length() == 0) {
				combinedAddress += second_level_region_bn;
				if (combinedAddress.length() > 0)
					combinedAddress += ", ";
				combinedAddress += district_bn;
			}
			return combinedAddress;

		default:
			return combinedAddress;
		}

	}

}
