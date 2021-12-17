package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import movie.OracleConnectionUtil;
import vo.TicketpriVo;


public class TicketPriDao {
	private static TicketPriDao dao = new TicketPriDao();
	
	private TicketPriDao() {
	}
	public static TicketPriDao getInstance() {
		return dao;
	}
	String movie_price;
	public List<TicketpriVo> getsList(String movie_date, String movie_name, String movie_time) {
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT movie_code,movie_price FROM TICKETINFO WHERE movie_name = ? AND movie_date = ? AND MOVIE_TIME = ?";		
		TicketpriVo vo2;
		List<TicketpriVo> list = new ArrayList<TicketpriVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, movie_time);			
			pstmt.setString(2, movie_date);
			pstmt.setString(3, movie_time);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				vo2 = new TicketpriVo(rs.getString(1),rs.getInt(2));
				list.add(vo2);
			    movie_price = rs.getString(2);
			}
			return list;
		} catch (SQLException e) {
			System.out.println("조회에 문제가 있습니다. : " + e.getMessage());
			e.printStackTrace();
	    }finally {
	    OracleConnectionUtil.close(conn);
        }
        return null;
	}
}