package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import movie.OracleConnectionUtil;
import vo.MembersVo;

public class SignupDao {
    private static SignupDao sdao = new SignupDao();
    
    private SignupDao() {};
    
    public static SignupDao getInstance() {
    	return sdao;
    }
     
     public void insert(MembersVo mvo) {
    	Connection conn = OracleConnectionUtil.connect();
    	PreparedStatement pstmt = null;
    	
    	String sql = "INSERT INTO MEMBERS (mem_code, id, password, name, email, tel)"
    			+ " VALUES(mem_code.nextval,?,?,?,?,?)";
 		
 		try {
 			pstmt = conn.prepareStatement(sql);
 			
 			pstmt.setString(1, mvo.getId());
 			pstmt.setString(2, mvo.getPassword());
 			pstmt.setString(3, mvo.getName());
 			pstmt.setString(4, mvo.getEmail());
 			pstmt.setString(5, mvo.getTel());
 		
 	  		pstmt.execute();
 		}
 		catch (SQLException e) {
	      System.out.println("SQL 실행에 오류가 발생했습니다."+e.getMessage());
			e.printStackTrace();
		}
 		finally {
			try {
				pstmt.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			OracleConnectionUtil.close(conn);  //연결종료
		}
     		
     } //insert end
 
     
public boolean idcheck (String id) {
	Connection conn = OracleConnectionUtil.connect();
	String sql ="SELECT * FROM MEMBERS WHERE id =?";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,id);
		
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			return false;
		}
		else
			return true;
		
	} catch (SQLException e) {
		System.err.println("SQL 실행에 오류가 발생했습니다." + e.getMessage());
		e.printStackTrace();
	}
	finally {
		try {
			rs.close();
			pstmt.close();    
		}
		catch (SQLException e) {
			System.out.println("close 오류발생" + e.getMessage());
			e.printStackTrace();
		}
		OracleConnectionUtil.close(conn);
	}
	return false;
	
} //idcheck end



}//SignupDao end



	

  


