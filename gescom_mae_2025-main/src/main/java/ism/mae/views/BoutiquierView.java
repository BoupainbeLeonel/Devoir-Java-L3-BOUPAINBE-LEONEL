package ism.mae.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import ism.mae.entity.Commande;
import ism.mae.entity.Produit;
import ism.mae.service.CommandeService;
import ism.mae.service.ProduitService;

public class BoutiquierView {
    
 @SuppressWarnings("FieldMayBeFinal")   
    private ProduitService produitService;
    private CommandeService commandeService;
    private  Scanner scanner=new Scanner(System.in);

    public BoutiquierView(ProduitService produitService, CommandeService commandeService) {
        this.produitService = produitService;
        this.commandeService = commandeService;
    }
    
    
    public Commande saisieCommande() {
       String dateCommande;
        do {
            System.out.print("Entrer la date de commande (format yyyy-MM-dd) : ");
            dateCommande = scanner.nextLine();
            if (dateCommande.isEmpty()) {
                dateCommande = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                System.out.println("Date vide → date du jour utilisée : " + dateCommande);
            }
        } while (dateCommande.isEmpty());

        Produit produit = null;
        do {
            System.out.print("Liste des produits : \n");
            List<Produit>produits=produitService.getAllProduits();
            afficheProduits(produits);
        
            System.out.print("Entrer l’ID du produit : ");
            int idProduit = Integer.parseInt(scanner.nextLine());
            produit = produitService.getProduitByCategory(idProduit);
            if (produit == null) {
                System.out.println("Produit introuvable, veuillez réessayer !");
            }
        } while (produit == null);

        double qte;
        do {
            System.out.print("Entrer la quantité : ");
            qte = Double.parseDouble(scanner.nextLine());
        } while (qte <= 0);

        double pu = produit.getPu();
        double montant = qte * pu;

        Commande commande = Commande.builder()
                .dateCommande(dateCommande)
                .produit(produit)
                .qte(qte)
                .pu(pu)
                .montant(montant)
                .build();

        System.out.println("Commande enregistrée : " );
        return commande;
    }
    public void afficheProduits(List<Produit>produits){
        System.out.println("Liste des Produits");
       for (Produit p : produits) {
        System.out.println(
            "Produit : " + p.getName() +
            " , ID  : " + p.getId() + 
            " , Catégorie : " + p.getCategorie().getName() +
            " (ID Catégorie : " + p.getCategorie().getId() + ")"
        );
    }
   }
    public  void afficheCommande(Commande commande){
        System.out.println("Date : " + commande.getDateCommande());
        System.out.println("Produit : " + commande.getProduit().getName());
        System.out.println("Quantité : " + commande.getQte());
        System.out.println("Prix unitaire : " + commande.getPu());
        System.out.println("Montant total : " + commande.getMontant());
    }

   public  void afficheCommandes(List<Commande>commandes){
       System.out.println("Liste des Commandes: ");
    if (commandes.isEmpty()) {
        System.out.println("Aucune commande trouvée !");
        return;
    }

    for (Commande cmd : commandes) {
        System.out.println("ID Commande: " + cmd.getId() +
                           " , Date: " + cmd.getDateCommande() +
                           " , Produit: " + cmd.getProduit().getName() +
                           " , Montant: " + cmd.getMontant());
    }
   }
   public void afficheDetailCommande(int id) {
       Commande commande = commandeService.getCommandeById(id);

        if (commande == null) {
            System.out.println("Commande introuvable !");
            return;
        }

        System.out.println("Détails de la Commande: " );
        System.out.println("ID : " + commande.getId());
        System.out.println("Date : " + commande.getDateCommande());
        System.out.println("Produit : " + commande.getProduit().getName());
        System.out.println("Quantité : " + commande.getQte());
        System.out.println("Prix unitaire : " + commande.getPu());
        System.out.println("Montant total : " + commande.getMontant());
    }
   

   public  int  menu(){
        System.out.println("******* MENU BOUTIQUIER ******* ");
        System.out.println("1-Ajouter Commande");
        System.out.println("2-Lister toutes les Commandes");
        System.out.println("3-Afficher les détails d'une Commande");
        System.out.println("4-Deconnexion");
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
                 Commande commande=saisieCommande();
                 if (commandeService.getCommandeById(commande.getId()) != null) {
                     System.out.println("Cette Commande existe Deja");
                 }else{
                     commandeService.addCommande(commande);
                 }
                 }
          case 2 -> {
              List<Commande> commandes=commandeService.getAllCommandes();
              afficheCommandes(commandes);
                 }
            case 3 -> {
                List<Commande> commandes=commandeService.getAllCommandes();
                afficheCommandes(commandes);
                if (commandes.isEmpty()) {
                    break;
                }
                System.out.print("\nEntrez l'ID de la commande à afficher : ");
                int id = Integer.parseInt(scanner.nextLine());
                afficheDetailCommande(id);
                 }
            case 4 -> {
                System.out.println("Deconnexion...");
                break;
                }
            
            default -> {
                System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
      } while (choix!=4);
   }
       
}