/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.Order;
import model.User;

/**
 *
 * @author huypd
 */
@WebServlet(name = "CheckOutSrv", urlPatterns = {"/cart-check-out"})
public class CheckOutSrv extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        User auth = (User) session.getAttribute("auth");
        if (auth != null) {
            SimpleDateFormat formatter = new SimpleDateFormat();
            List<Cart> curList = new ArrayList<>();
            DAO dao = new DAO();
            Cookie[] arr = request.getCookies();
            if (arr != null) {
                for (Cookie c : arr) {
                    if (c.getName().equals("cartC")) {
                        try {
                            curList = dao.decode(c.getValue());
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(CheckOutSrv.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            Cookie cartC = new Cookie("cartC", dao.encode(curList));
            cartC.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cartC);
            if (curList != null && curList.size() != 0) {
                for (Cart cart : curList) {
                    Order o = new Order(cart.getId(), auth.getId(), cart.getQuantity(), formatter.format(new Date()));
                    dao.insertOrder(o);
                }
                curList.clear();
                response.sendRedirect("orders.jsp");
            } else {
                request.setAttribute("mes", "There is no items in your cart. We can't checkout for you.");
                request.getRequestDispatcher("cart.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
