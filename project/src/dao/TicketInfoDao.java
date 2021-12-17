package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import movie.OracleConnectionUtil;
import vo.TicketInfoVo;


public class TicketInfoDao {
	private static TicketInfoDao dao = new TicketInfoDao();
	
	private TicketInfoDao() {
	}
	public static TicketInfoDao getInstance() {
		return dao;
	}
	public List<TicketInfoVo> get_List(String movie_date, String movie_name) {
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT movie_time FROM TICKETINFO WHERE movie_date = ? AND MOVIE_NAME = ? ORDER BY movie_time";		
		TicketInfoVo vo1;
		List<TicketInfoVo> list1 = new ArrayList<TicketInfoVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, movie_date);
			pstmt.setString(2, movie_name);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				vo1 = new TicketInfoVo(rs.getString(1));
				list1.add(vo1);
			}
			return list1;
		} catch (SQLException e) {
			System.out.println("조회에 문제가 있습니다. : " + e.getMessage());
			e.printStackTrace();
	    }finally {
	    OracleConnectionUtil.close(conn);
        }
        return null;
	}
	
	public List<TicketInfoVo> get_TimeList(String movie_date, String movie_name, String movie_time) {
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT movie_time FROM TICKETINFO WHERE movie_date = ? AND MOVIE_NAME = ? AND MOVIE_TIME =?";		
		TicketInfoVo vo1;
		List<TicketInfoVo> list1 = new ArrayList<TicketInfoVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, movie_date);
			pstmt.setString(2, movie_name);
			pstmt.setString(3, movie_time);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				vo1 = new TicketInfoVo(rs.getString(1));
				list1.add(vo1);
			}
			return list1;
		} catch (SQLException e) {
			System.out.println("조회에 문제가 있습니다. : " + e.getMessage());
			e.printStackTrace();
	    }finally {
	    OracleConnectionUtil.close(conn);
        }
        return null;
	}
}
