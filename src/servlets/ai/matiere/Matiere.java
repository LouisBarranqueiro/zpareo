package servlets.ai.matiere;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import forms.MatiereForm;
import dao.DAOFactory;
import dao.MatiereDao;

@SuppressWarnings("serial")
@WebServlet("/ai/matiere")
public class Matiere extends HttpServlet 
{
	private static final String CONF_DAO_FACTORY = "daofactory";
	private static final String ATT_MATIERES     = "listeMatieres";
	private static final String ATT_NB_MATIERES  = "nbMatieres";
	private static final String ATT_FORM         = "form";
	private static final String VUE              = "/WEB-INF/ai/matiere/index.jsp";
	private MatiereDao matiereDao;
	
    public Matiere() 
    {
        super();
    }
    
    public void init() throws ServletException 
    {
        this.matiereDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getMatiereDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        MatiereForm form = new MatiereForm(this.matiereDao);
        Set<beans.Matiere> listeMatieres = form.rechercherMatiere(request);
		int nbMatieres = listeMatieres.size();
		
        request.setAttribute(ATT_FORM, form);
        request.setAttribute(ATT_MATIERES, listeMatieres);
        request.setAttribute(ATT_NB_MATIERES, nbMatieres);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	}
	
}
