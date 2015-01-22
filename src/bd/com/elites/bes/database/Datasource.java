package bd.com.elites.bes.database;

import java.util.ArrayList;

import javax.crypto.Cipher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bd.com.elites.bes.map.StationInfoForMap;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.FireserviceStation;
import bd.com.elites.bes.model.HospitalModel;
import bd.com.elites.bes.model.PoliceSubTwoUnit;
import bd.com.elites.bes.model.PoliceSubUnit;
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.model.PoliceUnit;
import bd.com.elites.bes.utils.AES;
import bd.com.elites.bes.utils.Constants;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Datasource extends SQLiteAssetHelper {

	public Datasource(Context context) {
		super(context, Constants.DB_INFO.DATABASE_NAME, null,
				Constants.DB_INFO.DATABASE_VERSION);
	}

	public ArrayList<District> get_districts() {

		// SELECT * FROM (`district`) ORDER BY `district_name_en` asc
		ArrayList<District> districtList = new ArrayList<District>();

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query("district", null, null, null, null, null,
				"division, district_name_en");
		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("district_name_en");
				String district_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district_name_bn");
				String district_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district_name_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("division");
				String division = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					division = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("division_bn");
				String division_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					division_bn = cursor.getString(columnIndex);
				}

				districtList.add(new District(id, district_name,
						district_name_bn, division, division_bn));

			}

		}

		cursor.close();
		db.close();
		return districtList;
	}

	public ArrayList<PoliceUnit> get_police_units() {

		// SELECT * FROM (`police_units`)
		ArrayList<PoliceUnit> policeUnits = new ArrayList<PoliceUnit>();

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query("police_units", null, null, null, null, null,
				"unit_name_en");

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("unit_name_en");
				String unit_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					unit_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("unit_name_bn");
				String unit_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					unit_name_bn = cursor.getString(columnIndex);
				}

				policeUnits.add(new PoliceUnit(id, unit_name, unit_name_bn));

			}

		}
		cursor.close();
		db.close();

		return policeUnits;

	}

	public ArrayList<PoliceSubUnit> get_police_sub_units(String unit_id) {

		ArrayList<PoliceSubUnit> policeSubUnits = new ArrayList<PoliceSubUnit>();

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query("police_sub_units", null,
				"parent_unit_id = ?", new String[] { unit_id }, null, null,
				"sub_unit_name_en");

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("sub_unit_name_en");
				String sub_unit_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					sub_unit_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("sub_unit_name_bn");
				String sub_unit_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					sub_unit_name_bn = cursor.getString(columnIndex);
				}

				policeSubUnits.add(new PoliceSubUnit(id, sub_unit_name,
						sub_unit_name_bn, Integer.parseInt(unit_id)));

			}

		}
		cursor.close();
		db.close();

		return policeSubUnits;

	}

	public ArrayList<PoliceSubTwoUnit> get_police_sub_two_units(
			String sub_unit_id) {

		ArrayList<PoliceSubTwoUnit> policeSubTwoUnits = new ArrayList<PoliceSubTwoUnit>();

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query("police_sub_two_units", null,
				"parent_sub_unit_id = ?", new String[] { sub_unit_id }, null,
				null, "sub_two_unit_name_en");

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("sub_two_unit_name_en");
				String sub_two_unit_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					sub_two_unit_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("sub_two_unit_name_bn");
				String sub_two_unit_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					sub_two_unit_name_bn = cursor.getString(columnIndex);
				}

				policeSubTwoUnits.add(new PoliceSubTwoUnit(id,
						sub_two_unit_name, sub_two_unit_name_bn, Integer
								.parseInt(sub_unit_id)));

			}

		}
		cursor.close();
		db.close();

		return policeSubTwoUnits;

	}

	public ArrayList<PoliceThana> get_police_stations(String unit_id,
			String sub_unit_id, String sub_two_unit_id) {

		ArrayList<PoliceThana> policeStations = new ArrayList<PoliceThana>();
		SQLiteDatabase db = getReadableDatabase();

		String query = "SELECT ";
		query += "`police_thanas`.`id` as `id`, ";
		query += "`police_thanas`.`thana_name_en` as `thana_name`, ";
		query += "`police_thanas`.`thana_name_bn` as `thana_name_bn`, ";
		query += "`police_thanas`.`phone_number_1` as `phone_number_1`, ";
		query += "`police_thanas`.`phone_number_2` as `phone_number_2`, ";
		query += "`police_thanas`.`address_en` as `address`, ";
		query += "`police_thanas`.`address_bn` as `address_bn`, ";
		query += "`police_thanas`.`latitude` as `latitude`, ";
		query += "`police_thanas`.`longitude` as `longitude`, ";
		query += "`second_level_region`.`region_name_en` as `second_level_region`, ";
		query += "`second_level_region`.`region_name_bn` as `second_level_region_bn`, ";
		query += "`district`.`district_name_en` as `district`, ";
		query += "`district`.`district_name_bn` as `district_bn` ";

		query += " FROM (`police_thanas`, `second_level_region`, `district`) ";

		query += "WHERE `police_thanas`.`unit_id` =  '" + unit_id + "'";

		if (Integer.parseInt(sub_unit_id) != -1) {
			query += " AND `police_thanas`.`sub_unit_id` =  '" + sub_unit_id
					+ "' ";
		}

		if (Integer.parseInt(sub_two_unit_id) != -1) {
			query += " AND `police_thanas`.`sub_two_unit_id` =  '"
					+ sub_two_unit_id + "' ";
		}

		query += " AND `police_thanas`.`second_level_region` = second_level_region.id ";
		query += " AND `police_thanas`.`district` = district.id ";
		query += " ORDER BY `police_thanas`.`thana_name_en` asc ";

		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("thana_name");
				String thana_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					thana_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("thana_name_bn");
				String thana_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					thana_name_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("phone_number_1");
				String phone_number_1 = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					phone_number_1 = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("phone_number_2");
				String phone_number_2 = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					phone_number_2 = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address");
				String address = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address_bn");
				String address_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("latitude");
				String latitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					latitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("longitude");
				String longitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					longitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region");
				String second_level_region = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region_bn");
				String second_level_region_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district");
				String district = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district_bn");
				String district_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district_bn = cursor.getString(columnIndex);
				}

				policeStations.add(new PoliceThana(id, thana_name,
						thana_name_bn, phone_number_1, phone_number_2,
						district, district_bn, second_level_region,
						second_level_region_bn, address, address_bn, latitude,
						longitude));
			}

		}
		cursor.close();
		db.close();

		return policeStations;

	}

	public ArrayList<PoliceThana> get_police_stations_by_district(
			String district_id) {

		ArrayList<PoliceThana> policeStations = new ArrayList<PoliceThana>();
		SQLiteDatabase db = getReadableDatabase();

		String query = "SELECT ";
		query += "`police_thanas`.`id` as `id`, ";
		query += "`police_thanas`.`thana_name_en` as `thana_name`, ";
		query += "`police_thanas`.`thana_name_bn` as `thana_name_bn`, ";
		query += "`police_thanas`.`phone_number_1` as `phone_number_1`, ";
		query += "`police_thanas`.`phone_number_2` as `phone_number_2`, ";
		query += "`police_thanas`.`address_en` as `address`, ";
		query += "`police_thanas`.`address_bn` as `address_bn`, ";
		query += "`police_thanas`.`latitude` as `latitude`, ";
		query += "`police_thanas`.`longitude` as `longitude`, ";
		query += "`second_level_region`.`region_name_en` as `second_level_region`, ";
		query += "`second_level_region`.`region_name_bn` as `second_level_region_bn`, ";
		query += "`district`.`district_name_en` as `district`, ";
		query += "`district`.`district_name_bn` as `district_bn` ";

		query += " FROM (`police_thanas`, `second_level_region`, `district`) ";

		query += "WHERE `police_thanas`.`district` =  '" + district_id + "'";

		query += " AND `police_thanas`.`second_level_region` = second_level_region.id ";
		query += " AND `police_thanas`.`district` = district.id ";
		query += " ORDER BY `police_thanas`.`thana_name_en` asc ";

		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("thana_name");
				String thana_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					thana_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("thana_name_bn");
				String thana_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					thana_name_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("phone_number_1");
				String phone_number_1 = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					phone_number_1 = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("phone_number_2");
				String phone_number_2 = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					phone_number_2 = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address");
				String address = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address_bn");
				String address_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("latitude");
				String latitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					latitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("longitude");
				String longitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					longitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region");
				String second_level_region = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region_bn");
				String second_level_region_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district");
				String district = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district_bn");
				String district_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district_bn = cursor.getString(columnIndex);
				}

				policeStations.add(new PoliceThana(id, thana_name,
						thana_name_bn, phone_number_1, phone_number_2,
						district, district_bn, second_level_region,
						second_level_region_bn, address, address_bn, latitude,
						longitude));
			}

		}
		cursor.close();
		db.close();

		return policeStations;

	}

	public ArrayList<FireserviceStation> get_fire_stations(String district_id) {

		ArrayList<FireserviceStation> fireStations = new ArrayList<FireserviceStation>();
		SQLiteDatabase db = getReadableDatabase();

		String query = "SELECT ";
		query += " `fireservice_stations`.`id` as `id`, ";
		query += " `fireservice_stations`.`station_name_en` as `station_name`, ";
		query += " `fireservice_stations`.`station_name_bn` as `station_name_bn`, ";
		query += " `fireservice_stations`.`telephone_number` as `telephone_number`,";
		query += " `fireservice_stations`.`mobile_number` as `mobile_number`, ";
		query += " `fireservice_stations`.`address_en` as `address`, ";
		query += " `fireservice_stations`.`address_bn` as `address_bn`, ";
		query += " `fireservice_stations`.`latitude` as `latitude`, ";
		query += " `fireservice_stations`.`longitude` as `longitude`, ";
		query += " `second_level_region`.`region_name_en` as `second_level_region`, ";
		query += " `second_level_region`.`region_name_bn` as `second_level_region_bn`, ";
		query += " `district`.`district_name_en` as `district`, ";
		query += " `district`.`district_name_bn` as `district_bn` ";
		query += " FROM (`fireservice_stations`, `second_level_region`, `district`) ";
		query += " WHERE ";
		query += " `fireservice_stations`.`district` = '" + district_id + "' ";
		query += " AND `fireservice_stations`.`second_level_region` = second_level_region.id ";
		query += " AND `fireservice_stations`.`district` = district.id ";
		query += " ORDER BY `fireservice_stations`.`station_name_en` asc";

		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("station_name");
				String station_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					station_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("station_name_bn");
				String station_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					station_name_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("telephone_number");
				String telephone_number = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					telephone_number = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("mobile_number");
				String mobile_number = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					mobile_number = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address");
				String address = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address_bn");
				String address_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("latitude");
				String latitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					latitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("longitude");
				String longitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					longitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region");
				String second_level_region = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region_bn");
				String second_level_region_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district");
				String district = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district_bn");
				String district_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district_bn = cursor.getString(columnIndex);
				}

				fireStations.add(new FireserviceStation(id, station_name,
						station_name_bn, telephone_number, mobile_number,
						district, district_bn, second_level_region,
						second_level_region_bn, address, address_bn, latitude,
						longitude));
			}

		}
		cursor.close();
		db.close();

		return fireStations;

	}

	public ArrayList<HospitalModel> get_hospitals(String district_id) {

		ArrayList<HospitalModel> hospitals = new ArrayList<HospitalModel>();
		SQLiteDatabase db = getReadableDatabase();

		String query = "SELECT ";
		query += " `hospitals`.`id` as `id`, ";
		query += " `hospitals`.`hospital_name_en` as `station_name`, ";
		query += " `hospitals`.`hospital_name_bn` as `station_name_bn`, ";
		query += " `hospitals`.`telephone_number` as `telephone_number`,";
		query += " `hospitals`.`mobile_number` as `mobile_number`, ";
		query += " `hospitals`.`address_en` as `address`, ";
		query += " `hospitals`.`address_bn` as `address_bn`, ";
		query += " `hospitals`.`latitude` as `latitude`, ";
		query += " `hospitals`.`longitude` as `longitude`, ";
		query += " `second_level_region`.`region_name_en` as `second_level_region`, ";
		query += " `second_level_region`.`region_name_bn` as `second_level_region_bn`, ";
		query += " `district`.`district_name_en` as `district`, ";
		query += " `district`.`district_name_bn` as `district_bn` ";
		query += " FROM (`hospitals`, `second_level_region`, `district`) ";
		query += " WHERE ";
		query += " `hospitals`.`district` = '" + district_id + "' ";
		query += " AND `hospitals`.`second_level_region` = second_level_region.id ";
		query += " AND `hospitals`.`district` = district.id ";
		query += " ORDER BY `hospitals`.`hospital_name_en` asc";

		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));

				int columnIndex = cursor.getColumnIndex("station_name");
				String station_name = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					station_name = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("station_name_bn");
				String station_name_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					station_name_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("telephone_number");
				String telephone_number = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					telephone_number = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("mobile_number");
				String mobile_number = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					mobile_number = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address");
				String address = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("address_bn");
				String address_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					address_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("latitude");
				String latitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					latitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("longitude");
				String longitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					longitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region");
				String second_level_region = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("second_level_region_bn");
				String second_level_region_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					second_level_region_bn = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district");
				String district = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("district_bn");
				String district_bn = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					district_bn = cursor.getString(columnIndex);
				}

				hospitals.add(new HospitalModel(id, station_name,
						station_name_bn, telephone_number, mobile_number,
						district, district_bn, second_level_region,
						second_level_region_bn, address, address_bn, latitude,
						longitude));
			}

		}
		cursor.close();
		db.close();

		return hospitals;

	}

	// public ArrayList<Hospital> get_hospitals(String district_id) {
	//
	// ArrayList<Hospital> hospitals = new ArrayList<Hospital>();
	// SQLiteDatabase db = getReadableDatabase();
	//
	// String query = "SELECT ";
	// query += " `hospitals`.`id` as `id`, ";
	// query += " `hospitals`.`hospital_name_en` as `hospital_name`, ";
	// query += " `hospitals`.`hospital_name_bn` as `hospital_name_bn`, ";
	// query += " `hospitals`.`telephone_number` as `telephone`,";
	// query += " `hospitals`.`mobile_number` as `mobile`, ";
	// query += " `hospitals`.`address_en` as `address`, ";
	// query += " `hospitals`.`address_bn` as `address_bn`, ";
	// query += " `hospitals`.`latitude` as `latitude`, ";
	// query += " `hospitals`.`longitude` as `longitude`, ";
	// // query +=
	// // " `second_level_region`.`region_name_en` as `second_level_region`, ";
	// query += " `district`.`district_name_en` as `district` ";
	// // query +=
	// // " FROM (`hospitals`, `second_level_region`, `district`) ";
	// query += " FROM (`hospitals`, `district`) ";
	// query += " WHERE ";
	// query += " `hospitals`.`district` = '" + district_id + "' ";
	// // query +=
	// // " AND `hospitals`.`second_level_region` = second_level_region.id ";
	// query += " AND `hospitals`.`district` = district.id ";
	// query += " ORDER BY `hospitals`.`hospital_name_en` asc";
	// Log.d("tonmoy", query);
	//
	// Cursor cursor = db.rawQuery(query, null);
	//
	// if (cursor != null && cursor.getCount() > 0) {
	// Log.d("tonmoy", "Count : " + cursor.getCount());
	// while (cursor.moveToNext()) {
	//
	// int id = cursor.getInt(cursor.getColumnIndex("id"));
	//
	// int columnIndex = cursor.getColumnIndex("hospital_name");
	// String station_name = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// station_name = cursor.getString(columnIndex);
	// }
	//
	// columnIndex = cursor.getColumnIndex("hospital_name_bn");
	// String station_name_bn = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// station_name_bn = cursor.getString(columnIndex);
	// }
	//
	// columnIndex = cursor.getColumnIndex("telephone");
	// String telephone_number = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// telephone_number = cursor.getString(columnIndex);
	// }
	//
	// columnIndex = cursor.getColumnIndex("mobile");
	// String mobile_number = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// mobile_number = cursor.getString(columnIndex);
	// }
	//
	// columnIndex = cursor.getColumnIndex("address");
	// String address = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// address = cursor.getString(columnIndex);
	// }
	//
	// columnIndex = cursor.getColumnIndex("latitude");
	// String latitude = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// latitude = cursor.getString(columnIndex);
	// }
	//
	// columnIndex = cursor.getColumnIndex("longitude");
	// String longitude = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// longitude = cursor.getString(columnIndex);
	// }
	//
	// // columnIndex = cursor.getColumnIndex("second_level_region");
	// // String second_level_region = "";
	// // if (!cursor.isNull(columnIndex)
	// // && cursor.getString(columnIndex).length() > 0) {
	// // second_level_region = cursor.getString(columnIndex);
	// // }
	//
	// columnIndex = cursor.getColumnIndex("district");
	// String district = "";
	// if (!cursor.isNull(columnIndex)
	// && cursor.getString(columnIndex).length() > 0) {
	// district = cursor.getString(columnIndex);
	// }
	//
	// hospitals.add(new Hospital(id, station_name, station_name_bn,
	// telephone_number, mobile_number, district, latitude,
	// longitude, address));
	// }
	//
	// }
	// cursor.close();
	// db.close();
	// Log.d("tonmoy", "list size is : " + hospitals.size());
	// return hospitals;
	//
	// }

	// Methods for Map

	// public ArrayList<StationInfoForMap> getAllPoliceStationsForMap() {
	// ArrayList<StationInfoForMap> items = new ArrayList<StationInfoForMap>();
	// SQLiteDatabase db = getReadableDatabase();
	// Cursor c = db.rawQuery("select * from police_thanas ", null);
	// if (c != null && c.moveToFirst()) {
	// do {
	// Double lat = -1.0, lng = -1.0;
	// if (!(c.isNull(12) && c.isNull(13))
	// && c.getString(12).length() > 0) {
	// lat = Double.parseDouble(c.getString(12));
	// lng = Double.parseDouble(c.getString(13));
	// }
	// StationInfoForMap ps = new StationInfoForMap(c.getString(1),
	// c.getString(2), StationInfoForMap.POLICE_STATION, lat,
	// lng, c.getString(6), c.getString(7), c.getString(10),
	// c.getString(11));
	// items.add(ps);
	// } while (c.moveToNext());
	// }
	//
	// c.close();
	// db.close();
	// return items;
	// }

	public ArrayList<StationInfoForMap> getAllPoliceStationsForMapByDistrict(
			String id) {
		ArrayList<StationInfoForMap> items = new ArrayList<StationInfoForMap>();
		ArrayList<PoliceThana> allThana = get_police_stations_by_district(id);
		for (PoliceThana policeThana : allThana) {
			StationInfoForMap std = new StationInfoForMap(
					policeThana.thana_name, policeThana.thana_name_bn,
					StationInfoForMap.POLICE_STATION, policeThana.latitude,
					policeThana.longitude, policeThana.phone_number_1,
					policeThana.phone_number_2, policeThana.address,
					policeThana.address_bn, policeThana.district,
					policeThana.district_bn, policeThana.second_level_region,
					policeThana.second_level_region_bn);
			items.add(std);

		}
		return items;
	}

	public ArrayList<StationInfoForMap> getAllFireStationsForMapByDistrict(
			String id) {
		ArrayList<StationInfoForMap> items = new ArrayList<StationInfoForMap>();
		ArrayList<FireserviceStation> allFireStation = get_fire_stations(id);
		for (FireserviceStation fireStation : allFireStation) {
			StationInfoForMap std = new StationInfoForMap(
					fireStation.station_name, fireStation.station_name_bn,
					StationInfoForMap.FIRE_STATION, fireStation.latitude,
					fireStation.longitude, fireStation.telephone_number,
					fireStation.mobile_number, fireStation.address,
					fireStation.address_bn, fireStation.district,
					fireStation.district_bn, fireStation.second_level_region,
					fireStation.second_level_region_bn);
			items.add(std);

		}
		return items;
	}

	public ArrayList<StationInfoForMap> getAllHospitalStationsForMapByDistrict(
			String id) {
		ArrayList<StationInfoForMap> items = new ArrayList<StationInfoForMap>();
		ArrayList<HospitalModel> allHospitals = get_hospitals(id);
		for (HospitalModel hospital : allHospitals) {
			StationInfoForMap std = new StationInfoForMap(
					hospital.station_name, hospital.station_name_bn,
					StationInfoForMap.HOSPITAL, hospital.latitude,
					hospital.longitude, hospital.telephone_number,
					hospital.mobile_number, hospital.address,
					hospital.address_bn, hospital.district,
					hospital.district_bn, hospital.second_level_region,
					hospital.second_level_region_bn);
			items.add(std);

		}
		return items;

		// // TODO change the sql query and curson accordingly
		// ArrayList<StationInfoForMap> items = new
		// ArrayList<StationInfoForMap>();
		// SQLiteDatabase db = getReadableDatabase();
		// Cursor c = db.rawQuery("select * from hospitals where district=" +
		// id,
		// null);
		// if (c != null && c.moveToFirst()) {
		// do {
		// Double lat = -1.0, lng = -1.0;
		// if (!(c.isNull(4) && c.isNull(5))
		// && c.getString(4).length() > 0) {
		// lat = Double.parseDouble(c.getString(4));
		// lng = Double.parseDouble(c.getString(5));
		// }
		// StationInfoForMap ps = new StationInfoForMap(c.getString(1),
		// c.getString(2), StationInfoForMap.HOSPITAL, lat, lng,
		// c.getString(6), c.getString(7), "House 5, Road 7",
		// "বাসা ৫, রোড ৭");
		// items.add(ps);
		// } while (c.moveToNext());
		// }
		//
		// c.close();
		// db.close();
		// return items;
	}

	public District getDistrictByName(String name) {

		District district = null;
		SQLiteDatabase db = getReadableDatabase();

		int id = -1;
		String district_name = "", division = "", division_bn = "", district_name_bn = "";

		Cursor cursor = db.query("district", null, "district_name_en LIKE ?",
				new String[] { "%" + name.toLowerCase() + "%" }, null, null,
				null);
		if (cursor != null && cursor.getCount() > 0) {

			cursor.moveToFirst();

			id = cursor.getInt(cursor.getColumnIndex("id"));

			int columnIndex = cursor.getColumnIndex("district_name_en");

			if (!cursor.isNull(columnIndex)
					&& cursor.getString(columnIndex).length() > 0) {
				district_name = cursor.getString(columnIndex);
			}
			columnIndex = cursor.getColumnIndex("district_name_bn");

			if (!cursor.isNull(columnIndex)
					&& cursor.getString(columnIndex).length() > 0) {
				district_name_bn = cursor.getString(columnIndex);
			}
			columnIndex = cursor.getColumnIndex("division");

			if (!cursor.isNull(columnIndex)
					&& cursor.getString(columnIndex).length() > 0) {
				division = cursor.getString(columnIndex);
			}

			columnIndex = cursor.getColumnIndex("division_bn");

			if (!cursor.isNull(columnIndex)
					&& cursor.getString(columnIndex).length() > 0) {
				division_bn = cursor.getString(columnIndex);
			}
			district = new District(id, district_name, district_name_bn,
					division, division_bn);
		}

		cursor.close();
		db.close();
		return district;
	}

	public boolean encrypt() {
		String selectQuery = "SELECT  * FROM " + "hospitals";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				String m_word = cursor.getString(0);
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int columnIndex = cursor.getColumnIndex("telephone_number");
				String telephone_number = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					telephone_number = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("mobile_number");
				String mobile_number = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					mobile_number = cursor.getString(columnIndex);
				}
				columnIndex = cursor.getColumnIndex("latitude");
				String latitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					latitude = cursor.getString(columnIndex);
				}

				columnIndex = cursor.getColumnIndex("longitude");
				String longitude = "";
				if (!cursor.isNull(columnIndex)
						&& cursor.getString(columnIndex).length() > 0) {
					longitude = cursor.getString(columnIndex);
				}
				AES.encryptIt(latitude);
				updatedata(AES.encryptIt(latitude), AES.encryptIt(longitude), AES.encryptIt(telephone_number),
						AES.encryptIt(mobile_number), "id=" + id);

			} while (cursor.moveToNext());
		}
		db.close();
		return true;
	}

	public void updatedata(String lat, String lon, String tele, String mobile,
			String whereCondition) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues newValues = new ContentValues();
		newValues.put("telephone_number", tele);
		newValues.put("mobile_number", mobile);
		newValues.put("latitude", lat);
		newValues.put("longitude", lon);

		db.update("hospitals", newValues, whereCondition, null);
		db.close();
	}
//	private byte[] decrypt(byte[] bytes) throws Exception {
//	    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");
//	    cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
//	    return cipher.doFinal(bytes);
//	}
//
//	private byte[] encrypt(byte[] bytes) throws Exception {
//	    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");
//	    cipher.init(Cipher.ENCRYPT_MODE, SERVER_KEY);
//	    return cipher.doFinal(bytes);
//	}


}
