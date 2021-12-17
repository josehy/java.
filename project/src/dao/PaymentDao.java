package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import movie.OracleConnectionUtil;
import vo.MovieTheaterVo;
import vo.PayinfoVo;

public class PaymentDao {
	private static PaymentDao dao = new PaymentDao();
	
	private PaymentDao() {	}
	
	public static PaymentDao getInstance() {
		return dao;
	}
	
	// payment 및 seat insert
	public List<PayinfoVo> payinfo(String movie_name, String movie_date, String movie_time, 
			String id, int pay_num, String movie_theater, String movie_seat){
		String sql = "SELECT m.mem_code, t.movie_code, movie_price FROM MEMBERS m ,TICKETINFO t "
				+ "WHERE t.movie_name = ? AND t.movie_date = ? AND movie_time = ? "
				+ "AND m.mem_code = (SELECT mem_code FROM members WHERE id = ?)";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		PayinfoVo vo;
		MovieTheaterVo tvo;
		List<PayinfoVo> list = new ArrayList<PayinfoVo>();
		
		String mem_code;
		String movie_code;
		int movie_price;
		
		try {
			pstmt1 = conn.prepareStatement(sql);
			
			pstmt1.setString(1, movie_name);
			pstmt1.setString(2, movie_date);
			pstmt1.setString(3, movie_time);
			pstmt1.setString(4, id);
			
			rs1 = pstmt1.executeQuery();
			
			rs1.next();
			vo = new PayinfoVo(rs1.getString(1), rs1.getString(2), rs1.getInt(3));
			list.add(vo);
			
			mem_code = rs1.getString(1);
			movie_code = rs1.getString(2);
			movie_price = rs1.getInt(3);

			
			
			// payment 테이블 insert
			sql = "INSERT INTO PAYMENT(pay_code, mem_code, movie_code, pay_num, "
					+ "movie_price, pay_price) "
					+ "VALUES(pay_code.nextval, ?, ?, ?, ?, ?)";
			
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setString(1, mem_code);
			pstmt2.setString(2, movie_code);
			pstmt2.setInt(3, pay_num);
			pstmt2.setInt(4, movie_price);
			pstmt2.setInt(5, pay_num * movie_price);
			
			pstmt2.execute();
			
			
			
			// movie_theater 찾기
			sql = "SELECT movie_theater FROM TICKETINFO WHERE movie_code = ?";
			
			List<MovieTheaterVo> t_list = new ArrayList<MovieTheaterVo>();
			
			pstmt3 = conn.prepareStatement(sql);
			pstmt3.setString(1, movie_code);
			
			rs2 = pstmt3.executeQuery();
			
			rs2.next();
			tvo = new MovieTheaterVo(rs2.getString(1));
			t_list.add(tvo);
			
			movie_theater = rs2.getString(1);
			
			
			
			// seat 테이블 insert
			sql = "INSERT INTO seat(mem_code, movie_theater, movie_code, movie_seat) "
					+ "VALUES(?, ?, ?, ?)";
			
			pstmt4 = conn.prepareStatement(sql);
			pstmt4.setString(1, mem_code);
			pstmt4.setString(2, movie_theater);
			pstmt4.setString(3, movie_code);
			pstmt4.setString(4, movie_seat);
			
			pstmt4.execute();
			
			return list;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				pstmt1.close();
				pstmt2.close();
				pstmt3.close();
				pstmt4.close();
				rs1.close();
				rs2.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			OracleConnectionUtil.close(conn);
		}
		return null;
	}
	
	
	
}
