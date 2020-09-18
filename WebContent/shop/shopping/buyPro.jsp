<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "shop.customer.*"%>
<%@ page import = "shop.master.ShopBookDBBean"%>
<%@ page import = "java.util.List"%>
<%@ page import = "java.sql.Timestamp"%>

<%request.setCharacterEncoding("utf-8");%>
<%
	String account = request.getParameter("account");
	String deliveryName = request.getParameter("deliveryName");
	String deliveryTel = request.getParameter("deliveryTel");
	String deliveryAddress = request.getParameter("deliveryAddress");
	String buyer = (String)session.getAttribute("id");
	
	CartDBBean cartProcess = CartDBBean.getInstance();
	List<CartDataBean> cartLists = cartProcess.getCart(buyer);
	
	BuyDBBean buyProcess = BuyDBBean.getInstance();
	
	buyProcess.insertBuy(cartLists,buyer,account,
				deliveryName,deliveryTel,deliveryAddress);
	
	response.sendRedirect("buyList.jsp");
%>