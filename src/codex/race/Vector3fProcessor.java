/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3mapFactory;
import codex.j3map.processors.J3mapPropertyProcessor;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class Vector3fProcessor implements J3mapPropertyProcessor<Vector3f> {

    @Override
    public Class<Vector3f> type() {
        return Vector3f.class;
    }
    @Override
    public Vector3f process(String str) {
        String s = J3mapFactory.getConstructorArguments(str, this);
        if (s == null) return null;
        String[] args = s.split(",");
        if (args.length != 3) return null;
        float[] values = new float[args.length];
        for (int i = 0; i < args.length; i++) {
            values[i] = Float.parseFloat(args[i].trim());
        }
        return new Vector3f(values[0], values[1], values[2]);
    }
    @Override
    public String[] export(Vector3f property) {
        return new String[] {getPropertyIdentifier()+"("+property.x+", "+property.y+", "+property.z+")"};
    }
    @Override
    public String getPropertyIdentifier() {
        return "Vector3f";
    }
    @Override
    public Vector3f[] createArray(int length) {
        return new Vector3f[length];
    }
    
}
