package bd.com.elites.bes.utils;

import java.util.ArrayList;

import bd.com.elites.bes.model.Division;

public class Globals {

	public static int PREFERRED_LANGUAGE = Constants.LANGUAGE.ENGLISH;
	public static String PRESENT_DISTRICT_NAME = "";
	public static int PRESENT_DISTRICT_ID = -1;
	public static String PRESENT_THANA_NAME = "";
	public static int PRESENT_THANA_ID = -1;
	public static String PRESENT_THANA_QUICK_NUMBER = "";

	public static String[] MAIN_MENU_ITEMS = { "Police Station",
			"Fire Station", "Nearest Hospital", "View on Map" };

	public static String[] MAIN_MENU_ICONS = { "ic_police_1", "ic_fire_2",
			"ic_hos_2", "bangladesh_map" };

	// public static ArrayList<District> DISTRICT_LIST = new
	// ArrayList<District>();
	public static ArrayList<Division> DIVISION_LIST = new ArrayList<Division>();

}