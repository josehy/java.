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
public class TicketingCheckVo {
	private String movie_code;
	private String movie_name;
	private Date movie_date;
	private String movie_time;
	private String movie_theater;
	private int movie_prive;
}
