package skipnode;

import java.io.Serializable;
import java.util.Objects;

// Basic skipnode.SkipNodeIdentity class
public class SkipNodeIdentity implements Serializable {
    private final String nameID;
    private final int numID;
    private final String address;
    private final int port;

    public SkipNodeIdentity(String nameID, int numID, String address, int port){
        this.nameID=nameID;
        this.numID=numID;
        this.address = address;
        this.port = port;
    }

    public String getNameID() {
        return nameID;
    }

    public int getNumID() {
        return numID;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {return port;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkipNodeIdentity that = (SkipNodeIdentity) o;
        return getNumID() == that.getNumID() &&
                getNameID().equals(that.getNameID()) &&
                getAddress().equals(that.getAddress()) &&
                getPort() == that.getPort();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNameID(), getNumID(), getAddress(), getPort());
    }
}
