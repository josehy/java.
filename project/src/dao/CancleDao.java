package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import movie.OracleConnectionUtil;
import vo.MembersVo;

public class CancleDao {
	public static CancleDao cdao = new CancleDao();
	
	private CancleDao() {}
	
	public static CancleDao getInstatnce() {
		return cdao;
	}
	
	// 로그인 후 예매취소
	public void Cancle(MembersVo mvo) {
		String sql = "DELETE FROM payment WHERE mem_code ="
				+ "(SELECT mem_code FROM members WHERE id = ? AND password = ?)"
				+ "AND movie_code = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, mvo.getId());
			pstmt.setString(2, mvo.getPassword());
			pstmt.setString(3, mvo.getMovie_code());
			
			pstmt.execute();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				pstmt.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			OracleConnectionUtil.close(conn);
		}	// finally -end
		
	}	// name_Cancle -end
	
}	// class -end
