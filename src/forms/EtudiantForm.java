package forms;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dao.EtudiantDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.Administrateur;
import beans.Etudiant;
import beans.Groupe;

public final class EtudiantForm 
{
	private static final String SESSION_ETUDIANT      = "sessionEtudiant";
	private static final String CHAMP_ID              = "id";
    private static final String CHAMP_NOM             = "nom";
    private static final String CHAMP_PRENOM          = "prenom";
    private static final String CHAMP_ADRESSE_MAIL    = "adresseMail";
    private static final String CHAMP_GROUPE          = "groupe";
    private static final String CHAMP_MOT_DE_PASSE    = "motDePasse";
    private Map<String, String> erreurs               = new HashMap<String, String>();
    private EtudiantDao etudiantDao;
    
    /**
     * Constrcuteur
     * 
     * @param etudiantDao
     */
    public EtudiantForm(EtudiantDao etudiantDao) 
    {
    	this.etudiantDao = etudiantDao;
    }
    
    /**
     * Retourne les erreurs
     * 
     * @return erreurs
     */
    public Map<String, String> getErreurs() 
    {
        return erreurs;
    }
    
    /**
     * Crée un étudiant dans la base de données
     * 
     * @param createur
     * @param request
     * @return etudiant
     */
    public Etudiant creerEtudiant(Administrateur createur, HttpServletRequest request) 
    {
    	String nom = getValeurChamp(request, CHAMP_NOM);
    	String prenom = getValeurChamp(request, CHAMP_PRENOM);
    	String adresseMail = getValeurChamp(request, CHAMP_ADRESSE_MAIL);
    	String groupeId = getValeurChamp(request, CHAMP_GROUPE);
    	Etudiant etudiant = new Etudiant();
   
        try 
        {
            traiterNom(nom, etudiant);
            traiterPrenom(prenom, etudiant);
            traiterAdresseMail(adresseMail, etudiant);
            traiterMotDePasse(etudiant);
            traiterGroupe(groupeId, etudiant);
            traiterEtudiant(etudiant);
            
            if (erreurs.isEmpty()) 
            {
            	etudiantDao.creer(createur, etudiant);
            }
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }

        return etudiant;
    }
    
    /**
     * Recherche un ou des étudiant(s) dans la base de données
     * 
     * @param request
     * @return listeEtudiants
     */
    public Set<Etudiant> rechercherEtudiant(HttpServletRequest request) 
    {
    	String id = getValeurChamp(request, CHAMP_ID);
    	String nom = getValeurChamp(request, CHAMP_NOM);
    	String prenom = getValeurChamp(request, CHAMP_PRENOM);
    	String adresseMail = getValeurChamp(request, CHAMP_ADRESSE_MAIL);
    	String groupeId = getValeurChamp(request, CHAMP_GROUPE);
    	Set<Etudiant> listeEtudiants = new HashSet<Etudiant>();
    	Etudiant etudiant = new Etudiant();
    	Groupe groupe = new Groupe();
    	
    	if (id != null) 
    	{
    		etudiant.setId(Long.parseLong(id));
    	}
    	
    	if (groupeId != null) 
    	{
    		groupe.setId(Long.parseLong(groupeId));
    	}
    	
    	etudiant.setNom(nom);
    	etudiant.setPrenom(prenom);
    	etudiant.setAdresseMail(adresseMail);
    	etudiant.setGroupe(groupe);
    	listeEtudiants = etudiantDao.rechercher(etudiant);
        
    	return listeEtudiants;
    }

    /**
     * Edite un étudiant dans la base de données
     * 
     * @param editeur
     * @param request
     * @return etudiant
     */
    public Etudiant editerEtudiant(Administrateur editeur, HttpServletRequest request) 
    {
    	String id = getValeurChamp(request, CHAMP_ID);
    	String nom = getValeurChamp(request, CHAMP_NOM);
    	String prenom = getValeurChamp(request, CHAMP_PRENOM);
    	String adresseMail = getValeurChamp(request, CHAMP_ADRESSE_MAIL);
    	String groupeId = getValeurChamp(request, CHAMP_GROUPE);
    	Etudiant etudiant = new Etudiant();
    	
        try 
        {
        	etudiant.setId(Long.parseLong(id));
            traiterNom(nom, etudiant);
            traiterPrenom(prenom, etudiant);
            traiterAdresseMail(adresseMail, etudiant);
            traiterGroupe(groupeId, etudiant);
            traiterEtudiant(etudiant);
            
            if (erreurs.isEmpty()) 
            {
            	etudiantDao.editer(editeur, etudiant);
            }
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }

        return etudiant;
    }
   
    /**
     * Vérifie les identifiants d'un étudiant dans la base de données
     * 
     * @param request
     * @return etudiant
     */
    public Etudiant verifIdentifiantEtudiant(HttpServletRequest request) 
    {
    	String adresseMail = getValeurChamp(request, CHAMP_ADRESSE_MAIL);
    	String motDePasse = getValeurChamp(request, CHAMP_MOT_DE_PASSE);
    	Etudiant etudiant = new Etudiant();
    	
        try 
        {
        	traiterAdresseMail(adresseMail, etudiant);
        	etudiant.setMotDePasse(crypterMotDePasse(motDePasse));
        	etudiant  = etudiantDao.verifIdentifiant(etudiant);
        	traiterIdentifiant(etudiant);
        	etudiant.setMotDePasse(crypterMotDePasse(motDePasse));
            etudiant.setAdresseMail(adresseMail);
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }

        return etudiant;
    }
    
    /**
     * Cherche un étudiant dans la base de données
     * 
     * @param request
     * @return etudiant
     */
    public Etudiant trouverEtudiant(HttpServletRequest request)
    {
    	String id = getValeurChamp(request, CHAMP_ID);
    	Etudiant etudiant = new Etudiant();
    	
    	etudiant.setId(Long.parseLong(id));
    	etudiant = etudiantDao.trouver(etudiant);
    	
    	return etudiant;
    }
    
    /**
     * Supprime un étudiant dans la base de données
     * 
     * @param request
     * @return statut
     */
    public void supprimerEtudiant(Administrateur editeur, HttpServletRequest request)
    {
    	String id = getValeurChamp(request, CHAMP_ID);
    	Etudiant etudiant = new Etudiant();
    	
    	etudiant.setId(Long.parseLong(id));
        etudiantDao.supprimer(editeur, etudiant);
    }
    
    /**
     * Récupère toutes les informations et le bulletin de l'étudiant
     * 
     * @param request
     * @return etudiant
     */
    public Etudiant recupTout(HttpServletRequest request)
    {
    	Etudiant etudiant = (Etudiant) getValeurSession(request, SESSION_ETUDIANT);
    	etudiant = etudiantDao.recupTout(etudiant);
    	
    	return etudiant;
    }
    
    /**
     *  Traite le nom d'un étudiant
     *  
     * @param nom
     * @param etudiant
     */
    private void traiterNom(String nom, Etudiant etudiant) 
    {
    	try 
    	{
            validationNom(nom);
        } 
    	catch (Exception e) 
    	{
            setErreur(CHAMP_NOM, e.getMessage());
        }
    	
    	etudiant.setNom(nom.substring(0, 1).toUpperCase() + nom.substring(1).toLowerCase());
    }
    
    /**
     *  Traite le prenom d'un étudiant
     *  
     * @param prenom
     * @param etudiant
     */
    private void traiterPrenom(String prenom, Etudiant etudiant) 
    {
    	try 
    	{
            validationPrenom(prenom);
        } 
    	catch (Exception e) 
    	{
            setErreur(CHAMP_PRENOM, e.getMessage());
        }
    	
    	etudiant.setPrenom(prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase());
    }
    
    /**
     *  Traite l'id du groupe de l'étudiant
     *  
     * @param groupeId
     * @param etudiant
     */
    private void traiterGroupe(String groupeId, Etudiant etudiant) 
    {
    	Groupe groupe = new Groupe();
    	
    	try 
    	{
            validationGroupe(groupeId);
        } 
    	catch (Exception e) 
    	{
            setErreur(CHAMP_GROUPE, e.getMessage());
        }
    	
    	groupe.setId(Long.parseLong(groupeId));
    	etudiant.setGroupe(groupe);
    }
    
    /**
     *  Traite l'adresse mail de l'étudiant
     *  
     * @param adresseMail
     * @param etudiant
     */
    private void traiterAdresseMail(String adresseMail, Etudiant etudiant)  
    {
    	try 
    	{
            validationAdresseMail(adresseMail);
        } 
    	catch (Exception e) 
    	{
            setErreur(CHAMP_ADRESSE_MAIL, e.getMessage());
        }
    	
    	etudiant.setAdresseMail(adresseMail.trim().toLowerCase());
    }
    
    /**
     *  Traite le mot de passe de l'étudiant
     *  
     * @param etudiant
     */
    private void traiterMotDePasse(Etudiant etudiant)  
    {		
    	String motDePasse = genererMotDePasse(8);

    	try 
    	{
    		validationMotsDePasse(motDePasse);
    		motDePasse = crypterMotDePasse(motDePasse);
    	} 
    	catch (Exception e)
    	{
    		
    	} 

    	etudiant.setMotDePasse(motDePasse);
    }
    
    /**
     *  Traite un étudiant
     *  
     * @param etudiant
     */
    private void traiterEtudiant(Etudiant etudiant) 
    {
    	try 
    	{
    		validationEtudiant(etudiant);
        } 
    	catch (Exception e) 
    	{
            setErreur("etudiant", e.getMessage());
        }
    }
    
    /**
     *  Traite les identifiants d'un étudiant
     *  
     * @param etudiant
     */
    private void traiterIdentifiant(Etudiant etudiant) 
    {
    	try 
    	{
    		validationIdentifiant(etudiant);
        } 
    	catch (Exception e) 
    	{
            setErreur("connexion", e.getMessage());
        }
    }
    
    /**
     * Valide le nom d'un étudiant
     * 
     * @param nom
     * @throws Exception
     */
    private void validationNom(String nom) throws Exception 
    {
        if ((nom == null) || (nom.length() < 2) || (nom.length() > 50)) 
        {
            throw new Exception("Veuillez entrer un nom de 2 à 50 caractères");
        }
    }
    
    /**
     * Valide le prenom d'un étudiant
     * 
     * @param prenom
     * @throws Exception
     */
    private void validationPrenom(String prenom) throws Exception 
    {
        if ((prenom == null) || (prenom.length() < 2) || (prenom.length() > 50)) 
        {
            throw new Exception( "Veuillez entrer un prenom de 2 à 50 caractères" );
        }
    }
    
    /**
     * Valide le groupe d'un étudiant
     * 
     * @param groupe
     * @throws Exception
     */
    private void validationGroupe(String groupe) throws Exception 
    {
        if (groupe == null) 
        {
            throw new Exception("Veuillez sélectionner un groupe de la liste");
        }
    }
    
    /**
     * Valide l'adresse mail d'un étudiant
     * 
     * @param adresseMail
     * @throws Exception
     */
    private void validationAdresseMail(String adresseMail) throws Exception 
    {
        if ((adresseMail == null) || (adresseMail.length() < 8) || (adresseMail.length() > 100 ) || (!adresseMail.matches("[a-zA-Z0-9@.-_]+@[a-zA-Z]{2,20}.[a-zA-Z]{2,3}"))) 
        {
            throw new Exception("Veuillez entrer une adresse mail correcte");
        }
    }
    
    /**
     * Valide le mot de passe d'un étudiant
     * 
     * @param motDePasse
     * @throws Exception
     */
    private void validationMotsDePasse(String motDePasse) throws Exception 
    {
        if ((motDePasse == null) || (motDePasse.length() != 8)) 
        {
            throw new Exception("Veuillez entrez un mot de passe plus fort");
        } 
    }
    
    /**
     * Valide un étudiant
     * 
     * @param etudiant
     * @throws Exception
     */
    private void validationEtudiant(Etudiant etudiant) throws Exception 
    {
        if (etudiantDao.verifExistance(etudiant) != 0) 
        {
            throw new Exception("Cet étudiant existe déja");
        }
    }
    
    /**
     * Valide les identifiants d'un étudiant
     * 
     * @param etudiant
     * @throws Exception
     */
    private void validationIdentifiant(Etudiant etudiant) throws Exception 
    {
        if (etudiant.getId() == null) 
        {
            throw new Exception("Votre adresse mail ou votre mot de passe est incorrect");
        }
    }
    
    /**
     * Enregistre une erreur
     * 
     * @param champ
     * @param message
     */
    private void setErreur(String champ, String message)
    {
        erreurs.put(champ, message);
    }

    /**
     * Génère un mot de passe aléatoire
     * 
     * @param longueur
     * @return motDePasse
     */
    private String genererMotDePasse(int longueur) 
    {
    	String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int caracteresTaille = caracteres.length();
        StringBuilder  motDePasse = new StringBuilder (caracteresTaille);
        
        for (int x = 0; x < longueur; x++) 
        {
            int y = (int) (Math.random() * caracteresTaille);
            motDePasse.append(caracteres.charAt(y));
        }
        System.out.println(motDePasse.toString());
        return motDePasse.toString();
    }

    /**
     * Crypte un mot de passe
     * 
     * @param motDePasse
     * @return motDePasseCrypte
     */
    private String crypterMotDePasse(String motDePasse)
    {
    	StringBuffer motDePasseCrypte = new StringBuffer();
    	
    	try 
    	{
    		MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(motDePasse.getBytes());
            byte byteData[] = md.digest();
            
            for (int i = 0; i < byteData.length; i++) 
            {
            	motDePasseCrypte.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
    	} 
    	catch (Exception e)
    	{
    		
    	} 

    	return motDePasseCrypte.toString();
    }
    
    /**
     * Retourne la valeur d'un champ du formulaire
     * 
     * @param request
     * @param nomChamp
     * @return valeur
     */
    private static String getValeurChamp(HttpServletRequest request, String nomChamp) 
    {
        String valeur = request.getParameter(nomChamp);
        
        if ((valeur == null) || (valeur.trim().length() == 0)) 
        {
            return null;
        }
        else 
        {
            return valeur.trim();
        }
    }
    
    /**
     * Retourne la valeur d'un parametre de session
     * 
     * @param request
     * @param nomSession
     * @return objet
     */
    private static Object getValeurSession(HttpServletRequest request, String nomSession) 
    {
    	HttpSession session = request.getSession();
    	Object objet = session.getAttribute(nomSession);
    	
        return((objet == null) ? null : objet);
    }
}

