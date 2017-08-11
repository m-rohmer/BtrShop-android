package fr.inria.spirals.sensorscollect.api.manualfeature;

public class ManualFeature {

    private final int id;
    private final String name;

    public ManualFeature(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManualFeature that = (ManualFeature) o;

        if (id != that.id) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ManualFeature{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
