package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import movie.OracleConnectionUtil;
import vo.MembersVo;

public class LoginDao {
	private static LoginDao ldao = new LoginDao();
	
	private LoginDao() {};
	
	public static LoginDao getInstance() {
		return ldao;
	}
	
	public boolean idCheck(MembersVo lvo) {	// 아이디 가입여부 확인
		String sql = "SELECT * FROM members WHERE id = ? AND password = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lvo.getId());
			pstmt.setString(2, lvo.getPassword());
			
			rs = pstmt.executeQuery();
			
			if (rs.next())
				return false;
			else
				return true;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				pstmt.close();
				rs.close();
				OracleConnectionUtil.close(conn);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}	// finally -end
		
		return false;
	}	// login -end
	
	
}	// class -end
