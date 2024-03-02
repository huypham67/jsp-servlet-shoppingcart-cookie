/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

/**
 *
 * @author huypd
 */
@WebServlet(name = "QuantityIncDecSrv", urlPatterns = {"/quantity-inc-dec"})
public class QuantityIncDecSrv extends HttpServlet {

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
            throws ServletException, IOException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String idS = request.getParameter("id");
        int id = Integer.parseInt(idS);
        DAO dao = new DAO();
        List<Cart> curList = new ArrayList<>();
        Cookie[] arr = request.getCookies();
        if (arr != null) {
            for (Cookie c : arr) {
                if (c.getName().equals("cartC")) {
                    curList = dao.decode(c.getValue());
                }
            }
        }
        if (action.equals("inc")) {
            for (Cart o : curList) {
                if (id == o.getId()) {
                    int quantity = o.getQuantity();
                    quantity++;
                    o.setQuantity(quantity);
                    break;
                }
            }
        }
        if (action.equals("dec")) {
            for (Cart o : curList) {
                if (id == o.getId() && o.getQuantity() > 0) {
                    int quantity = o.getQuantity();
                    if (quantity != 1) {
                        quantity--;
                        o.setQuantity(quantity);
                    } else {
                        curList.remove(o);
                    }
                    break;
                }
            }
        }
        Cookie cartC = new Cookie("cartC", dao.encode(curList));
        cartC.setMaxAge(60*60*24*7);
        response.addCookie(cartC);
        response.sendRedirect("cart.jsp");
        //request.setAttribute("curList", curList);
        //request.getRequestDispatcher("cart.jsp").forward(request, response);
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QuantityIncDecSrv.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QuantityIncDecSrv.class.getName()).log(Level.SEVERE, null, ex);
        }
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
