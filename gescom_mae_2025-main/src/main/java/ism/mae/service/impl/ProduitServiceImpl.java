package ism.mae.service.impl;

import java.util.*;

import ism.mae.entity.Produit;
import ism.mae.repository.ProduitRepository;
import ism.mae.service.ProduitService;


public class ProduitServiceImpl implements ProduitService {
    @SuppressWarnings("FieldMayBeFinal")
    private ProduitRepository produitRepository;

    public ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }
    @Override
    public boolean addProduit(Produit produit) {
        return produitRepository.insertProduit(produit);
    }

    @Override
    public List<Produit> getAllProduits() {
        return produitRepository.findAllProduits();
    }

    @Override
    public List<Produit> getProduitsByCategory(int id){
        return produitRepository.findProduitsByCategorie(id);
    }

    @Override
    public Produit getProduitByCategory(int id){
        return produitRepository.findProduitByCategorie(id);
    }
        
}


