package ism.mae.views;

import java.util.List;
import java.util.Scanner;

import ism.mae.entity.Categorie;
import ism.mae.entity.Produit;
import ism.mae.service.CategorieService;
import ism.mae.service.ProduitService;

public  class RsView {
     @SuppressWarnings("FieldMayBeFinal")   
    private CategorieService categorieService;
    private ProduitService produitService;
    private  Scanner scanner=new Scanner(System.in);

    public RsView(CategorieService categorieService, ProduitService produitService) {
        this.categorieService = categorieService;
        this.produitService = produitService;
    }
    
    public Categorie saisieCategorie(){
        String libelle;
        do {
            System.out.println("Entrer le nom de la categorie: ");
          libelle=scanner.nextLine();
        } while (libelle.equals(""));
          return Categorie.builder()
                          .name(libelle)
                          .build();
             
    }
    public Produit saisieProduit(){
        String name;
        double qteStock,pu;
        int categorieId;
        do {
            System.out.println("Entrer le nom du Produit: ");
          name=scanner.nextLine();
        } while (name.equals(""));
        do {
            System.out.println("Entrer la Quantite en Stock: ");
          qteStock=scanner.nextDouble();
        } while (qteStock<=0);
        do {
            System.out.println("Entrer le Prix Unitaire: ");
          pu=scanner.nextDouble();
        } while (pu<=0);
        do {
            List<Categorie>categories=categorieService.getAllCategories();
            for (Categorie categorie : categories) {
            System.out.println(categorie);
        }
            System.out.println("Entrer l'ID de la Categorie: ");
          categorieId=scanner.nextInt();
        } while (categorieId<=0);
          return Produit.builder()
                          .name(name)
                          .qteStock(qteStock)
                          .pu(pu)
                          .categorieId(categorieId)
                          .build();
             
    }

   public  void afficheCategories(List<Categorie>categories){
      System.out.println("Liste des Categories: ");
       for (Categorie categorie : categories) {
            System.out.println(categorie);
       }
   }
   public void afficheProduits(List<Produit>produits){
    System.out.println("Liste des Produits:");
       for (Produit p : produits) {
        System.out.println(
            "Produit : " + p.getName() +
            " | Catégorie : " + p.getCategorie().getName() +
            " (ID Catégorie : " + p.getCategorie().getId() + ")"
        );
    }

   }

   public  int  menu(){
        System.out.println("******* MENU RESPONSABLE STOCK ******* ");
        System.out.println("1-Ajouter Categorie");
        System.out.println("2-Lister toutes les Categories");
        System.out.println("3-Ajouter un Produit");
        System.out.println("4-Lister les Produits");
        System.out.println("5- Lister les Produits par Categorie");
        System.out.println("6-Deconnexion");
        System.out.println("Faites votre choix");
        return scanner.nextInt();
   }

   public  void main(){
         int choix;
      do {
           choix= menu();
           scanner.nextLine();
          switch (choix) {
             case 1 -> {        
                
                 Categorie categorie=saisieCategorie();
                 if (categorieService.getCategorieByName(categorie.getName()).isPresent()) {
                     System.out.println("Cette Categorie existe Deja");
                 }else{
                     categorieService.addCategorie(categorie);
                      System.out.println("Catégorie bien ajoutée");
                 }
                 }
            case 2 -> {
              List<Categorie> categories=categorieService.getAllCategories();
              afficheCategories(categories);
                 }
            case 3 -> {
                Produit produit= saisieProduit(); 
                if (produitService.getProduitByCategory(produit.getCategorieId())!=null) {
                System.out.println("Ce Produit existe Deja pour cette Categorie");
                 }else{ 
                    produitService.addProduit(produit);
                    System.out.println("Produit bien ajouté");
                 }
                 }
            case 4 -> {
                List<Produit> produits=produitService.getAllProduits();
                afficheProduits(produits);
                 }
            case 5 -> {
                List<Categorie>categories=categorieService.getAllCategories();
                for (Categorie categorie : categories) {
                  System.out.println(categorie);
                }
                System.out.println("Entrer l'ID de la Categorie: ");
                int id=scanner.nextInt();
                List<Produit> produitsByCat=produitService.getProduitsByCategory(id);
                afficheProduits(produitsByCat);
                 }  
            case 6 -> {
                System.out.println("Deconnexion...");
                break;
                 }
                default -> {
                System.out.println("Choix invalide. Veuillez réessayer.");
            }
          }
      } while (choix!=6);
   }
       
}
