package movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import dao.CancleDao;
import dao.LoginDao;
import dao.MovieInfoDao;
import dao.PaymentDao;
import dao.SeatCheckDao;
import dao.SignupDao;
import dao.TicketInfoDao;
import dao.TicketingCheckDao;
import vo.MembersVo;
import vo.MovieInfoVo;
import vo.PayinfoVo;
import vo.SeatVo;
import vo.TicketInfoVo;
import vo.TicketingCheckVo;

public class main {
	// 좌석지정을 위한 배열
	public static String[][] seat = new String[5][9];

	public void resetSeat(){
		for (int i = 1; i < 5; i++){
			for(int j = 1; j < 9; j++){
				seat[i][j] = "___";
			}
		}
	}	// resetSeat -end
	
	// 좌석표시
	public void reference(){
		int row = 1;
		for (int i = 1; i < 5; i++){
			System.out.print("\n" + row + "행  ");
			row++;
			for(int j = 1; j < 9; j++){
				String seat = (this.seat[i][j].equals("___"))?"◻︎":"◼︎";
				System.out.print((j) + seat + "  ");
			}
		}
		System.out.println();
	}	// reference -end
	
	// 이메일 형식 일치여부 판단
	public static boolean isEmail(String email) {
		if (email == null) return false;
		boolean rr = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim()); 
	     //email.trim() :양옆 공백제거
		// Pattern matches :정규패턴  
		return rr;
	}
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		LoginDao ldao = LoginDao.getInstance();
		TicketingCheckDao tdao = TicketingCheckDao.getInstance();
		CancleDao cdao = CancleDao.getInstatnce();
	    MovieInfoDao mdao = MovieInfoDao.getInstance();
	    TicketInfoDao idao = TicketInfoDao.getInstance();
	    SignupDao sdao = SignupDao.getInstance();
		MembersVo mvo;
		PaymentDao pdao = PaymentDao.getInstance();
		SeatCheckDao scdao = SeatCheckDao.getInstance();
		SeatVo svo;
		
		while(true) {
			System.out.println("::: 원하시는 메뉴의 숫자를 입력해주세요. :::");
			System.out.println("1. 회원가입\n2. 로그인\n3. 예매내역조회\n4. 예매취소\n5. 종료");
			System.out.print("숫자입력 >>> ");
			int select = sc.nextInt();
			sc.nextLine();
			
			if (select == 1) {	// 회원가입
				String id = null, password = null, password2 = null, name = null, email = null;
				String tel = null;
				
				System.out.println(" 영화 그 이상의 감동\n" + "CGV에 오신걸 환영합니다.\n ");
				System.out.println("============== 회원가입 ==============");

				while (true) {	// 아이디 형식 일치 및 중복여부 확인
					System.out.print("ID를 입력하세요 ===> ");
					id = sc.nextLine().trim();
					if (sdao.idcheck(id)) {
						if (id.length() <= 20) {
							System.out.println(id + "은 사용 가능한 아이디 입니다.");
							break;
						} else {
							System.out.println("아이디가 너무 깁니다 다시 확인해주세요.");
						}
					}
					else {
						System.out.println("::: 사용 중인 ID 입니다. :::");
					}
				}	// while -end

				while (true) {	// 비밀번호 형식 일치여부 확인
					System.out.print("\n비밀번호를 입력하세요 ===> ");
					password = sc.nextLine().trim();
					if (password.length() >= 8 && password.length() <= 20) {
						System.out.print("\n비밀번호를 한번 더 입력하세요 ===> ");
						password2 = sc.nextLine();
						if (password.equals(password2)) {
							System.out.print("\n이름을 입력하세요 ===> ");
							name = sc.nextLine().trim();
							break;
						}
						else {	// 비밀번호 확인 불일치
							System.out.println("다르게 입력하셨습니다. 다시 입력하세요.");
						}
					}
					else {
						System.out.println("비밀번호는 8~20자리 이내로 입력하세요");
					}
				}	// while -end

				while(true) {	// 이메일 입력 및 형식 일치여부 확인
					System.out.print("\nEmail를 입력하세요 ===> ");
					email = sc.nextLine();
					if(isEmail(email)) {
						break;
						}
					else {
						System.out.println("올바르지 않은 이메일 형식입니다. Ex:)sample@naver.com");
					};
				}
				
				while (true) {	// 전화번호 입력 및 자릿수 일치여부 확인
					System.out.print("\n숫자만을 포함한 전화번호 11자리를 입력하세요 ===> ");
					tel = sc.nextLine().trim();
					
					if (tel.length() == 11) {
						break;
					}
					else {
						System.out.println("전화번호 입력이 위 조건과 일치하지 않습니다.");
					}
				}	// while -end

				mvo = MembersVo.builder().id(id).password(password).name(name).email(email).tel(tel).build();
				sdao.insert(mvo);
				System.out.println("\n축하합니다 가입이 완료되었습니다.");
				System.out.println("===================================");
				
			}	// select1 -end
			
			else if (select == 2) {	// 로그인
				System.out.println("\n::: 로그인을 위해 아이디 및 비밀번호를 입력해주세요. :::");
				System.out.print("아이디 >>> ");
				String id = sc.nextLine();
				System.out.print("비밀번호 >>> ");
				String password = sc.nextLine();
				
				mvo = MembersVo.builder().id(id).password(password).build();
				
				if (ldao.idCheck(mvo)) {	// 아이디 가입여부 확인(로그인 실패)
					System.out.println("\n::: 아이디 혹은 비밀번호가 일치하지 않습니다. :::\n");
				}
				else {	// 로그인 성공
					System.out.println("\n::: 로그인이 완료되었습니다. :::");
					
					while(true) {	// 로그인 후 메뉴선택
						System.out.println("\n::: 원하시는 메뉴를 입력해주세요 :::");
						System.out.println("1. 영화예매\n2. 예매내역 조회\n3. 예매취소\n4. 로그아웃");
						System.out.print("숫자입력 >>> ");
						int select2 = sc.nextInt();
						sc.nextLine();

						
						if (select2 == 1) {	// 영화예매
							PreparedStatement pstmt = null;
							ResultSet rs = null;
							Connection conn = OracleConnectionUtil.connect();
							String sql = "SELECT * FROM MOVIEINFO";
							main cr = new main();
							cr.resetSeat();
							int selectNum, row, col;
							String user = null;
							String movie_name, movie_date, movie_time;
							
							try {
								pstmt = conn.prepareStatement(sql);
								rs = pstmt.executeQuery(); 
								
								System.out.println("\n::: 영화정보  :::");
								System.out.println("::: 영화 리스트 입니다. :::");
								
							    while(rs.next()) {
								System.out.println("-----------------------------------------------------------------------");
								System.out.println("예매순위:" + rs.getString("movie_num"));
								System.out.print ( rs.getString("movie_name") + "[" + rs.getString("MOVIE_rating") +"/"+
										rs.getString("movie_genre") + "/" + rs.getString("nation") +" "+ rs.getDate("playdate") + "]");
								System.out.print("\t");
								System.out.println("상영시간: " + rs.getString("RUN_TIME"));
								System.out.println("-----------------------------------------------------------------------");
							    }
						    }
							catch (SQLException e) {
						       System.out.println("SQL 실행에 오류가 발생했습니다. : " + e.getMessage());
						    }
							finally {
								try {
									pstmt.close();
									rs.close();
								}
								catch (SQLException e) {
									e.printStackTrace();
								}
							}
							
				         	while(true) {	// 예매 진행 및 취소중 선택
								System.out.println("::: 1.예매  2.취소 :::");
								System.out.print("번호 입력 >>> ");
								int choice = sc.nextInt();
								sc.nextLine();
								
								if(choice == 1) {			
									while(true) {	// 희망 영화 선택
										System.out.print("\n원하시는 영화를 입력해주세요. >>> ");
										movie_name = sc.nextLine();
										List<MovieInfoVo> list = mdao.getList(movie_name);
										if(list.size() == 0) {
											System.out.println("\n::: 잘못 입력하셨습니다. :::");
											continue;
										}
										else {
											System.out.println("\n::: 상영일자 정보 :::");
											for (MovieInfoVo vo : list) {
												System.out.println(vo);
											}
											break;
										}
									}
									
									StringBuffer str;	// 희망날짜 선택
									while(true) {
										System.out.println("\n::: 원하시는 날짜를 입력해주세요. :::");
										System.out.print("희망날짜(dd 형식으로 입력해주세요.) >>> ");
										movie_date = sc.nextLine();
										str = new StringBuffer(movie_date);
										str.insert(0, "2021-09-");
										List<TicketInfoVo>list1 = idao.get_List(str.toString(), movie_name);
										
										if(list1.size() == 0) {
											System.out.println("\n::: 잘못 입력하셨습니다. :::");
											continue;
										}
										else {
											System.out.println("\n::: 상영시간 정보 :::");
											for(TicketInfoVo vo1 : list1) {
												System.out.println(vo1);
											}
										 	break;
										}
									}
	
									while(true) {	// 희망시간 선택
										System.out.println("\n::: 원하시는 시간을 입력해주세요. :::");
										System.out.print("희망시간(hh:mm 형식으로 입력해주세요.) >>> ");
										movie_time = sc.nextLine();
						                List<TicketInfoVo>list1 = idao.get_TimeList(str.toString(), movie_name, movie_time);
						                
						                if(list1.size() == 0) {
						                System.out.println("\\n::: 잘못 입력하셨습니다. :::");
						                      continue;
						                }
						                else {
						                	break;
										}
									}	// while 희망시간 선택 -end
									
									System.out.println("\n----------------------------------------------------------");
									System.out.println("::: 선택하신 영화 정보입니다. :::");
									System.out.println("영화제목 :"+ movie_name + "\n날짜:" +str.toString()+ "  영화시간"+ movie_time);
									System.out.println("----------------------------------------------------------");
									System.out.println("\n::: 예매를 진행하시겠습니까? 1:네 2:아니오 :::");
									System.out.print("번호입력 >>> ");
									int numm = sc.nextInt();
									
									if (numm == 1) {	// 예매진행
										String movie_theater = null;
										String movie_seat = null;
										System.out.println("\n::: 인원수를 선택하세요. :::");
										System.out.print("인원수 입력(숫자) >>> ");
										int pay_num = sc.nextInt();
										user = sc.nextLine();
										System.out.println("\n::: 좌석 선택창으로 넘어갑니다. :::");
										
										while (true) {
											System.out.println("\n::: 1.좌석위치 조회 2.좌석선택 3.종료(기능) :::");
											System.out.println("::: 원하시는 서비스를 선택해주세요. :::");
											System.out.print("번호입력 >>> ");
											selectNum = sc.nextInt();
											
											if (selectNum == 1){	// 좌석위치 조회
												System.out.println("\n::: 좌석조회 서비스 입니다. :::");
												scdao.seat_Display(movie_name, str.toString(), movie_time);
												System.out.println("");
											}
											
											else if (selectNum == 2){	// 좌석선택
												scdao.seat_Display(movie_name, str.toString(), movie_time);
												for (int i = 0; i < pay_num; i++) {
													while(true) {
														System.out.print("\n행을 선택하세요.(1 ~ 4) >>> ");
														row = sc.nextInt();
														System.out.print("열을 선택하세요.(1 ~ 8) >>> ");
														col = sc.nextInt();
														
														movie_seat = row + " - " + col;
														
														if (scdao.seat_Check(movie_name, str.toString(), movie_time, movie_seat)) {
															if (seat[row][col].equals("___")) {
																seat[row][col] = "◼︎";
																
																List<PayinfoVo> p_list = pdao.payinfo
																		(movie_name, str.toString(), movie_time, id, 
																				pay_num, movie_theater, movie_seat);
																
																scdao.seat_Display(movie_name, str.toString(), movie_time);
																System.out.println("\n"+row+" - "+col+"번 좌석이 예약되었습니다.\n");
															}
															else {
																System.out.println("\n::: 이미 예약된 좌석입니다. 다른 좌석을 선택해주세요. :::\n");
																scdao.seat_Display(movie_name, str.toString(), movie_time);
															}
															break;
														}
														else {
															System.out.println("::: 이미 예약된 좌석입니다. 다른 좌석을 선택해주세요. :::");
															continue;
														}
													}	// while -end
												}
												break;
											}	// selecNum2 -end
											
											else if (selectNum == 3) {
												System.out.println("서비스를 종료합니다.");
												break; 	
											}
										}	// while -end
										break;
									}	// numm1 -end
									else if (numm == 2) {	// 예매진행 취소
										System.out.println("서비스를 종료합니다.");
										break;
									}
									
								}	// if choice 1 -end
								else if(choice==2){
									System.out.println("서비스를 종료합니다.");
									break;
								}
						    }
									
							
						}	// select2-1 -end

						
						else if (select2 == 2) {	// 예매내역 조회
							List<TicketingCheckVo> list = tdao.id_Lookup(id, password);
							if (list.size() == 0) {
								System.out.println("\n::: 예매내역이 없습니다. :::");
							}
							else {
								System.out.println("\n::: 예매내역을 불러옵니다. :::");
								for (TicketingCheckVo vo : list) {
									System.out.println(vo);
								}
								while(true) {
									System.out.println("\n::: 상영관 및 좌석정보를 조회하시겠습니까? :::");
									System.out.println("::: 1. 예(해당영화의 movie_code를 기억해 주십시오.)\t\t 2. 아니요. :::");
									System.out.print("번호입력 >>> ");
									int select3 = sc.nextInt();
									sc.nextLine();
									
									if (select3 == 1) {	// 영화코드 입력 및 일치여부 확인
										while(true) {
											System.out.print("영화코드(movie_code) 입력 >>> ");
											String movie_code = sc.nextLine();
											List<SeatVo> s_list = tdao.seat_password_lookup(id, password, movie_code);
											if (s_list.size() == 0) {
												System.out.println("\n::: 영화코드가 일치하지 않습니다. :::");
												break;
											}
											else {
												System.out.println("\n::: 상영관 및 좌석정보를 불어옵니다. :::");
												for (SeatVo vo : s_list) {
													System.out.println(vo);
												}
												System.out.println("");
												break;
											}
										}	// while -end
									}	// select3-1 -end
									else {
										System.out.println("\n::: 메뉴화면으로 돌아갑니다. :::");
										break;
									}	// else -end
								}	// while -end
							}	// else -end
						}	// select2-2 -end
						
						
						
						
						else if (select2 == 3) {	// 예매취소
							System.out.println("\n::: 본인확인을 위해 비밀번호를 입력해주세요. :::");
							System.out.print("비밀번호 >>> ");
							password = sc.nextLine();
							
							mvo = MembersVo.builder().id(id).password(password).build();
							if (ldao.idCheck(mvo)) {
								System.out.println("\n::: 비밀번호가 일치하지 않습니다. :::");
							}
							else {
								List<TicketingCheckVo> list = tdao.id_Lookup(id, password);
								if (list.size() == 0) {
									System.out.println("\n::: 예매내역이 없습니다. :::");
								}
								else {
									System.out.println("\n::: 예매내역을 불러옵니다. :::");
									for (TicketingCheckVo vo : list) {
										System.out.println(vo);
									}
									System.out.println("\n::: 취소할 영화의 movie_code를 기억하셔야합니다. :::");
									System.out.println("::: 예매내역을 취소하시겠습니까? :::");
									System.out.println("1. 예\t\t2.아니요");
									System.out.print("번호입력 >>> ");
									int select3 = sc.nextInt();
									sc.nextLine();
									
									if (select3 == 1) {
										System.out.println("\n:::예매번호(movie_code)를 입력해주세요. :::");
										System.out.print("예매번호 >>> ");
										String movie_code = sc.nextLine();
										
										// 예매번호 일치여부 확인
										if (tdao.movie_Check(movie_code, id, password)) {
											System.out.println("예매번호가 올바르지 않습니다.");
										}
										else {	// 예매번호 일치 및 예매취소
											mvo.setMovie_code(movie_code);
											cdao.Cancle(mvo);
											System.out.println("\n::: 예매취소가 완료되었습니다. :::");
										}
										
										System.out.println("::: 로그인 화면으로 돌아갑니다. :::");
									}
									else {	// 예매취소 안하고 돌아가기
										System.out.println("\n::: 로그인 화면으로 돌아갑니다. :::");
									}
								}	// else 예매내역 불러오기 -end
								
							}
						}	// select2-3 -end
						
						
						
						else if (select2 == 4) {	// 로그아웃
							System.out.println("\n::: 정상적으로 로그아웃 되었습니다. :::");
							System.out.println("::: 메인화면으로 돌아갑니다. :::\n");
							break;
						}	// select2-4 -end
						
					}	// select2 while -end
				}	// login -end
				
			}	// select2 -end
			
			else if (select == 3) {	// 로그인 없이 예매조회 및 방법선택
				System.out.println("\n::: 내역조회 방법을 선택해주세요 :::");
				System.out.println("1. 이름 및 아이디 입력\n2. 전화번호 입력\n3. 결제코드 입력\n4. 뒤로가기");
				System.out.print("숫자입력 >>> ");
				int select2 = sc.nextInt();
				sc.nextLine();
				
				if (select2 == 1) {	// 이름 및 아이디 입력을 통한 조회
					System.out.println("\n::: 예매조회를 위해 이름 및 아이디를 입력해주세요. :::");
					System.out.print("이름  >>> ");
					String name = sc.nextLine();
					
					System.out.print("아이디 >>> ");
					String id = sc.nextLine();
					
					List<TicketingCheckVo> list = tdao.name_Lookup(name, id);
					if (list.size() == 0) {
						System.out.println("\n::: 예매내역이 없거나 입력정보가 일치하지 않습니다. :::");
						System.out.println("입력한 이름 : " + name);
						System.out.println("입력한 아이디 : " + id + "\n");
					}
					else {
						System.out.println("\n::: 예매내역을 불러옵니다. :::");
						for (TicketingCheckVo vo : list) {
							System.out.println(vo);
						}
						while(true) {
							System.out.println("\n::: 상영관 및 좌석정보를 조회하시겠습니까? :::");
							System.out.println("::: 1. 예(해당영화의 movie_code를 기억해 주십시오.)\t\t 2. 아니요. :::");
							System.out.print("번호입력 >>> ");
							int select3 = sc.nextInt();
							sc.nextLine();
							
							if (select3 == 1) {	// 영화코드 입력 및 일치여부 확인
								while(true) {
									System.out.print("영화코드(movie_code) >>> ");
									String movie_code = sc.nextLine();
									List<SeatVo> s_list = tdao.seat_name_lookup(name, id, movie_code);
									if (s_list.size() == 0) {
										System.out.println("\n::: 영화코드가 일치하지 않습니다. :::");
										break;
									}
									else {
										System.out.println("\n::: 상영관 및 좌석정보를 불어옵니다. :::");
										for (SeatVo vo : s_list) {
											System.out.println(vo);
										}
										System.out.println("");
										break;
									}
								}	// while -end
							}	// select3-1 -end
							else {
								System.out.println("\n::: 메뉴화면으로 돌아갑니다. :::");
								break;
							}	// else -end
						}	// while -end
					}	// else -end
				}	// select2-1 -end
				
				else if (select2 == 2) {	// 전화번호를 통한 조회
					System.out.println("\n::: 예매조회를 위해 전화번호를 입력해주세요. :::");
					System.out.print("전화번호 >>> ");
					String tel = sc.nextLine();
					tel = tel.replace("-", "");
					tel = tel.replace(" ", "");
					
					List<TicketingCheckVo> list = tdao.tel_Lookup(tel);
					if (list.size() == 0) {	// 조회 실패
						StringBuffer str = new StringBuffer(tel);
						if (str.length() >= 3) {
							str.insert(3, "-");
							if (str.length() >= 8) {
								str.insert(8, "-");
							}
						}
						System.out.println("\n::: 예매내역이 없거나 입력정보가 일치하지 않습니다. :::");
						System.out.println("입력한 전화번호 : " + str + "\n");
					}
					else {	// 조회 성공
						System.out.println("\n::: 예매내역을 불러옵니다. :::");
						for (TicketingCheckVo tvo : list) {
							System.out.println(tvo);
						}
						while(true) {
							System.out.println("\n::: 상영관 및 좌석정보를 조회하시겠습니까? :::");
							System.out.println("::: 1. 예(해당영화의 movie_code를 기억해 주십시오.)\t\t 2. 아니요. :::");
							System.out.print("번호입력 >>> ");
							int select3 = sc.nextInt();
							sc.nextLine();
							
							if (select3 == 1) {	// 영화코드 입력 및 일치여부 확인
								while(true) {
									System.out.print("영화코드(movie_code) 입력 >>> ");
									String movie_code = sc.nextLine();
									List<SeatVo> s_list = tdao.seat_tel_lookup(tel, movie_code);
									if (s_list.size() == 0) {
										System.out.println("\n::: 영화코드가 일치하지 않습니다. :::");
										break;
									}
									else {
										System.out.println("\n::: 상영관 및 좌석정보를 불어옵니다. :::");
										for (SeatVo vo : s_list) {
											System.out.println(vo);
										}
										System.out.println("");
										break;
									}
								}	// while -end
							}	// select3-1 -end
							else {
								System.out.println("\n::: 메뉴화면으로 돌아갑니다. :::");
								break;
							}	// else -end
						}
					}	// else 조회성공 -end
				}	// select2-2 -end
				
				
				
				
				else if (select2 == 3) { // 결재코드를 통한 조회
					System.out.println("\n::: 예매조회를 위해 결제코드를 입력해주세요. :::");
					System.out.print("결제코드 >>> ");
					String pay_code = sc.nextLine();
					
					List<TicketingCheckVo> list = tdao.code_Lookup(pay_code);
					if (list.size() == 0) {	// 조회 실패
						System.out.println("\n::: 입력정보가 일치하지 않습니다. :::");
						System.out.println("입력한 결제코드 : " + pay_code + "\n");
					}
					else {
						System.out.println("\n::: 예매내역을 불러옵니다. :::");
						for (TicketingCheckVo tvo : list) {
							System.out.println(tvo);
						}
						while(true) {
							System.out.println("\n::: 상영관 및 좌석정보를 조회하시겠습니까? :::");
							System.out.println("::: 1. 예(해당영화의 movie_code를 기억해 주십시오.)\t\t 2. 아니요. :::");
							System.out.print("번호입력 >>> ");
							int select3 = sc.nextInt();
							sc.nextLine();
							
							if (select3 == 1) {	// 영화코드 입력 및 일치여부 확인
								while(true) {
									System.out.print("영화코드(movie_code) 입력 >>> ");
									String movie_code = sc.nextLine();
									List<SeatVo> s_list = tdao.seat_pay_code_lookup(pay_code, movie_code);
									if (s_list.size() == 0) {
										System.out.println("\n::: 영화코드가 일치하지 않습니다. :::");
										break;
									}
									else {
										System.out.println("\n::: 상영관 및 좌석정보를 불어옵니다. :::");
										for (SeatVo vo : s_list) {
											System.out.println(vo);
										}
										System.out.println("");
										break;
									}
								}	// while -end
							}	// select3-1 -end
							else {
								System.out.println("\n::: 메뉴화면으로 돌아갑니다. :::");
								break;
							}	// else -end
						}	// while -end
					}	// else -end
				}	// select2-3 -end
				
				
				else if (select2 == 4) {	// 뒤로가기
					System.out.println("\n::: 메인화면으로 돌아갑니다. :::");
				}
				
				else {
					System.out.println("\n::: 올바른 값을 입력해주세요. :::");
					System.out.println("입력값 : " + select2);
					System.out.println("");
				}	// select2-4 -end
				
			}	// select3 -end
			
			
			
			else if (select == 4) {	// 예매취소
				System.out.println("\n::: 본인확인을 위해 아이디 및 비밀번호를 입력해주세요. :::");
				System.out.print("아이디 >>> ");
				String id = sc.nextLine();
				
				System.out.print("비밀번호 >>> ");
				String password = sc.nextLine();
				
				mvo = MembersVo.builder().id(id).password(password).build();
				if (ldao.idCheck(mvo)) {	// 조회 실패
					System.out.println("\n::: 아이디 혹은 비밀번호가 일치하지 않습니다. :::\n");
				}
				else {	// 조회 성공
					List<TicketingCheckVo> list = tdao.id_Lookup(id, password);
					if (list.size() == 0) {
						System.out.println("\n::: 예매내역이 없습니다. :::");
					}
					else {
						System.out.println("\n::: 예매내역을 불러옵니다. :::");
						for (TicketingCheckVo vo : list) {
							System.out.println(vo);
						}
						System.out.println("\n::: 취소할 영화의 movie_code를 기억하셔야합니다. :::");
						System.out.println("::: 예매내역을 취소하시겠습니까? :::");
						System.out.println("1. 예\t\t2.아니요");
						System.out.print("번호입력 >>> ");
						int select2 = sc.nextInt();
						sc.nextLine();
						
						if (select2 == 1) {
							System.out.println("\n:::예매번호(movie_code)를 입력해주세요. :::");
							System.out.print("예매번호 >>> ");
							String movie_code = sc.nextLine();
							
							// 예매번호 일치여부 확인
							if (tdao.movie_Check(movie_code, id, password)) {
								System.out.println("예매번호가 올바르지 않습니다.");
							}
							else {	// 예매번호 일치 및 예매취소
								mvo.setMovie_code(movie_code);
								cdao.Cancle(mvo);
								System.out.println("\n::: 예매취소가 완료되었습니다. :::");
							}
							
							System.out.println("::: 메인화면으로 돌아갑니다. :::\n");
						}
						else {	// 예매취소 안하고 돌아가기
							System.out.println("\n::: 메인화면으로 돌아갑니다. :::\n");
						}
					}	// else 예매내역 불러오기 -end
				}	// else 조회성공 -end
			}	// select4 -end
			
			
			else if (select == 5) {
				System.out.println("프로그램을 종료합니다.");
				break;
			}	// select5 -end
			
			else {
				System.out.println("\n::: 올바른 숫자를 입력해주세요. :::\n");
				continue;
			}	// select5 -end
			
		}	// while -end
		
		sc.close();
	}	// main -end
	

}	// class -end
