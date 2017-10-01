package core;

//exception à lever s'il n'y a pas de noeud de synchronisation
public class NoSynchro extends Exception {
	private String contenu;

	public NoSynchro(String contenu) {
		this.contenu = contenu;
	}

}
