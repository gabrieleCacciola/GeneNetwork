package dao;

import java.util.Arrays;

/**
 * Gene implementation.
 */
public class Gene {
    private String name;
    private int index;
    private double[] controls;
    private double[] psoriatics;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double[] getControls() {
        return controls;
    }

    public void setControls(double[] controls) {
        this.controls = controls;
    }

    public double[] getPsoriatics() {
        return psoriatics;
    }

    public void setPsoriatics(double[] psoriatics) {
        this.psoriatics = psoriatics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gene gene = (Gene) o;

        if (index != gene.index) return false;
        if (!name.equals(gene.name)) return false;
        if (!Arrays.equals(controls, gene.controls)) return false;
        return Arrays.equals(psoriatics, gene.psoriatics);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + index;
        result = 31 * result + Arrays.hashCode(controls);
        result = 31 * result + Arrays.hashCode(psoriatics);
        return result;
    }

    @Override
    public String toString() {
        return "Gene{" +
                "name='" + name + '\'' +
                ", index=" + index +
                ", controls=" + Arrays.toString(controls) +
                ", psoriatics=" + Arrays.toString(psoriatics) +
                '}';
    }
}
