package core;

import java.io.*;

import base.Dessin;
import base.Readarg;

public class Pcc extends Algo {
	protected int zoneOrigine;
	protected int origine;
	protected int zoneDestination;
	protected int algo_a_lancer;
	protected int destination;
	protected Label[] tabDeLabel;
	protected BinaryHeap<Label> tas;
	protected Noeud[] tabNoeud;
	protected Dessin D;

	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg);
		// on définit un tableau de label qui contient autant de cases que de
		// noeuds du graphe
		tabDeLabel = new Label[gr.getNbNoeud()];
		tabNoeud = gr.RenvoyertabNoeud();
		tas = new BinaryHeap<Label>();
		D = gr.getDessin();
		this.zoneOrigine = gr.getZone();
		this.origine = readarg.lireInt("Numero du sommet d'origine ? ");
		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone();
		this.destination = readarg.lireInt("Numero du sommet destination ? ");
		// Choix algo
		this.algo_a_lancer = readarg.lireInt("Djikstra en distance : 1 / Djikstra en temps : 0");
	}

	public void run() throws NoSynchro {
		System.out
				.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination);

		try {
			if (this.algo_a_lancer == 0) {
				for (int i = 0; i < tabDeLabel.length; i++) {
					tabDeLabel[i] = new Label(false, Float.POSITIVE_INFINITY, -1, tabNoeud[i]);
				}
				tabDeLabel[origine].setCout(0);
				tas.insert(tabDeLabel[origine]);
				// iterations
				int nombreDeSommetMarque = 0;
				Label x;
				Noeud leNoeudCourant;
				Chemin leChemin = new Chemin();
				int nombre_max_d_element_dans_le_tas = 1;
				int nombre_de_sommets_marques = 0;
				while (tabDeLabel[destination].getMarquage() == false && !tas.isEmpty()) {
					if (tas.size() > nombre_max_d_element_dans_le_tas) {
						nombre_max_d_element_dans_le_tas = tas.size();
					}
					x = tas.deleteMin();
					x.setMarquage(true);
					// affichage des sommets marques
					D.setWidth(3);
					D.setColor(java.awt.Color.blue);
					nombre_de_sommets_marques++; // on incr�mente le nombre de
													// sommets marqu�s
					leNoeudCourant = x.getNoeudCourant();
					D.drawPoint(leNoeudCourant.getLongitude(), leNoeudCourant.getLatitude(), 5);
					D.setColor(java.awt.Color.black);
					float temps_courant;
					int num_sommet_x;
					for (int j = 0; j < leNoeudCourant.numDernierElement() + 1; j++) {
						int numer_du_successeur = leNoeudCourant.accederAElement(j).getNumDest();
						if (leNoeudCourant.accederAElement(j).getNumzone() == this.zoneOrigine) {
							if (tabDeLabel[numer_du_successeur].getMarquage() == false) {
								num_sommet_x = leNoeudCourant.accederAElement(j).getNumSource();
								temps_courant = leNoeudCourant.temps_min(num_sommet_x, numer_du_successeur);
								if (tabDeLabel[numer_du_successeur].getCout() > tabDeLabel[num_sommet_x].getCout()
										+ temps_courant) {
									tabDeLabel[numer_du_successeur]
											.setCout(tabDeLabel[num_sommet_x].getCout() + temps_courant);
									tabDeLabel[numer_du_successeur].setPere(num_sommet_x);
									if (tas.array.contains(tabDeLabel[numer_du_successeur])) {
										tas.update(tabDeLabel[numer_du_successeur]);
									} else {
										tas.insert(tabDeLabel[numer_du_successeur]);
									}
								}
							}
						} else {
							System.out.println(" Le noeud ne se trouve pas dans la n'existe pas sur le graphe");
						}
					}
				}

				// on va lire le temps de parcours ;
				float cout_final = tabDeLabel[destination].getCout();
				System.out.println("cout total avec djikstra " + cout_final + " nombre_max_d_element_dans_le_tas "
						+ nombre_max_d_element_dans_le_tas + " nombre de sommets marqu�s : "
						+ nombre_de_sommets_marques);
				int fin = destination;
				if (!tas.isEmpty()) {
					System.out.println("tas non vide donc il existe un chemin entre " + origine + "  " + destination);
					while (fin != origine) {
						leChemin.FormerListeSuccesseur(fin);
						fin = tabDeLabel[fin].getPere();
					}
					if (fin == origine) {
						leChemin.FormerListeSuccesseur(fin);
					}
					// pour dessiner
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
				} else {
					System.out.println("Le tas est vide, tous les sommets ont été marqués");
				}
			} else {
				for (int i = 0; i < tabDeLabel.length; i++) {
					tabDeLabel[i] = new Label(false, Float.POSITIVE_INFINITY, -1, tabNoeud[i]);
				}
				tabDeLabel[origine].setCout(0);
				tas.insert(tabDeLabel[origine]);
				int nombreDeSommetMarque = 0;
				Label x;
				Noeud leNoeudCourant;
				Chemin leChemin = new Chemin();
				int nombre_max_d_element_dans_le_tas = 1;
				int nombre_de_sommets_marques = 0;
				while (tabDeLabel[destination].getMarquage() == false && !tas.isEmpty()) {
					if (tas.size() > nombre_max_d_element_dans_le_tas) {
						nombre_max_d_element_dans_le_tas = tas.size();
					}
					x = tas.deleteMin();
					x.setMarquage(true);
					if (x.getMarquage())
						nombre_de_sommets_marques++; // on incr�mente le nombre
														// de sommets marqu�s
					leNoeudCourant = x.getNoeudCourant();
					D.setColor(java.awt.Color.black);
					float distance_courante;
					int num_sommet_x;
					for (int j = 0; j < leNoeudCourant.numDernierElement() + 1; j++) {
						int numer_du_successeur = leNoeudCourant.accederAElement(j).getNumDest();
						if (leNoeudCourant.accederAElement(j).getNumzone() == this.zoneOrigine) {
							if (tabDeLabel[numer_du_successeur].getMarquage() == false) {
								num_sommet_x = leNoeudCourant.accederAElement(j).getNumSource();
								distance_courante = leNoeudCourant.distanceMin(num_sommet_x, numer_du_successeur);
								if (tabDeLabel[numer_du_successeur].getCout() > tabDeLabel[num_sommet_x].getCout()
										+ distance_courante) {
									tabDeLabel[numer_du_successeur]
											.setCout(tabDeLabel[num_sommet_x].getCout() + distance_courante);
									tabDeLabel[numer_du_successeur].setPere(num_sommet_x);
									if (tas.array.contains(tabDeLabel[numer_du_successeur])) {
										tas.update(tabDeLabel[numer_du_successeur]);
									} else {
										tas.insert(tabDeLabel[numer_du_successeur]);
									}
								}
							}
						} else {
							System.out.println(" Le noeud ne se trouve pas dans la n'existe pas sur le graphe");
						}
					}
				}
				// on va lire le temps de parcours ;
				float cout_final = tabDeLabel[destination].getCout();
				System.out.println("cout total avec djikstra " + cout_final + " nombre_max_d_element_dans_le_tas "
						+ nombre_max_d_element_dans_le_tas + " nombre de sommets marqu�s : "
						+ nombre_de_sommets_marques);
				int fin = destination;
				if (!tas.isEmpty()) {
					System.out.println("tas non vide donc il existe un chemin entre " + origine + "  " + destination);
					while (fin != origine) {
						leChemin.FormerListeSuccesseur(fin);
						fin = tabDeLabel[fin].getPere();
					}
					if (fin == origine) {
						leChemin.FormerListeSuccesseur(fin);
					}
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
				} else {
					System.out.println("Le tas est vide, tous les sommets ont été marqués");
				}
			}
		} catch (ArrayIndexOutOfBoundsException E) {
			System.out.println(" l'origine ou la destination ne se trouve pas sur le graphe");
			System.exit(1);
		}
	}
}
