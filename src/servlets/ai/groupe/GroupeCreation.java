package servlets.ai.groupe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beans.Administrateur;
import beans.Groupe;
import dao.DAOFactory;
import dao.GroupeDao;
import forms.GroupeForm;

@SuppressWarnings("serial")
@WebServlet("/ai/groupe/creation")
public class GroupeCreation extends HttpServlet 
{
	private static final String CONF_DAO_FACTORY           = "daofactory";
	private static final String ATT_SESSION_ADMINISTRATEUR = "sessionAdministrateur";
	private static final String ATT_GROUPE                 = "groupe";
	private static final String ATT_FORM                   = "form";
	private static final String VUE_CREATION               = "/WEB-INF/ai/groupe/creation.jsp";
    private GroupeDao groupeDao;

    public GroupeCreation() 
    {
        super();
    }
    
    public void init() throws ServletException 
    {
        this.groupeDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getGroupeDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        this.getServletContext().getRequestDispatcher(VUE_CREATION).forward( request, response);   
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        GroupeForm form = new GroupeForm(this.groupeDao);
        HttpSession session = request.getSession();
		Administrateur sessionAdministrateur = (Administrateur) session.getAttribute(ATT_SESSION_ADMINISTRATEUR);
        Groupe groupe = form.creerGroupe(sessionAdministrateur, request);
		
       if (form.getErreurs().isEmpty())
       {
           response.sendRedirect("http://localhost:8080/ZPareo/ai/groupe");   
       }
       else
       {
    	   request.setAttribute(ATT_FORM, form);
           request.setAttribute(ATT_GROUPE, groupe);
    	   this.getServletContext().getRequestDispatcher(VUE_CREATION).forward(request, response);   
       }
	}

}
