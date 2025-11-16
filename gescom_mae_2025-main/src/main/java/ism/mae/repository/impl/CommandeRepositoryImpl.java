package ism.mae.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ism.mae.entity.Categorie;
import ism.mae.entity.Commande;
import ism.mae.entity.Produit;
import ism.mae.repository.CommandeRepository;


public class CommandeRepositoryImpl implements CommandeRepository {

    @Override
    public boolean insertCommande(Commande commande) {
    int result = 0;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gestion_Commercial","root","");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO commande (date_cmd, produit_id, qte, pu, montant) VALUES (?, ?, ?, ?, ?)" );

            ps.setDate(1, Date.valueOf(commande.getDateCommande()));
            ps.setInt(2, commande.getProduit().getId());
            ps.setDouble(3, commande.getQte());
            ps.setDouble(4, commande.getPu());
            ps.setDouble(5, commande.getMontant());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur insertCommande SQL: " + e.getMessage());
        }

    } catch (ClassNotFoundException e) {
        System.out.println("Erreur Driver MySQL: " + e.getMessage());
    }

    return result != 0;
}


    @Override
    public List<Commande> findAllCommandes() {
    List<Commande> commandes = new ArrayList<>();

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gestion_Commercial","root","");
            String sql = "SELECT cmd.id AS commande_id, cmd.date_cmd, cmd.montant, "
                       + "p.id AS produit_id, p.name AS produit_name "
                       + "FROM commande cmd "
                       + "JOIN produit p ON cmd.produit_id = p.id";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Produit produit = Produit.builder()
                        .id(rs.getInt("produit_id"))
                        .name(rs.getString("produit_name"))
                        .build();

                Commande commande = Commande.builder()
                        .id(rs.getInt("commande_id"))
                        .dateCommande(rs.getDate("date_cmd").toString())
                        .montant(rs.getDouble("montant"))
                        .produit(produit)
                        .build();

                commandes.add(commande);
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL findAllCommandes: " + e.getMessage());
        }

    } catch (ClassNotFoundException e) {
        System.out.println("Erreur Driver MySQL: " + e.getMessage());
    }

    return commandes;
    }


    @Override
    public Commande findCommandeById(int id) {
    Commande commande = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gestion_Commercial","root","");
            String sql = "SELECT c.*, p.id AS p_id, p.name AS p_name, p.pu AS p_pu, "
                       + "cat.id AS cat_id, cat.name AS cat_name "
                       + "FROM commande c "
                       + "JOIN produit p ON p.id = c.produit_id "
                       + "JOIN categorie cat ON cat.id = p.categorieId "
                       + "WHERE c.id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Categorie categorie = Categorie.builder()
                        .id(rs.getInt("cat_id"))
                        .name(rs.getString("cat_name"))
                        .build();
                Produit produit = Produit.builder()
                        .id(rs.getInt("p_id"))
                        .name(rs.getString("p_name"))
                        .categorieId(rs.getInt("cat_id"))
                        .categorie(categorie)
                        .build();
                commande = Commande.builder()
                        .id(rs.getInt("id"))
                        .produit(produit)
                        .qte(rs.getDouble("qte"))
                        .pu(rs.getDouble("pu"))
                        .montant(rs.getDouble("montant"))
                        .dateCommande(rs.getDate("date_cmd").toString())
                        .build();
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL findCommandeById: " + e.getMessage());
        }

    } catch (ClassNotFoundException e) {
        System.out.println("Erreur Driver MySQL: " + e.getMessage());
    }

    return commande;
    }

}


