package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbutil.DBConnection;
import pojo.StoragePojo;

public class StorageDao {
private static PreparedStatement ps,ps1,ps2;
	
	static {
		try {
			ps = DBConnection.getConnection().prepareStatement("select filename, navpath, createdate, type, id from storage where userid=? and type='folder' and navpath=?");
			ps1 = DBConnection.getConnection().prepareStatement("select filename, navpath, filepath, createdate, type, id from storage where userid=? and type='file' and navpath=?");
			ps2 = DBConnection.getConnection().prepareStatement("insert into storage (userid,filename,navpath,filepath,createdate,type) values(?,?,?,?,?,?)");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static Map<String,List<StoragePojo>> getAllStorageData(String userid, String navpath) throws Exception {
		ps.setString(1, userid);
		ps.setString(2, navpath);
		
		ResultSet rs = ps.executeQuery();
		
		List<StoragePojo> folders = new ArrayList<>();
		while(rs.next()) {
			StoragePojo s = new StoragePojo();
			s.setFileName(rs.getString(1));
			s.setNavPath(rs.getString(2));
			java.util.Date date = new java.util.Date(rs.getDate(3).getTime());
			s.setCreateDate(date);
			s.setType(rs.getString(4));
			s.setId(rs.getInt(5));
			folders.add(s);
		}
		
		ps1.setString(1, userid);
		ps1.setString(2, navpath);
		
		ResultSet rs1 = ps1.executeQuery();
		
		List<StoragePojo> files = new ArrayList<>();
		while(rs1.next()) {
			StoragePojo s = new StoragePojo();
			s.setFileName(rs1.getString(1));
			s.setNavPath(rs1.getString(2));
			s.setFilePath(rs1.getString(3));
			java.util.Date date = new java.util.Date(rs1.getDate(4).getTime());
			s.setCreateDate(date);
			s.setType(rs1.getString(5));
			s.setId(rs1.getInt(6));
			files.add(s);
		}
		
		Map<String,List<StoragePojo>> alldatas = new HashMap<>();
		alldatas.put("folder", folders);
		alldatas.put("file", files);
		
		return alldatas;
	}
	
	public static boolean insertNewFolder(StoragePojo s) throws Exception {
		ps2.setString(1, s.getUserId());
		ps2.setString(2, s.getFileName());
		ps2.setString(3, s.getNavPath());
		ps2.setString(4, null);
		java.sql.Date date = new java.sql.Date(s.getCreateDate().getTime());
		ps2.setDate(5, date);
		ps2.setString(6, s.getType());
			
		return ps2.executeUpdate()>0;
	}
}








