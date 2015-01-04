package bd.com.elites.bes.model;

import java.util.ArrayList;

import bd.com.elites.bes.utils.Constants;

public class Division {

	public String name = "";
	public String name_bn = "";
	public ArrayList<District> districts = new ArrayList<District>();

	public Division(String name) {
		this.name = name;
	}
	
	public Division(String name, String name_bn) {
		this.name = name;
		this.name_bn = name_bn;
	}

	public void addDistrict(District district) {
		districts.add(district);
	}
	
	public String getName(int language_preference) {

		switch (language_preference) {
		case Constants.LANGUAGE.ENGLISH:
			return name + " Division";

		case Constants.LANGUAGE.BANGLA:
			return name_bn + " বিভাগ";

		default:
			return name + " Division";
		}

	}

}
