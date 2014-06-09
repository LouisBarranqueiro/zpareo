package beans;

import java.util.Set;

import beans.Groupe;
import beans.Matiere;

public class Professeur extends Utilisateur implements Comparable<Professeur>
{
	private Set<Matiere> listeMatieres;
	private Set<Groupe> listeGroupes;
	
	public Professeur()
	{
		super();
		this.listeMatieres = null;
		this.listeGroupes = null;
	}
	public Professeur(Professeur professeur)
	{
		super(professeur);
		this.setListeMatieres(professeur.getListeMatieres());
		this.setListeGroupes(professeur.getListeGroupes());
	}
	
	public Set<Matiere> getListeMatieres()
	{
		return listeMatieres;
	}
	
	public void setListeMatieres(Set<Matiere> listeMatieres) 
	{
		this.listeMatieres = listeMatieres;
	}
	
	public Set<Groupe> getListeGroupes() 
	{
		return listeGroupes;
	}
	
	public void setListeGroupes(Set<Groupe> listeGroupes) 
	{
		this.listeGroupes = listeGroupes;
	}
	
	@Override
	public int compareTo(Professeur professeur2) 
	{
        int compId = this.getId().compareTo(professeur2.getId());
        if(compId != 0) return compId;
        return 0;
	}
}