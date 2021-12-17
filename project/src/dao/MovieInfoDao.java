package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import movie.OracleConnectionUtil;
import vo.MovieInfoVo;
import vo.MovieInforVo;


public class MovieInfoDao {
		private static MovieInfoDao dao = new MovieInfoDao();
		private MovieInfoDao() {  }
		public static MovieInfoDao getInstance() {
			return dao;
		}

		public void insert(MovieInforVo vo) {
			String sql="select * from MovieInfo ";
			Connection conn = OracleConnectionUtil.connect();
			PreparedStatement pstmt=null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, vo.getMovie_num());    
		        pstmt.setString(2, vo.getMovie_name());		  
		        pstmt.setString(3, vo.getRun_time());
		        pstmt.setString(4, vo.getMovie_genre());
		        pstmt.setDate(5, vo.getPlaydate());
		        pstmt.setString(6, vo.getNation());
		        pstmt.setString(7, vo.getMovie_rating());
		      	pstmt.execute();
			} catch (SQLException e) {

				e.printStackTrace();
			}finally {
				try {
					pstmt.close();
				} catch (SQLException e) {	e.printStackTrace(); }
				OracleConnectionUtil.close(conn);
			}
			
	    }
		public List<MovieInfoVo> getList(String movie_name){
			Connection conn = OracleConnectionUtil.connect();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<MovieInfoVo> list = new ArrayList<MovieInfoVo>();
			String sql = "SELECT movie_date FROM TICKETINFO WHERE movie_name =? GROUP BY movie_date ORDER BY movie_date";
			MovieInfoVo vo;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, movie_name);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					vo = new MovieInfoVo(rs.getDate(1));
					list.add(vo);
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

