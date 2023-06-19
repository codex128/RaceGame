/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author gary
 */
public class GameFactory extends GameAppState {

    public static final String[] COLORS = "red/blue/green/yellow".split("/");
    
    @Override
    protected void init(Application app) {}
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
    public Spatial createCarModel(J3map carData, String color) {
        Spatial model = assetManager.loadModel(carData.getString("model"));
        Material mat = assetManager.loadMaterial(carData.getString("material"));
        Texture tex = assetManager.loadTexture(new TextureKey(carData.getString("texture").replace((CharSequence)"$", color)));
        mat.setTexture("DiffuseMap", tex);
        model.setMaterial(mat);
        return model;
    }
    
}
