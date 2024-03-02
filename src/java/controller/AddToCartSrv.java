/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DAO;
import java.io.IOException;
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
import model.Cart;

/**
 *
 * @author huypd
 */
@WebServlet(name = "AddToCartSrv", urlPatterns = {"/add-to-cart"})
public class AddToCartSrv extends HttpServlet {

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
            throws ServletException, IOException, ClassNotFoundException{
        response.setContentType("text/html;charset=UTF-8");
        List<Cart> curList = new ArrayList<>();
        DAO dao = new DAO();
        Cookie[] arr = request.getCookies();
        if (arr != null) {
            for (Cookie c : arr) {
                if (c.getName().equals("cartC")) {
                    curList = dao.decode(c.getValue());
                }
            }
        }
        if (curList == null) {
            curList = new ArrayList<>();
        }
        String idS = request.getParameter("id");
        String quantityS = request.getParameter("quantity");
        int id = Integer.parseInt(idS);
        int quantity = Integer.parseInt(quantityS);
        boolean existed = false;
        for (Cart o : curList) {
            if (o.getId() == id) {
                existed = true;
                request.setAttribute("tag", id);
                request.setAttribute("mes", "Item Already in Cart");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            }
        }
        if (!existed) {
            Cart cart = new Cart(id, quantity);
            curList.add(cart);
            String txt = dao.encode(curList);
            //tìm cái cookie tên CART và gán vào txt

            Cookie cartC = new Cookie("cartC", txt);
            cartC.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cartC);

            Cookie sizeC = new Cookie("sizeC", String.valueOf(curList.size()));
            sizeC.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(sizeC);
            response.sendRedirect("index.jsp");
        }
    }

    public static void main(String[] args) {
        try {
            List<Cart> curList = new ArrayList<>();
            curList.add(new Cart(1, 2));
            curList.add(new Cart(2, 1));
            curList.add(new Cart(3, 2));
            DAO dao = new DAO();
            String s = dao.encode(curList);
            System.out.println(s);
            List<Cart> newCart = new ArrayList<>();
            try {
                newCart = dao.decode(s);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AddToCartSrv.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Cart o : newCart) {
                System.out.println(o);
            }
        } catch (IOException ex) {
            Logger.getLogger(AddToCartSrv.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddToCartSrv.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddToCartSrv.class.getName()).log(Level.SEVERE, null, ex);
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
