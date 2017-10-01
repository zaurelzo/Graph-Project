package core;

import java.util.*;

import java.io.PrintStream;

import base.Readarg;

public class CovoiturageSimple extends PccStar {
	private Label[] tabDeLabelOrigine1;
	private Label[] tabDeLabelOrigine2;
	protected BinaryHeap<Label> tasOrigine1;
	protected BinaryHeap<Label> tasOrigine2;
	protected BinaryHeap<Label> tasDestination;
	private ArrayList<Label> NoeudsRencontre;
	private Noeud[] tabInverse;
	private Label[] tabDeLabelDestination;
	private int origine2;
	private ArrayList<Integer> ListeZPieton;

	public CovoiturageSimple(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg);
		tabDeLabelOrigine1 = new Label[gr.getNbNoeud()];
		tabDeLabelOrigine2 = new Label[gr.getNbNoeud()];
		tabDeLabelDestination = new Label[gr.getNbNoeud()];
		NoeudsRencontre = new ArrayList<Label>();
		tabInverse = gr.getTabArcEntrants();
		this.origine2 = readarg.lireInt("Numero du sommet d'origine 2 ? ");
		tasOrigine1 = new BinaryHeap<Label>();
		tasOrigine2 = new BinaryHeap<Label>();
		tasDestination = new BinaryHeap<Label>();
		ListeZPieton = new ArrayList<Integer>();
	}

	public void run() throws NoSynchro {
		System.out.println("Run PCC-Star de " + zoneOrigine + " origine 1 :" + origine + " et de origine 2 : "
				+ origine2 + "vers " + zoneDestination + ":" + destination);
		for (int i = 0; i < tabDeLabelOrigine1.length; i++) {
			tabDeLabelOrigine1[i] = new Label(false, Float.POSITIVE_INFINITY, -1, tabNoeud[i], origine);
			tabDeLabelOrigine2[i] = new Label(false, Float.POSITIVE_INFINITY, -1, tabNoeud[i], origine2);
		}
		tabDeLabelOrigine1[origine].setCout(0);
		tasOrigine1.insert(tabDeLabelOrigine1[origine]);
		tabDeLabelOrigine2[origine2].setCout(0);
		tasOrigine2.insert(tabDeLabelOrigine2[origine2]);
		Label x;
		Noeud leNoeudCourant;
		Chemin leChemin = new Chemin();
		Chemin leChemin1 = new Chemin();
		Chemin leChemin2 = new Chemin();
		int maxElementDansLeTas = 1;
		int nbSommetsMarques = 0;
		int numSommetX;
		float tempsCourant;

		// recherche du premier chemin
		while (!tasOrigine1.isEmpty()) {
			x = tasOrigine1.deleteMin();
			x.setMarquage(true);
			D.setWidth(3);
			leNoeudCourant = x.getNoeudCourant();
			for (int j = 0; j < leNoeudCourant.numDernierElement() + 1; j++) {
				int numer_du_successeur = leNoeudCourant.accederAElement(j).getNumDest();
				if (tabDeLabelOrigine1[numer_du_successeur].getMarquage() == false) {
					numSommetX = leNoeudCourant.accederAElement(j).getNumSource();
					tempsCourant = leNoeudCourant.temps_min(numSommetX, numer_du_successeur);
					if (tabDeLabelOrigine1[numer_du_successeur].getCout() > tabDeLabelOrigine1[numSommetX].getCout()
							+ tempsCourant) {
						tabDeLabelOrigine1[numer_du_successeur]
								.setCout(tabDeLabelOrigine1[numSommetX].getCout() + tempsCourant);
						tabDeLabelOrigine1[numer_du_successeur].setPere(numSommetX);
						if (tasOrigine1.array.contains(tabDeLabelOrigine1[numer_du_successeur])) {
							tasOrigine1.update(tabDeLabelOrigine1[numer_du_successeur]);
						} else {
							tasOrigine1.insert(tabDeLabelOrigine1[numer_du_successeur]);
						}
					}
				}
			}
		}

		// recherche du deuxieme chemin
		while (!tasOrigine2.isEmpty()) {
			x = tasOrigine2.deleteMin();
			x.setMarquage(true);
			leNoeudCourant = x.getNoeudCourant();
			D.setColor(java.awt.Color.red);
			for (int j = 0; j < leNoeudCourant.numDernierElement() + 1; j++) {
				int numer_du_successeur = leNoeudCourant.accederAElement(j).getNumDest();
				if (tabDeLabelOrigine2[numer_du_successeur].getMarquage() == false) {
					numSommetX = leNoeudCourant.accederAElement(j).getNumSource();
					if (leNoeudCourant.accederAElement(j).getDescripteur().vitesseMax() < 130) {
						tempsCourant = leNoeudCourant.temps_min_pieton(numSommetX, numer_du_successeur);
						if (tabDeLabelOrigine2[numer_du_successeur].getCout() > tabDeLabelOrigine2[numSommetX].getCout()
								+ tempsCourant) {
							tabDeLabelOrigine2[numer_du_successeur]
									.setCout(tabDeLabelOrigine2[numSommetX].getCout() + tempsCourant);
							tabDeLabelOrigine2[numer_du_successeur].setPere(numSommetX);
							if (tasOrigine2.array.contains(tabDeLabelOrigine2[numer_du_successeur])) {
								tasOrigine2.update(tabDeLabelOrigine2[numer_du_successeur]);
							} else {
								tasOrigine2.insert(tabDeLabelOrigine2[numer_du_successeur]);
							}
						}
					}
				}
			}
		}

		/*
		 * TOD0: augmenter le champ nb sortie + regarder sommet en commun pour
		 * calculer le cout min en sommant couto1->comm
		 * +couto2->comm+coutcom->ddest et prendrre le plus petit verifier que
		 * pieton n'empreinte pas des chemins ou il peut mourir Les deux bouts
		 * de code se ressemble, faire une putain de fonction pour factoriser le
		 * bordel
		 */

		// on fait maintenant un Dijkstra entre la destination et tous les
		// sommets
		for (int i = 0; i < tabDeLabelDestination.length; i++) {
			tabDeLabelDestination[i] = new Label(false, Float.POSITIVE_INFINITY, -1, tabInverse[i], -1); // -1
																											// pour
																											// l'origine
																											// car
																											// il
																											// n'y
																											// en
																											// a
																											// pas
																											// vraiment
		}
		tabDeLabelDestination[destination].setCout(0);
		tasDestination.insert(tabDeLabelDestination[destination]);
		float cout_min = Float.POSITIVE_INFINITY;
		while (!tasDestination.isEmpty()) {
			if (tasDestination.size() > maxElementDansLeTas) {
				maxElementDansLeTas = tasDestination.size();
			}
			cout_min = Float.POSITIVE_INFINITY;
			x = tasDestination.deleteMin();
			x.setMarquage(true);
			nbSommetsMarques++; // on incr�mente le nombre de sommets marqu�s
			leNoeudCourant = x.getNoeudCourant();
			for (int j = 0; j < leNoeudCourant.numDernierElement() + 1; j++) {
				int numer_du_successeur = leNoeudCourant.accederAElement(j).getNumSource();
				if (tabDeLabelDestination[numer_du_successeur].getMarquage() == false) {
					numSommetX = leNoeudCourant.accederAElement(j).getNumDest();
					// temps de parcour entre source et son successeur
					tempsCourant = leNoeudCourant.temps_min(numer_du_successeur, numSommetX);
					if (tabDeLabelDestination[numer_du_successeur]
							.getCout() > tabDeLabelDestination[numSommetX].getCout() + tempsCourant) {
						tabDeLabelDestination[numer_du_successeur]
								.setCout(tabDeLabelDestination[numSommetX].getCout() + tempsCourant);
						tabDeLabelDestination[numer_du_successeur].setPere(numSommetX);
						if (tasDestination.array.contains(tabDeLabelDestination[numer_du_successeur])) {
							tasDestination.update(tabDeLabelDestination[numer_du_successeur]);
						} else {
							tasDestination.insert(tabDeLabelDestination[numer_du_successeur]);
						}
					}
				}
			}
		}

		// recherche noeud synchro
		float min = Float.POSITIVE_INFINITY;
		int numero_de_sommet_syncro = -1;
		System.out.println(" **************** TAILLE 2 : " + NoeudsRencontre.size());
		float cout_syncro_dest = -1f;
		float cout_origine1_syncro = -1f;
		float cout_origine2_syncro = -1f;
		float cout_total = Float.POSITIVE_INFINITY;
		float max = Float.POSITIVE_INFINITY;
		for (int k = 0; k < tabDeLabelOrigine1.length; k++) {
			cout_syncro_dest = tabDeLabelDestination[k].getCout();
			cout_origine1_syncro = tabDeLabelOrigine1[k].getCout();
			cout_origine2_syncro = tabDeLabelOrigine2[k].getCout();
			if (Float.compare(cout_origine1_syncro, Float.POSITIVE_INFINITY) != 0
					&& Float.compare(cout_origine2_syncro, Float.POSITIVE_INFINITY) != 0) {
				max = getMax(cout_origine1_syncro, cout_origine2_syncro);
				cout_total = max + cout_syncro_dest;
			}
			if (cout_total < min) {
				min = cout_total;
				numero_de_sommet_syncro = k;
				System.out.println(" NUMERO DE K : " + numero_de_sommet_syncro);
			}
		}
		if (numero_de_sommet_syncro == -1) {
			throw (new NoSynchro("message"));
		}
		float somme_cout = min + tabDeLabelOrigine2[numero_de_sommet_syncro].getCout();
		System.out.println(" NUMERO DE SYNCHRO : " + numero_de_sommet_syncro);
		System.out.println(" COUT TOTAL: " + somme_cout);
		System.out.println(" COUT SYNCHRO -> DEST : " + tabDeLabelDestination[numero_de_sommet_syncro].getCout());
		System.out.println(" COUT ORIGINE1 -> SYNCHRO : " + tabDeLabelOrigine1[numero_de_sommet_syncro].getCout());
		System.out.println(" COUT ORIGINE2 -> SYNCHRO : " + tabDeLabelOrigine2[numero_de_sommet_syncro].getCout());

		// recherche et affichage des chemins
		D.setWidth(3);
		D.setColor(java.awt.Color.black);
		int fin = numero_de_sommet_syncro;
		while (fin != origine) {
			leChemin.FormerListeSuccesseur(fin);
			fin = tabDeLabelOrigine1[fin].getPere();
		}
		if (fin == origine) {
			leChemin.FormerListeSuccesseur(fin);
		}
		// pour dessiner */
		float debut_longittude;
		float debut_latitude;
		float fin_longitude;
		float fin_latitude;
		for (int i = leChemin.getNombreSommet() - 1; i > 0; i--) {
			debut_longittude = tabNoeud[leChemin.accederElement(i)].getLongitude();
			debut_latitude = tabNoeud[leChemin.accederElement(i)].getLatitude();
			fin_longitude = tabNoeud[leChemin.accederElement(i - 1)].getLongitude();
			fin_latitude = tabNoeud[leChemin.accederElement(i - 1)].getLatitude();
			D.drawLine(debut_longittude, debut_latitude, fin_longitude, fin_latitude);

		}
		D.setColor(java.awt.Color.green);
		fin = numero_de_sommet_syncro;
		while (fin != origine2) {
			leChemin1.FormerListeSuccesseur(fin);
			fin = tabDeLabelOrigine2[fin].getPere();

		}
		if (fin == origine2) {
			leChemin1.FormerListeSuccesseur(fin);
		}
		for (int i = leChemin1.getNombreSommet() - 1; i > 0; i--) {
			debut_longittude = tabNoeud[leChemin1.accederElement(i)].getLongitude();
			debut_latitude = tabNoeud[leChemin1.accederElement(i)].getLatitude();
			fin_longitude = tabNoeud[leChemin1.accederElement(i - 1)].getLongitude();
			fin_latitude = tabNoeud[leChemin1.accederElement(i - 1)].getLatitude();
			D.drawLine(debut_longittude, debut_latitude, fin_longitude, fin_latitude);
		}
		D.setColor(java.awt.Color.blue);
		fin = numero_de_sommet_syncro;
		while (fin != destination) {
			leChemin2.FormerListeSuccesseur(fin);
			fin = tabDeLabelDestination[fin].getPere();
		}
		if (fin == numero_de_sommet_syncro) {
			leChemin2.FormerListeSuccesseur(fin);
		}
		for (int i = leChemin2.getNombreSommet() - 1; i > 0; i--) {
			debut_longittude = tabNoeud[leChemin2.accederElement(i)].getLongitude();
			debut_latitude = tabNoeud[leChemin2.accederElement(i)].getLatitude();
			fin_longitude = tabNoeud[leChemin2.accederElement(i - 1)].getLongitude();
			fin_latitude = tabNoeud[leChemin2.accederElement(i - 1)].getLatitude();
			D.drawLine(debut_longittude, debut_latitude, fin_longitude, fin_latitude);
		}
		D.setColor(java.awt.Color.red);
		D.drawPoint(tabNoeud[numero_de_sommet_syncro].getLongitude(), tabNoeud[numero_de_sommet_syncro].getLatitude(),
				10);
	}

	public void ZPieton() {
		Label x;
		Noeud leNoeudCourant;
		int num_sommet_x;
		boolean arrete_de_marcher = false;
		float temps_courant;

		for (int i = 0; i < tabDeLabelOrigine1.length; i++) {
			tabDeLabelOrigine2[i] = new Label(false, Float.POSITIVE_INFINITY, -1, tabNoeud[i], origine2);
		}
		tabDeLabelOrigine2[origine2].setCout(0);
		tasOrigine2.insert(tabDeLabelOrigine2[origine2]);
		while (!tasOrigine2.isEmpty() && !arrete_de_marcher) {
			x = tasOrigine2.deleteMin();
			x.setMarquage(true);
			leNoeudCourant = x.getNoeudCourant();
			num_sommet_x = leNoeudCourant.accederAElement(0).getNumSource();
			if (tabDeLabelOrigine2[num_sommet_x].getCout() <= 60) {
				ListeZPieton.add(num_sommet_x);
				for (int j = 0; j < leNoeudCourant.numDernierElement() + 1; j++) {
					int numer_du_successeur = leNoeudCourant.accederAElement(j).getNumDest();
					if (tabDeLabelOrigine2[numer_du_successeur].getMarquage() == false) {
						if (leNoeudCourant.accederAElement(j).getDescripteur().vitesseMax() < 130) {
							temps_courant = leNoeudCourant.temps_min_pieton(num_sommet_x, numer_du_successeur);
							if (tabDeLabelOrigine2[numer_du_successeur]
									.getCout() > tabDeLabelOrigine2[num_sommet_x].getCout() + temps_courant) {
								tabDeLabelOrigine2[numer_du_successeur]
										.setCout(tabDeLabelOrigine2[num_sommet_x].getCout() + temps_courant);
								tabDeLabelOrigine2[numer_du_successeur].setPere(num_sommet_x);
								if (tasOrigine2.array.contains(tabDeLabelOrigine2[numer_du_successeur])) {
									tasOrigine2.update(tabDeLabelOrigine2[numer_du_successeur]);
								} else {
									tasOrigine2.insert(tabDeLabelOrigine2[numer_du_successeur]);
								}
							}
						}
					}
				}
			} else {
				arrete_de_marcher = true;
			}
		}
	}

	public void ParcoursZPieton() {
		Label x;
		Noeud leNoeudCourant;
		int num_sommet_x;
		float temps_courant;
		while (!ListeZPieton.isEmpty()) {
			x = tasOrigine1.deleteMin();
			x.setMarquage(true);
			leNoeudCourant = x.getNoeudCourant();
			num_sommet_x = leNoeudCourant.accederAElement(0).getNumSource();
			if (ListeZPieton.contains(num_sommet_x) && !ListeZPieton.isEmpty()) {
				ListeZPieton.remove(num_sommet_x);
				for (int j = 0; j < leNoeudCourant.numDernierElement() + 1; j++) {
					int numer_du_successeur = leNoeudCourant.accederAElement(j).getNumDest();
					if (tabDeLabelOrigine1[numer_du_successeur].getMarquage() == false) {
						temps_courant = leNoeudCourant.temps_min(num_sommet_x, numer_du_successeur);
						if (tabDeLabelOrigine1[numer_du_successeur]
								.getCout() > tabDeLabelOrigine1[num_sommet_x].getCout() + temps_courant) {
							tabDeLabelOrigine1[numer_du_successeur]
									.setCout(tabDeLabelOrigine1[num_sommet_x].getCout() + temps_courant);
							tabDeLabelOrigine1[numer_du_successeur].setPere(num_sommet_x);
							if (tasOrigine1.array.contains(tabDeLabelOrigine1[numer_du_successeur])) {
								tasOrigine1.update(tabDeLabelOrigine1[numer_du_successeur]);
							} else {
								tasOrigine1.insert(tabDeLabelOrigine1[numer_du_successeur]);
							}
						}
					}
				}
			}
		}
	}

	public float getMax(float f1, float f2) {
		float res = 0;
		if (Float.compare(f1, f2) < 0) {
			res = f2;
		} else if (Float.compare(f1, f2) > 0) {
			res = f1;
		}
		return res;
	}
}
