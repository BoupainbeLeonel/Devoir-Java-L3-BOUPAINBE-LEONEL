package ism.mae.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ism.mae.entity.Categorie;
import ism.mae.entity.Produit;
import ism.mae.repository.ProduitRepository;

public class ProduitRepositoryImpl implements ProduitRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/Gestion_Commercial";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    public boolean insertProduit(Produit produit) {
        String sql = """
            INSERT INTO produit (name, categorieId, qteStock, pu)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produit.getName());
            ps.setInt(2, produit.getCategorieId());
            ps.setDouble(3, produit.getQteStock());
            ps.setDouble(4, produit.getPu());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Produit> findProduitsByCategorie(int id) {
        List<Produit> produits = new ArrayList<>();

        String sql = """
            SELECT p.id, p.name, p.qteStock, p.pu,
                   c.id AS cat_id, c.name AS cat_name
            FROM produit p
            JOIN categorie c ON p.categorieId = c.id
            WHERE p.categorieId = ?
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Categorie categorie = Categorie.builder()
                        .id(rs.getInt("cat_id"))
                        .name(rs.getString("cat_name"))
                        .build();

                Produit produit = Produit.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .qteStock(rs.getDouble("qteStock"))
                        .pu(rs.getDouble("pu"))
                        .categorie(categorie)
                        .build();

                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    @Override
    public Produit findProduitByCategorie(int id) {

        String sql = """
            SELECT p.id, p.name, p.qteStock, p.pu,
                   c.id AS cat_id, c.name AS cat_name
            FROM produit p
            JOIN categorie c ON p.categorieId = c.id
            WHERE p.id = ?
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Categorie categorie = Categorie.builder()
                        .id(rs.getInt("cat_id"))
                        .name(rs.getString("cat_name"))
                        .build();

                return Produit.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .qteStock(rs.getDouble("qteStock"))
                        .pu(rs.getDouble("pu"))
                        .categorie(categorie)
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Produit> findAllProduits() {
        List<Produit> produits = new ArrayList<>();

        String sql = """
            SELECT p.id, p.name, p.qteStock, p.pu,
                   c.id AS cat_id, c.name AS cat_name
            FROM produit p
            JOIN categorie c ON p.categorieId = c.id
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Categorie categorie = Categorie.builder()
                        .id(rs.getInt("cat_id"))
                        .name(rs.getString("cat_name"))
                        .build();

                Produit produit = Produit.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .qteStock(rs.getDouble("qteStock"))
                        .pu(rs.getDouble("pu"))
                        .categorie(categorie)
                        .build();

                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }
}
