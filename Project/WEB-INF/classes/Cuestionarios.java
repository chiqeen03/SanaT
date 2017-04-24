import javax.servlet.*;
import javax.servlet.http.*;

import bean.Pregunta;
import bean.Respuesta;

import java.sql.*;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

@WebServlet("/Cuest")
public class Cuestionarios extends HttpServlet{

	public void doPost(HttpServletRequest request, HttpServletResponse response){
		try{

			String base = getServletContext().getInitParameter("base");
			String usuario = getServletContext().getInitParameter("usuario");
			String password = getServletContext().getInitParameter("pass");


			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/"+base;

			System.out.println(url);
			Connection con = DriverManager.getConnection(url,usuario,password);
			int id=Integer.parseInt(request.getParameter("id"));
			int number = Integer.parseInt(request.getParameter("test"));
			Statement one = con.createStatement();
			ResultSet res= one.executeQuery("SELECT IDPregunta,DescripcionPregunta, NumPregunta FROM pregunta WHERE IDCuestionario="+number+";");
			Statement resp = con.createStatement();
			Vector<Respuesta> respuestas = new Vector<Respuesta>();
			ResultSet opciones = resp.executeQuery("SELECT DescripcionRespuesta, IDRespuesta, IDPregunta  FROM respuesta NATURAL JOIN pregunta WHERE IDCuestionario="+number+";");
			while(opciones.next()){
				Respuesta aux = new Respuesta();
				aux.setDescripcionRespuesta(opciones.getString("DescripcionRespuesta"));
				aux.setIDRespuesta(opciones.getInt("IDRespuesta"));
				aux.setIDPregunta(opciones.getInt("IDPregunta"));
				respuestas.add(aux);
			}
			Vector<Pregunta> preguntas= new Vector<Pregunta>();
			while(res.next()){
				Pregunta aux = new Pregunta();
				aux.setDescripcionPregunta(res.getString("DescripcionPregunta"));	
				aux.setNumPregunta(res.getInt("NumPregunta"));	
				aux.setIDPregunta(res.getInt("IDPregunta"));	
				preguntas.add(aux);
			}
			
			request.getSession().setAttribute("id", id);
			request.setAttribute("respuestas",respuestas);
			request.setAttribute("preguntas",preguntas);
			con.close();

			RequestDispatcher disp =  getServletContext().getRequestDispatcher("/contCuest.jsp");

			if(disp!=null){
				disp.forward(request,response);
			}


		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
			
}

