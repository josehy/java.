package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import movie.OracleConnectionUtil;
import vo.MembersVo;
import vo.SeatVo;
import vo.TicketingCheckVo;

public class TicketingCheckDao {
	public static TicketingCheckDao tdao = new TicketingCheckDao();
	
	private TicketingCheckDao() {}
	
	public static TicketingCheckDao getInstance() {
		return tdao;
	}
	
	// 이름 및 아이디로 예매여부 확인
	public boolean login_Check(MembersVo mvo) {
		String sql = "SELECT * FROM members WHERE name = ? AND id = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mvo.getName());
			pstmt.setString(2, mvo.getId());
			
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
		
	}	// login_Check -end
	
	
	// 이름 및 아이디로 예매내역 조회
	public List<TicketingCheckVo> name_Lookup(String name, String id) {
		String sql = "SELECT * FROM TICKETINFO WHERE movie_code IN "
				+ "(SELECT movie_code FROM PAYMENT WHERE mem_code = "
				+ "(SELECT MEM_CODE FROM MEMBERS WHERE name = ? AND id = ?))";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TicketingCheckVo> list = new ArrayList<TicketingCheckVo>();
		TicketingCheckVo tvo;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				tvo = new TicketingCheckVo(rs.getString(1), rs.getString(2), rs.getDate(3),
						rs.getString(4), rs.getString(5), rs.getInt(6));
				list.add(tvo);
			}
			return list;
			
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
		return null;
		
	}	// name_Lookup -end
	
	
	
	
	// 아이디 및 패스워드로 예매내역 조회
	public List<TicketingCheckVo> id_Lookup(String id, String password) {
		String sql = "SELECT * FROM TICKETINFO WHERE movie_code IN"
				+ "(SELECT movie_code FROM PAYMENT WHERE mem_code ="
				+ "(SELECT MEM_CODE FROM MEMBERS WHERE id = ? AND password = ?))";
		
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TicketingCheckVo> list = new ArrayList<TicketingCheckVo>();
		TicketingCheckVo tvo;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				tvo = new TicketingCheckVo(rs.getString(1), rs.getString(2), rs.getDate(3),
						rs.getString(4), rs.getString(5), rs.getInt(6));
				list.add(tvo);
			}
			return list;
			
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
		return null;
		
	}	// id_Lookup -end
	
	
	
	
	
	
	// 전화번호로 예매내역 조회
	public List<TicketingCheckVo> tel_Lookup(String tel) {
		String sql = "SELECT * FROM TICKETINFO WHERE movie_code IN "
				+ "(SELECT movie_code FROM PAYMENT WHERE mem_code = "
				+ "(SELECT MEM_CODE FROM MEMBERS WHERE tel = ?))";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TicketingCheckVo> list = new ArrayList<TicketingCheckVo>();
		TicketingCheckVo tvo;	// 질문하기(while 안에 들어가야 하는지)
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tel);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				tvo = new TicketingCheckVo(rs.getString(1), rs.getString(2), rs.getDate(3),
						rs.getString(4), rs.getString(5), rs.getInt(6));
				list.add(tvo);
			}
			return list;
			
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
		return null;
		
	}	// tel_Lookup -end
	
	
	// 결제코드로 예매내역 조회
	public List<TicketingCheckVo> code_Lookup(String pay_code) {
		String sql = "SELECT * FROM TICKETINFO WHERE movie_code IN "
				+ "(SELECT movie_code FROM PAYMENT WHERE pay_code = ?)";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		List<TicketingCheckVo> list = new ArrayList<TicketingCheckVo>();
		ResultSet rs = null;
		TicketingCheckVo tvo = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pay_code);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				tvo = new TicketingCheckVo(rs.getString(1), rs.getString(2), rs.getDate(3),
						rs.getString(4), rs.getString(5), rs.getInt(6));
				list.add(tvo);
			}
			return list;
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
		return null;
		
	}	// code_Lookup -end
	
	
	
	// 예매번호로 결제내역 조회(예매취소시 입력한 회원정보와 영화코드가 payment에 내용과 일치하는지 확인용)
	public boolean movie_Check(String movie_code, String id, String password) {
		String sql = "SELECT * FROM payment "
				+ "WHERE movie_code = ? "
				+ "AND mem_code = (SELECT mem_code FROM members "
				+ "WHERE id = ? AND password = ?)";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, movie_code);
			pstmt.setString(2, id);
			pstmt.setString(3, password);
			
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
		
	}	// movie_Check -end
	
	
	// 전화번호로 예매내역 찾고난 후 상영관 및 좌석번호 조회
	public List<SeatVo> seat_tel_lookup(String tel, String movie_code){
		String sql = "SELECT movie_code, movie_theater, movie_seat FROM seat "
				+ "WHERE mem_code = (SELECT MEM_CODE FROM MEMBERS WHERE tel = ?) "
				+ "AND movie_code = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SeatVo> list = new ArrayList<SeatVo>(); 
		SeatVo svo;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tel);
			pstmt.setString(2, movie_code);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				svo = new SeatVo(rs.getString(1), rs.getString(2), rs.getString(3));
				list.add(svo);
			}
			return list;
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
		return null;
	}
	
	
	
	// 이름 및 아이디로 예매내역 찾고난 후 상영관 및 좌석번호 조회
	public List<SeatVo> seat_name_lookup(String name, String id, String movie_code){
		String sql = "SELECT movie_code, movie_theater, movie_seat FROM seat "
				+ "WHERE mem_code = (SELECT MEM_CODE FROM MEMBERS WHERE name = ? AND id = ?) "
				+ "AND movie_code = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SeatVo> list = new ArrayList<SeatVo>(); 
		SeatVo svo;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, movie_code);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				svo = new SeatVo(rs.getString(1), rs.getString(2), rs.getString(3));
				list.add(svo);
			}
			return list;
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
		return null;
	}
	
	
	
	
	// 결제코드로 예매내역 찾고난 후 상영관 및 좌석번호 조회
	public List<SeatVo> seat_pay_code_lookup(String pay_code, String movie_code){
		String sql = "SELECT movie_code, movie_theater, movie_seat FROM seat "
				+ "WHERE mem_code = (SELECT MEM_CODE FROM PAYMENT WHERE pay_code = ?) "
				+ "AND movie_code = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SeatVo> list = new ArrayList<SeatVo>(); 
		SeatVo svo;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pay_code);
			pstmt.setString(2, movie_code);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				svo = new SeatVo(rs.getString(1), rs.getString(2), rs.getString(3));
				list.add(svo);
			}
			return list;
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
		return null;
	}
	
	
	
	// 아이디 및 패스워드 예매내역 찾고난 후 상영관 및 좌석번호 조회
	public List<SeatVo> seat_password_lookup(String id, String password, String movie_code){
		String sql = "SELECT movie_code, movie_theater, movie_seat FROM seat "
				+ "WHERE mem_code = (SELECT MEM_CODE FROM MEMBERS WHERE id = ? AND password = ?) "
				+ "AND movie_code = ?";
		
		Connection conn = OracleConnectionUtil.connect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SeatVo> list = new ArrayList<SeatVo>(); 
		SeatVo svo;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			pstmt.setString(3, movie_code);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				svo = new SeatVo(rs.getString(1), rs.getString(2), rs.getString(3));
				list.add(svo);
			}
			return list;
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
		return null;
	}
	
	
	
	

	
}	// class -end
