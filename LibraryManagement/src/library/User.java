package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {
	// ADD USER FUNCTION
	public static void AddUser(Scanner sc)
	{
		System.out.print("Enter Email Id : ");
		String email = sc.next();
		System.out.print("Enter Your First Name : ");
		String fname = sc.next();
		System.out.print("Enter Your Last Name : ");
		String lname = sc.next();
		System.out.print("Enter Your Mobile : ");
		String mobile = sc.next();
		System.out.print("Create A Password  : ");
		String pass = sc.next();
		System.out.print("Gender : ");
		String gender = sc.next();
		System.out.print("Address : ");
		String address = sc.next();
		try {
			Connection conn = DBConnect.getConnection();
			String sql = "INSERT INTO user(email,fname,lname,mobile,pass,gender,address) VALUE(?,?,?,?,?,?,?);";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1,email);
			pst.setString(2,fname);
			pst.setString(3,lname);
			pst.setString(4,mobile);
			pst.setString(5,pass);
			pst.setString(6,gender);
			pst.setString(7,address);
			int ar = pst.executeUpdate();
			if(ar>0)
				System.out.println("User Added Successfully!");
			else
				System.out.println("User Already Exist on "+email);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	// CHECK USER IN DATABASE
	public static int CheckUser(String email,String pass,String role)
	{
		try {
			Connection conn = DBConnect.getConnection();
			String sql = "SELECT * FROM user WHERE email=? AND pass=? AND roles=? AND sts=1";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1,email);
			pst.setString(2,pass);
			pst.setString(3,role);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	// LOGIN FOR STUDENT
	public static void Login(Scanner sc)
	{
		System.out.print("Enter Email Address  :");
		String email = sc.next();
		System.out.print("Enter Password  :");
		String pass = sc.next();
		int id = CheckUser(email,pass,"Student");
		if(id>=0)
		{
			int flag=1;
			while(flag==1) {
			System.out.println("\n\n*********** USER PANEL ************");
			System.out.println("1. Order A Book");
			System.out.println("2. Show Books");
			System.out.println("3. Logout");
			System.out.print("Enter Your Choice : ");
			int choice = sc.nextInt();
			switch(choice)
			{
			case 1: 
				Books.OrderBook(id);
				break;
			case 2:
				Books.ShowBooks();
				break;
			case 3:
				System.out.println("\nBye Bye User!");
				flag=0;
				break;
			default : 
				System.out.println("\n\nTry Again!");
			}
			}
		}else {
			System.out.println("User Not Found!\nOr User is Disabled!");
		}
	}
	// VIEW ALL STUDENTS
	public static void ViewStudent() {
		try {
			Connection conn = DBConnect.getConnection();
			String sql = "SELECT * FROM user WHERE roles='Student'";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				System.out.println("ID : "+rs.getInt(1));
				System.out.println("Email : "+rs.getString(2));
				System.out.println("First Name : "+rs.getString(3));
				System.out.println("Last Name : "+rs.getString(4));
				System.out.println("Mobile : "+rs.getString(5));
				System.out.println("Password : "+rs.getString(6));
				System.out.println("Gender : "+rs.getString(7));
				System.out.println("Address : "+rs.getString(8));
			}
		}catch(Exception e ) {
			e.printStackTrace();
		}
	}
	// DELETE A STUDENT BY ID
	public static void DeleteStudent(Scanner sc) {
		System.out.print("\nEnter ID :");
		int id = sc.nextInt();
		try {
			String sql = "DELETE FROM user WHERE uid=? AND roles='Student'";
			PreparedStatement pst = DBConnect.getConnection().prepareStatement(sql);
			pst.setInt(1, id);
			int ar = pst.executeUpdate();
			if(ar>0)
				System.out.println("\nStudent Deleted Successfully!");
			else
				System.out.println("\nStudent Not Found");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	// ADMIN LOGIN PAGE
	public static void AdminLogin(Scanner sc)
	{
		System.out.print("Enter Admin Email Address  :");
		String email = sc.next();
		System.out.print("Enter Admin Password  :");
		String pass = sc.next();
		if(CheckUser(email,pass,"Admin")>0)
		{
			int flag=1;
			while(flag==1) {
			System.out.println("\n******** ADMIN LOGIN *********");
			System.out.println("1. View Students");
			System.out.println("2. View Book Orders");
			System.out.println("3. Active/Unactive Student");
			System.out.println("4. Delete A Student");
			System.out.println("5. Return Order");
			System.out.println("6. Add A Book");
			System.out.println("7. Delete A Book");
			System.out.println("8. Exit");
			System.out.print("Enter Your Choice : ");
			int choice = sc.nextInt();
			switch(choice)
			{
			case 1:
				ViewStudent();
				break;
			case 2:
				Books.ViewOrders();
				break;
			case 3:
				UserActiveUnactive(sc);
				break;
			case 4:
				User.DeleteStudent(sc);break;
			case 5:
				Books.ViewOrderById(sc);
				break;
			case 6:
				Books.AddBook(sc);
				break;
			case 7:
				Books.DeleteBook(sc);
				break;
			case 8:
				System.out.println("\n\nBye-Bye Admin\n");
				flag=0;
				break;
			default:
				System.out.println("\nTry Again!");
			}
			}
		}else {
			System.out.println("\nAdmin Email or Password is incorrect!");
		}
	}
	public static void UserActiveUnactive(Scanner sc) {
		System.out.println("Enter User ID : ");
		int uid = sc.nextInt();
		try {
			int sts=-1;
			System.out.println("Press 1 to Active");
			System.out.println("Press 0 to Unactive");
			int ch = sc.nextInt();
			if(ch==1)
				sts=1;
			else if(ch==0)
				sts=0;
			else
				System.out.println("Wrong Entered!\n");
			if((sts==1)||(sts==0))
			{
				String st = "UPDATE user SET sts=? WHERE uid=?";
				PreparedStatement ps = DBConnect.getConnection().prepareStatement(st);
				ps.setInt(1,sts);
				ps.setInt(2,uid);
				int ar = ps.executeUpdate();
				if(ar>0)
					System.out.println("User Update Successfull!");
				else
					System.out.println("Failed To Update!");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}