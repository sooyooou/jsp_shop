package shop.customer;

import java.sql.*;
import javax.naming.*;
import javax.sql.*;

public class CustomerDBBean {

	private static CustomerDBBean instance
	= new CustomerDBBean();
	
	public static CustomerDBBean getInstance() {
		return instance;
	}
	
	private CustomerDBBean() {}
	
	private Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/shopdb");
		return ds.getConnection();
	}
	
	
	//ȸ������
	public void insertMember(CustomerDataBean member)
	throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(
					"insert into member values(?,?,?,?,?,?,?,?)"
					);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getName());
			pstmt.setTimestamp(4, member.getReg_data());
			pstmt.setString(5,member.getTel());
			pstmt.setString(6, member.getAddress());
			
			pstmt.executeUpdate();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
	}

	//����� ����ó��
	public int userCheck(String id, String passwd)
	throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd="";
		int x=-1;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(
					"select passwd from member where id=?"
					);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("passwd");
				if(dbpasswd.equals(passwd))
					x=1; //��������
				else 
					x=0; //��� Ʋ��
			}else
				x=-1 ; //�Ƶ� ����
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	//�ߺ� ���̵� üũ
	public int confirmId(String id) 
	throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x=-1;
		
		try {
			conn = getConnection();
			pstmt =conn.prepareStatement(
					"select id from member where id=?"
					);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				x=1;
			else
				x=-1;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null) 
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null) 
				try {conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	//ȸ������ �����ϱ� ���� ���� ǥ��
	
	public CustomerDataBean getMember(String id)
	throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CustomerDataBean member = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(
					"select * from member where id=?"
					);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member = new CustomerDataBean();
				
				member.setId(rs.getString("id"));
				member.setPasswd(rs.getString("passwd"));
				member.setName(rs.getString("name"));
				member.setReg_data(rs.getTimestamp("reg_data"));
				member.setTel(rs.getString("tel"));
				member.setAddress(rs.getString("address"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
		return member;
	}
	
	//ȸ���� ���� ����
	public void updateMember(CustomerDataBean member)
	throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(
		"update member set passwd=?,name=?,tel=?,address=?" +
			"where id=?");

			pstmt.setString(1, member.getPasswd());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getTel());
			pstmt.setString(4, member.getAddress());
			pstmt.setString(5, member.getId());
			
			pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
	}
	
	//ȸ�� Ż��
	public int deleteMember(String id, String passwd)
	throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd = "";
		int x = -1;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(
			"select passwd from member where id=?"		
					);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("passwd");
				if(dbpasswd.equals(passwd)) {
					pstmt = conn.prepareStatement(
						"delete from member where id=?"
							);
					pstmt.setString(1, id);
					pstmt.executeUpdate();
					x=1;
				}else
					x=0;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
}
