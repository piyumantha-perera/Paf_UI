package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class Payment {
	
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gadgetproject?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertPayment(String method, String date, String amount)  
	{   
		String output = ""; 
	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for inserting."; } 
	 
			// create a prepared statement 
			String query = " insert into payment(`pid`,`pmethod`,`pdate`,`pamount`)"
					 + " values (?, ?, ?, ?)";
	 
	 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			 preparedStmt.setInt(1, 0);
			 preparedStmt.setString(2, method);
			 preparedStmt.setString(3, date);
			 preparedStmt.setString(4, amount);
			
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	   
			String newPayment = readPayment(); 
			output =  "{\"status\":\"success\", \"data\": \"" + newPayment + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while inserting the Payment.\"}";  
			System.err.println(e.getMessage());   
		} 
		
	  return output;  
	} 
	
	
	public String readPayment()  
	{   
		String output = ""; 
	
		try   
		{    
			Connection con = connect(); 
		
			if (con == null)    
			{
				return "Error while connecting to the database for reading."; 
			} 
	 
			// Prepare the html table to be displayed    
			output = "<table border=\'1\'><tr><th>Payment Method</th><th>Date</th><th>Amount</th><th>Update</th><th>Remove</th></tr>";
	 
			String query = "select * from payment";    
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
	 
			// iterate through the rows in the result set    
			while (rs.next())    
			{     
				String pid = Integer.toString(rs.getInt("pid"));
				 String pmethod = rs.getString("pmethod");
				 String pdate = rs.getString("pdate");
				 String pamount = rs.getString("pamount");
			
	 
				// Add into the html table 
				output += "<tr><td><input id=\'hidPaymentIDUpdate\' name=\'hidPaymentIDUpdate\' type=\'hidden\' value=\'" + pid + "'>" 
							+ pmethod + "</td>"; 
				output += "<td>" + pdate + "</td>";
				output += "<td>" + pamount + "</td>";

				  
	 
				// buttons     
				output +="<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"       
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-paymentid='" + pid + "'>" + "</td></tr>"; 
			
			}
			con.close(); 
	 
			// Complete the html table    
			output += "</table>";   
		}   
		catch (Exception e)   
		{    
			output = "Error while reading the payment.";    
			System.err.println(e.getMessage());   
		} 
	 
		return output;  
	}
	
	public String updatePayment(String ID, String method, String date, String amount)  
	{   
		String output = ""; 
	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for updating."; } 
	 
			// create a prepared statement    
			String query = "UPDATE payment SET pmethod=?,pdate=?,pamount=?" 
					   + "WHERE pid=?";  
	 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setString(1, method);
			 preparedStmt.setString(2, date);
			 preparedStmt.setString(3, amount);
			 preparedStmt.setInt(4, Integer.parseInt(ID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	 
			String newPayment = readPayment();    
			output = "{\"status\":\"success\", \"data\": \"" + newPayment + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while updating the payment.\"}";   
			System.err.println(e.getMessage());   
		} 
	 
	  return output;  
	} 
	
	public String deletePayment(String pID)   
	{   
		String output = ""; 
	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{
				return "Error while connecting to the database for deleting."; 
				
			} 
	 
			// create a prepared statement    
			String query = "delete from payment where pid=?"; 
	 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setInt(1, Integer.parseInt(pID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	 
			String newPayment = readPayment();    
			output = "{\"status\":\"success\", \"data\": \"" +  newPayment + "\"}";    
		}   
		catch (Exception e)   
		{    
			output = "Error while deleting the payment.";    
			System.err.println(e.getMessage());   
		} 
	 
		return output;  
	}
	
}
