package vo;

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
public class MembersVo {
	// 아이디, 비밀번호
	private String mem_code;
	private String name;
	private String id;
	private String password;
	private String email;
	private String tel;
	private String movie_code;
}
