package shop.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ShopBookDBBean {
	
	private static ShopBookDBBean instance 
		= new ShopBookDBBean();
	
	public static ShopBookDBBean getInstance() {
		return instance;
	}
	
	private ShopBookDBBean() {
		
	}
	
	//커넥션 풀로부터 커넥션 객체를 얻어내는 메소드
	private Connection getConnection() throws Exception {
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/shopping");
		return ds.getConnection();
	}
	
	//관리자 인증 메소드
	public int managerCheck(String id, String passwd) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd = "";
		int x = -1;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select managerPasswd from manager where managerId = ?");
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("managerPasswd");
				if(dbpasswd.equals(passwd)) {
					x = 1; //인증성공
				} else {
					x = 0; //비번 틀림
				}
			} else {
				x = -1; // 아디 없음
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) 
				try { rs.close();} catch(SQLException ex) {}
			
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
		return x;
	}
	
	//책등록 메소드
	public void insertBook(ShopBookDataBean book) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("insert into book values(?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, book.getBook_id());
			pstmt.setString(2, book.getBook_kind());
			pstmt.setString(3, book.getBook_title());
			pstmt.setInt(4, book.getBook_price());
			pstmt.setShort(5, book.getBook_count());
			pstmt.setString(6, book.getAuthor());
			pstmt.setString(7, book.getPublish_com());
			pstmt.setString(8, book.getPublish_date());
			pstmt.setString(9, book.getBook_image());
			pstmt.setString(10, book.getBook_content());
			pstmt.setByte(11, book.getDiscount_rate());
			pstmt.setTimestamp(12, book.getReg_date());
			
			pstmt.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
	}
		
	//이미 등록된 책을 검증
	public int registedBookconfirm(String kind, String bookName, String author) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;
			
		try {
			conn = getConnection();
				
			String sql = "select book_name from book";
			sql += "where book_kind = ? and book_name = ? and author = ?";
				
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kind);
			pstmt.setString(2, bookName);
			pstmt.setString(3, author);
				
			rs = pstmt.executeQuery();
				
			if(rs.next()) {
				x = 1; //�빐�떦 梨낆씠 �씠誘� �벑濡�
			} else {
				x = -1; //�빐�떦 梨� �뾾�쓬
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) 
				try { rs.close();} catch(SQLException ex) {}
			
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
		return x;
	}
	//전체 등록된 책의 수를 얻어내는 메소드
	public int getBookCount() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		int x = 0;
			
		try {
			conn = getConnection();
				
			pstmt = conn.prepareStatement("select count(*) from book");
			rs = pstmt.executeQuery();
				
			if(rs.next())
				x = rs.getInt(1);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) 
				try { rs.close();} catch(SQLException ex) {}
			
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
		return x;
	}
		
	//분류별 또는 전체 등록된 책의 정보를 얻어내는 메소드
	public List<ShopBookDataBean> getBooks(String book_kind) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<ShopBookDataBean> bookList = null;
		
		try {
			conn = getConnection();
			
			String sql1 = "select * from book";
			String sql2 = "select * from book where book_kind=?";
			sql2 += "order by reg_date desc";
			
			if(book_kind.equals("all")) {
				pstmt = conn.prepareStatement(sql1);
			} else {
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, book_kind);
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bookList = new ArrayList<ShopBookDataBean>();
				do {
					ShopBookDataBean book = new ShopBookDataBean();
					
					book.setBook_id(rs.getInt("book_id"));
					book.setBook_kind(rs.getString("book_kind"));
					book.setBook_title(rs.getString("book_title"));
					book.setBook_price(rs.getInt("book_price"));
					book.setBook_count(rs.getShort("book_count"));
					book.setAuthor(rs.getString("author"));
					book.setPublish_com(rs.getString("publish_com"));
					book.setPublish_date(rs.getString("publish_date"));
					book.setBook_image(rs.getString("book_image"));
					book.setDiscount_rate(rs.getByte("discount_rate"));
					book.setReg_date(rs.getTimestamp("reg_date"));
					
					bookList.add(book);
				} while(rs.next());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) 
				try { rs.close();} catch(SQLException ex) {}
			
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
		return bookList;
	}
	
	//쇼핑몰 메인에 포시하기 위해 사용하는 분류별 신간 목록을 얻어내는 메소드
	public ShopBookDataBean[] getBooks(String book_kind, int count) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ShopBookDataBean bookList[] = null;
		int i = 0;
		
		try {
			conn = getConnection();
			
			String sql = "select * from book where book_kind = ?";
			sql += "order by reg_date desc limit ?,?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, book_kind);
			pstmt.setInt(2, 0);
			pstmt.setInt(3, count);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bookList = new ShopBookDataBean[count];
				do {
					ShopBookDataBean book = new ShopBookDataBean();
					book.setBook_id(rs.getInt("book_id"));
					book.setBook_kind(rs.getString("book_kind"));
					book.setBook_title(rs.getString("book_title"));
					book.setBook_price(rs.getInt("book_price"));
					book.setBook_count(rs.getShort("book_count"));
					book.setAuthor(rs.getString("author"));
					book.setPublish_com(rs.getString("publish_com"));
					book.setPublish_date(rs.getString("publish_date"));
					book.setBook_image(rs.getString("book_image"));
					book.setDiscount_rate(rs.getByte("discount_rate"));
					book.setReg_date(rs.getTimestamp("reg_date"));
					
					bookList[i] = book;
					
					i++;
				} while(rs.next());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) 
				try { rs.close();} catch(SQLException ex) {}
			
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
		return bookList;
	}
	
	//bookId�뿉 �빐�떦�븯�뒗 梨낆쓽 �젙蹂대�� �뼸�뼱�궡�뒗 硫붿냼�뱶
	//�벑濡앸맂 梨낆쓣 �닔�젙�븯湲� �쐞�빐 �닔�젙 �뤌�쑝濡� �씫�뼱�삤�뒗 硫붿냼�뱶
	public ShopBookDataBean getBook(int bookId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ShopBookDataBean book = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select * from book where book_id = ?");
			pstmt.setInt(1, bookId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				book = new ShopBookDataBean();
				
				book.setBook_kind(rs.getString("book_kind"));
				book.setBook_title(rs.getString("book_title"));
				book.setBook_price(rs.getInt("book_price"));
				book.setBook_count(rs.getShort("book_count"));
				book.setAuthor(rs.getString("author"));
				book.setPublish_com(rs.getString("publish_com"));
				book.setPublish_date(rs.getString("publish_date"));
				book.setBook_image(rs.getString("book_image"));
				book.setBook_content(rs.getString("book_content"));
				book.setDiscount_rate(rs.getByte("discount_rate"));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) 
				try { rs.close();} catch(SQLException ex) {}
			
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
		return book;
	}
	
	//�벑濡앸맂 梨낆쓽 �젙蹂대�� �닔�젙�떆 �궗�슜�븯�뒗 硫붿냼�뱶
	public void updateBook(ShopBookDataBean book, int bookId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn = getConnection();
			
			sql = "update book set book_kind=?, book_title=?, book_price=?";
			sql += ", book_count=?, author=?, publish_com=?, publish_date=?";
			sql += ",book_image=?, book_content=?, discount_rate=?";
			sql += " where book_id=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, book.getBook_kind());
			pstmt.setString(2, book.getBook_title());
			pstmt.setInt(3, book.getBook_price());
			pstmt.setShort(4, book.getBook_count());
			pstmt.setString(5, book.getAuthor());
			pstmt.setString(6, book.getPublish_com());
			pstmt.setString(7, book.getPublish_date());
			pstmt.setString(8, book.getBook_image());
			pstmt.setString(9, book.getBook_content());
			pstmt.setByte(10, book.getDiscount_rate());
			pstmt.setInt(11, bookId);
			
			pstmt.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
	}
	
	//bookId�뿉 �빐�떦�븯�뒗 梨낆쓽 �젙蹂대�� �궘�젣�떆 �궗�슜�븯�뒗 硫붿냼�뱶
	public void deleteBook(int bookId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("delete from book where book_id=?");
			pstmt.setInt(1, bookId);
			
			pstmt.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) 
				try { rs.close();} catch(SQLException ex) {}
			
			if(pstmt != null) 
				try { pstmt.close();} catch(SQLException ex) {}
			
			if(conn != null) 
				try { conn.close();} catch(SQLException ex) {}
			
		}
	}
}
