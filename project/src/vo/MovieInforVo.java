package vo;


import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class MovieInforVo {
	private String movie_num;
	private String movie_name;
	private String run_time;
	private String movie_genre;
	private Date playdate;
	private String nation;
	private String movie_rating;
}