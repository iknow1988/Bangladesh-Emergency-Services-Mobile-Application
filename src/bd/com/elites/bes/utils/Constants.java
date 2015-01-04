package bd.com.elites.bes.utils;

public class Constants {

	public static final class SHARED_PREFERENCE {

		public static final String SHARED_PREFERENCE_NAME = "bes_preference";
		public static final String LANGUAGE_PREFERENCE = "language_preference";

		public static final String PRESENT_DISTRICT = "present_dis";
		// public static final String PRESENT_DISTRICT_NAME =
		// "present_dis_name";
		// public static final String PRESENT_DISTRICT_NAME_BN =
		// "present_dis_name_bn";
		// public static final String PRESENT_DISTRICT_ID = "present_dis_id";

		public static final String PRESENT_THANA = "present_thana";
		// public static final String PRESENT_THANA_NAME = "present_thana_name";
		// public static final String PRESENT_THANA_ID = "present_thana_id";
		// public static final String PRESENT_THANA_QUICK_NUMBER =
		// "present_thana_quick_number";

		public static final String PROPERTY_REG_ID = "registration_id";
		public static final String PROPERTY_APP_VERSION = "appVersion";
	}

	public static final class DB_INFO {
		public static final String DATABASE_NAME = "db_bes";
		public static final int DATABASE_VERSION = 1;
	}

	public static final class LANGUAGE {
		public static final int ENGLISH = 0;
		public static final int BANGLA = 1;
	}

	public static final class ASYNCTASK_ACTIONS {
		public static final int GET_DISTRICT_LIST = 0;
		public static final int GET_POLICE_UNITS = 1;
		public static final int GET_POLICE_SUB_UNITS = 2;
		public static final int GET_POLICE_SUB_TWO_UNITS = 3;
		public static final int GET_POLICE_STATIONS = 4;
		public static final int GET_FIRE_STATIONS = 5;
		public static final int GET_POLICE_STATIONS_FOR_DISTRICT = 6;
		public static final int GET_FIRE_STATIONS_FOR_DISTRICT = 7;
		public static final int GET_DISTRICT_LIST_FROM_HOME = 8;
		public static final int GET_DISTRICT_LIST_FROM_FIRESERVICE = 9;
		public static final int SUBMIT_CONTRIBUTION = 10;
		public static final int SUBMIT_COMPLAIN = 11;
		public static final int GET_DISTRICT_LIST_FROM_SETTINGS = 12;
		public static final int GET_THANA_LIST_FROM_SETTINGS = 13;
		public static final int GET_DISTRICT_LIST_FROM_HOSPITAL = 14;
		public static final int GET_HOSPITALS = 15;
		public static final int GET_HOSPITALS_FOR_DISTRICT = 16;
		public static final int GET_GCM_REG_ID = 17;
		public static final int SEND_GCM_REG_ID = 18;
		public static final int GET_PUSH_NOTIFICATION = 19;
		public static final int GET_STATIONS_FROM_POLICE_BY_DISTRICT = 20;
	}

}
