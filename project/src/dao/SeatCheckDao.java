package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import movie.OracleConnectionUtil;
import vo.SeatVo;

public class SeatCheckDao {
	public static SeatCheckDao dao = new SeatCheckDao();
	
	private SeatCheckDao() {}
	
	public static SeatCheckDao getInstance() {
		return dao;
	}
	
	
	// 좌석선택 표시
	public void seat_Display(String movie_name, String movie_date, String movie_time) {
		String sql = "SELECT * FROM seat WHERE movie_code = "
				+ "(SELECT movie_code FROM ticketinfo WHERE movie_name = ? AND "
				+ "movie_date = ? AND movie_time = ?)";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SeatVo> list = new ArrayList<SeatVo>();
		SeatVo vo;
		
		String[][] seat = new String[5][9];
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, movie_name);
			pstmt.setString(2, movie_date);
			pstmt.setString(3, movie_time);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				vo = new SeatVo(rs.getString(1), rs.getString(2), rs.getString(3));
				list.add(vo);
				
				String seat_num = rs.getString(4);
				
				String seat_row = seat_num.substring(0, 1);
				int s_row = Integer.parseInt(seat_row);
				
				String seat_col = seat_num.substring(4, 5);
				int s_col = Integer.parseInt(seat_col);
				
				seat[s_row][s_col] = "◼︎";
				
			}
			
			int row = 1;
			for (int i = 1; i < 5; i++){
				System.out.print("\n" + row + "행  ");
				row++;
				for(int j = 1; j < 9; j++){
					if (seat[i][j] == null) {
						seat[i][j] = "◻︎";
					}
					System.out.print(j + seat[i][j] + "  ");
				}
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				pstmt.close();
				rs.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			OracleConnectionUtil.close(conn);
		}
	}	// seat_Display -end
	
	
	
	
	// 좌석선택 가능여부 확인
	public boolean seat_Check(String movie_name, String movie_date, String movie_time, String movie_seat) {
		String sql = "SELECT * FROM seat WHERE movie_code = "
				+ "(SELECT movie_code FROM ticketinfo WHERE movie_name = ? AND "
				+ "movie_date = ? AND movie_time = ?) AND movie_seat = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, movie_name);
			pstmt.setString(2, movie_date);
			pstmt.setString(3, movie_time);
			pstmt.setString(4, movie_seat);
			
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
		
		// movie_Check -end
	}
	
}
