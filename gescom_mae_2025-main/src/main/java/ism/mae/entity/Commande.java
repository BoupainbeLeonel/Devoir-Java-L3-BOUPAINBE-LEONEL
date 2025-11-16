package ism.mae.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@Builder
@ToString(exclude = {"qte","pu"})
public class Commande {
    private int id;
    private String dateCommande;
    private double montant;
    private Produit produit;
    private int produitId;
    private String nomProduit;
    private double qte;
    private double pu;
}
