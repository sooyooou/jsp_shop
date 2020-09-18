<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <% session.invalidate(); %>
    <!-- 모든 세션 속성을 무효화 -->
    
    <script>
    	alert("로그아웃 되었습니다.");
    	location.href="../managerMain.jsp";
    </script>