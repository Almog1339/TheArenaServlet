package arena.bll;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.Part;

import org.json.simple.JSONObject;

import arena.dal.DBManager;

public class PhotosManager {

	
	public static JSONObject json = new JSONObject();
	public static ArrayList<FileOutputStream> photos = new ArrayList<>();
	
	public static void insertPhoto(String mail,InputStream image) throws IOException {
		Users currentUser = UsersManager.returnUserId(mail);
		DBManager.insertImage(currentUser.getId(), image);
	}
	
//	public static void selectPhoto(String mail) {
//		json.clear();
//		photos.clear();
//		
//		Users currentUser = UsersManager.returnUserId(mail);
//		String query = String.format("SELECT photo FROM usersPhotos where userId = %d;", currentUser.getId());
//		
//		
//		DBManager.runSelect(query, (res) -> {
//			try {
//
//				photos.add(res.getString("photo"));
//				json.put("userPhoto", photos);
//				
//			} catch (SQLException e) {
//				photos.clear();
//				json.clear();
//				//questions.clear();
//				e.printStackTrace();
//			}
//		});
//	}
	
	
	
	public static void selectPhoto(String mail,OutputStream os) {
		json.clear();
		Users currentUser = UsersManager.returnUserId(mail);
		String query = String.format("SELECT photo FROM usersPhotos WHERE userId = %d;", currentUser.getId());
		try {
			FileOutputStream image = DBManager.selectImage(query,os);
			photos.add(image);
			json.put("images",photos);
			//return image.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//	return null;
		
	}
	
}
	

