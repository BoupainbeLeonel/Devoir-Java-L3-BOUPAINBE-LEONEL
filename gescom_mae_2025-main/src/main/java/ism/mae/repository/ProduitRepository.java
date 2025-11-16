package ism.mae.repository;

import java.util.List;

import ism.mae.entity.Produit;

public interface  ProduitRepository {
    public boolean insertProduit(Produit produit);
    List<Produit> findProduitsByCategorie(int id);
    List<Produit> findAllProduits();
    Produit findProduitByCategorie(int id);
}
