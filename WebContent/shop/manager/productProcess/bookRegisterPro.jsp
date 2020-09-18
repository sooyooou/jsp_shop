<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@page import="shop.master.ShopBookDBBean" %>
    <%@page import="java.sql.Timestamp" %>
    <%@page import="com.oreilly.servlet.MultipartRequest" %>
    <%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
    <%@page import="java.util.*" %>
    <%@page import="java.io.*" %>
    
    <% request.setCharacterEncoding("utf-8"); %>
    
    <%
    String realFolder = ""; //웹 애플리케이션상의 절대 경로
    String filename="";
    MultipartRequest imageUp = null;
    
    String saveFolder = "imageFile"; //파일이 업로드되는 폴더를 지정한다.
    String encType = "utf-8"; //인코딩 타입
    int maxSize = 2*1024*1024; //최대 업로드될 파일 크기 5mb
    //현재 jsp페이지의 웹 애플리케이션상의 절대경로를 구한다.
    ServletContext context = getServletContext();
    realFolder = context.getRealPath(saveFolder);
    
    try{
    	//전송 담당 콤포넌트 생성, 파일 전송
    	//전송 파일명을 가진 객체, 서버상 절대경로, 최대 업로드 파일크기, 문자코드, 기본보안 적용
    	imageUp = new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());
    	
    	//전송한 파일 정보를 가져와 출력
    	Enumeration files = imageUp.getFileNames();
    	
    	//파일정보가 있으면
    	while(files.hasMoreElements()) {
    		//input 태그의 속성이 file인 태그의 name 속성값: 파라미터이름
    		String name = (String)files.nextElement();
    	
    		//서버에 저장된 파일 이름
    		filename = imageUp.getFilesystemName(name);
    	}
    } catch(Exception e) {
    	e.printStackTrace();
    }
    %>
    
    <jsp:useBean id="book" scope="page" class="shop.master.ShopBookDataBean"></jsp:useBean>
    
    <%
    String book_kind = imageUp.getParameter("book_kind");
    String book_title = imageUp.getParameter("book_title");
    String book_price = imageUp.getParameter("book_price");
    String book_count = imageUp.getParameter("book_count");
    String author = imageUp.getParameter("author");
    String publish_com = imageUp.getParameter("publish_com");
    String book_content = imageUp.getParameter("book_content");
    String discount_rate = imageUp.getParameter("discount_rate");
    
    String year = imageUp.getParameter("publish_year");
    String month = (imageUp.getParameter("publish_month").length() == 1) ? "0" + imageUp.getParameter("publish_month"): imageUp.getParameter("publish_month");
    String day = (imageUp.getParameter("publish_day").length() == 1) ? "0" + imageUp.getParameter("publish_day"): imageUp.getParameter("publish_day");
    
    book.setBook_kind(book_kind);
    book.setBook_title(book_title);
    book.setBook_price(Integer.parseInt(book_price));
    book.setBook_count(Short.parseShort(book_count));
    book.setAuthor(author);
    book.setPublish_com(publish_com);
    book.setPublish_date(year+"-"+month+"-"+day);
    book.setBook_image(filename);
    book.setBook_content(book_content);
    book.setDiscount_rate(Byte.parseByte(discount_rate));
    book.setReg_date(new Timestamp(System.currentTimeMillis()));
    
    ShopBookDBBean bookProcess = ShopBookDBBean.getInstance();
    bookProcess.insertBook(book);
    
    response.sendRedirect("bookList.jsp?book_kind=all");
    %>