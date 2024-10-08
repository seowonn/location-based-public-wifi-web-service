package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import dbConnection.ConnectDB;
import dto.WIfiInfosDto;

public class WIfiInfosDao {
	public static Connection connection;
	public static PreparedStatement preparedStatement;
	public static ResultSet resultSet;
	
	public WIfiInfosDao() {}
	
	public List<WIfiInfosDto> getNearWifis(String lat, String lnt) {
		
		connection = null;
		preparedStatement = null;
		resultSet = null;
		
		List<WIfiInfosDto> nearWifisInfo = new ArrayList<>();
		
		String getNearWifisQuery = " SELECT " +
		  " round(6371*acos(cos(radians(?))*cos(radians(LAT))*cos(radians(LNT) " +
		  " -radians(?))+sin(radians(?))*sin(radians(LAT))), 4) " + 
		  " AS distance, * " +
		  " FROM SEOUL_WIFI " + 
		  " ORDER BY distance " + 
		  " LIMIT 20;";
		 
		
		try {						
			connection = ConnectDB.connectDB();
			connection.setAutoCommit(false);
						
			preparedStatement = connection.prepareStatement(getNearWifisQuery);
			preparedStatement.setDouble(1, Double.parseDouble(lat));
			preparedStatement.setDouble(2, Double.parseDouble(lnt));
			preparedStatement.setDouble(3, Double.parseDouble(lat));
			
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {

				WIfiInfosDto nearWifi = WIfiInfosDto.builder()
						.distance(resultSet.getDouble("distance"))
						.mgr_no(resultSet.getString("mgr_no"))
						.wrdofc(resultSet.getString("wrdofc"))
						.main_nm(resultSet.getString("main_nm"))
						.adres1(resultSet.getString("adres1"))
						.adres2(resultSet.getString("adres2"))
						.instl_floor(resultSet.getString("instl_floor"))
						.instl_ty(resultSet.getString("instl_ty"))
						.instl_mby(resultSet.getString("instl_mby"))
						.svc_se(resultSet.getString("svc_se"))
						.cmcwr(resultSet.getString("cmcwr"))
						.cnstc_year(resultSet.getString("cnstc_year"))
						.inout_door(resultSet.getString("inout_door"))
						.remars3(resultSet.getString("remars3"))
						.lnt(resultSet.getString("lnt"))
						.lat(resultSet.getString("lat"))
						.work_dttm(String.valueOf(resultSet.getTimestamp("work_dttm").toLocalDateTime()))
						.build();
				
				nearWifisInfo.add(nearWifi);
			}
			
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectDB.close(connection, preparedStatement, resultSet);
		}
		
		WifiHistoryDao.insertSearchData(lat, lnt);
		
		return nearWifisInfo;
	}
	
	public WIfiInfosDto getWifiInfo(String mgrNo){
		
		WIfiInfosDto wifiInfo = null;
		
		connection = null;
		preparedStatement = null;
		resultSet = null;
		
		String getWifiInfoQuery = "SELECT *" +
		  " FROM SEOUL_WIFI " + 
		  " WHERE MGR_NO = ?";
		
		try {						
			connection = ConnectDB.connectDB();
			connection.setAutoCommit(false);
						
			preparedStatement = connection.prepareStatement(getWifiInfoQuery);
			preparedStatement.setString(1, mgrNo);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
		
				wifiInfo = WIfiInfosDto.builder()
						.mgr_no(resultSet.getString("mgr_no"))
						.wrdofc(resultSet.getString("wrdofc"))
						.main_nm(resultSet.getString("main_nm"))
						.adres1(resultSet.getString("adres1"))
						.adres2(resultSet.getString("adres2"))
						.instl_floor(resultSet.getString("instl_floor"))
						.instl_ty(resultSet.getString("instl_ty"))
						.instl_mby(resultSet.getString("instl_mby"))
						.svc_se(resultSet.getString("svc_se"))
						.cmcwr(resultSet.getString("cmcwr"))
						.cnstc_year(resultSet.getString("cnstc_year"))
						.inout_door(resultSet.getString("inout_door"))
						.remars3(resultSet.getString("remars3"))
						.lnt(resultSet.getString("lnt"))
						.lat(resultSet.getString("lat"))
						.work_dttm(String.valueOf(resultSet.getTimestamp("work_dttm").toLocalDateTime()))
						.build();		
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectDB.close(connection, preparedStatement, resultSet);
		}
		
		return wifiInfo;
	}
}
